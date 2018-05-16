package cz.vutbr.fit.openmrdp.server;

import com.google.common.collect.Lists;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import cz.vutbr.fit.openmrdp.cache.ClientAccessMetadata;
import cz.vutbr.fit.openmrdp.cache.ClientEntry;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.HeaderType;
import cz.vutbr.fit.openmrdp.security.UserAuthorizator;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public final class SecureServerHandler implements HttpHandler {

    private final Map<String, List<ClientAccessMetadata>> authorizedUsers;
    private final UserAuthorizator userAuthorizator;
    private final Map<ClientEntry, BaseMessage> preparedMessages;

    public SecureServerHandler(UserAuthorizator userAuthorizator, Map<ClientEntry, BaseMessage> preparedMessages) {
        this.authorizedUsers = new HashMap<>();
        this.userAuthorizator = userAuthorizator;
        this.preparedMessages = preparedMessages;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        invalidateLongTimeLoggedUsers();
        String clientAddress = String.valueOf(httpExchange.getRequestHeaders().get(HeaderType.CLIENT_ADDRESS.getHeaderCode()).get(0));
        int sequenceNumber = Integer.parseInt(httpExchange.getRequestHeaders().get(HeaderType.NSEQ.getHeaderCode()).get(0));

        httpExchange.getResponseHeaders().add(HeaderType.ACCESS_CONTROL_ALLOW_ORIGIN.getHeaderCode(), "*");
        httpExchange.getResponseHeaders().add(HeaderType.NSEQ.getHeaderCode(), String.valueOf(sequenceNumber + 1));

        String authorizationString = getDecodedAuthorizationString(httpExchange);

        String response;
        if (authenticateUser(authorizationString, clientAddress)) {
            ClientEntry key = new ClientEntry(clientAddress.substring(0, clientAddress.length() - 1), sequenceNumber - 2, Instant.now());
            response = preparedMessages.get(key).getMessageBody().getQuery();
            httpExchange.sendResponseHeaders(ResponseCode.OK.getCode(), response.length());
        } else {
            response = "Invalid login or password.";
            httpExchange.sendResponseHeaders(ResponseCode.FORBIDDEN.getCode(), response.length());
        }

        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void invalidateLongTimeLoggedUsers() {
        for (String clientAddress : authorizedUsers.keySet()) {
            List<ClientAccessMetadata> clientAccessMetadataList = authorizedUsers.get(clientAddress);

            List<ClientAccessMetadata> metadataToRemove = new ArrayList<>();
            for (ClientAccessMetadata clientAccessMetadata : clientAccessMetadataList) {
                if (clientAccessMetadata.getLastAccess().toEpochMilli() < Instant.now().minus(30, ChronoUnit.MINUTES).toEpochMilli()) {
                    metadataToRemove.add(clientAccessMetadata);
                }
            }

            clientAccessMetadataList.removeAll(metadataToRemove);
        }
    }

    private String getDecodedAuthorizationString(HttpExchange httpExchange) {
        String encodedAuthString = String.valueOf(httpExchange.getRequestHeaders().get(HeaderType.AUTHORIZATION.getHeaderCode()).get(0));

        return new String(Base64.getDecoder().decode(encodedAuthString));
    }

    private boolean authenticateUser(String authString, String clientAddress) {
        int delimiterIndex = authString.indexOf(":");
        String login = authString.substring(0, delimiterIndex);
        String password = authString.substring(delimiterIndex + 1);

        ClientAccessMetadata clientAccessMetadata = new ClientAccessMetadata(login, authString, Instant.now());

        if (authorizedUsers.containsKey(clientAddress)) {
            if (authorizedUsers.get(clientAddress).contains(clientAccessMetadata)) {
                return true;
            }
        }

        if (userAuthorizator.authorizeUser(login, password)) {
            if (authorizedUsers.containsKey(clientAddress)) {
                authorizedUsers.get(clientAddress).add(clientAccessMetadata);
            } else {
                authorizedUsers.put(clientAddress, Lists.newArrayList(clientAccessMetadata));
            }

            return true;
        }

        return false;
    }
}
