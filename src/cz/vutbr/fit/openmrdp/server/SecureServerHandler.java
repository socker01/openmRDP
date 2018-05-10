package cz.vutbr.fit.openmrdp.server;

import com.google.common.collect.Lists;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import cz.vutbr.fit.openmrdp.cache.ClientEntry;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.HeaderType;
import cz.vutbr.fit.openmrdp.security.UserAuthorizator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public final class SecureServerHandler implements HttpHandler {

    //TODO udrzovat nejen IP a login ale take samotny hash kvuli podvrzeni
    //TODO platnost 1 hodinu
    private final Map<String, List<String>> authorizedUsers;
    private final UserAuthorizator userAuthorizator;
    private final Map<ClientEntry, BaseMessage> preparedMessages;

    public SecureServerHandler(Map<String, List<String>> authorizedUsers, UserAuthorizator userAuthorizator, Map<ClientEntry, BaseMessage> preparedMessages) {
        this.authorizedUsers = authorizedUsers;
        this.userAuthorizator = userAuthorizator;
        this.preparedMessages = preparedMessages;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String clientAddress = String.valueOf(httpExchange.getRequestHeaders().get(HeaderType.CLIENT_ADDRESS.getHeaderCode()).get(0));
        int sequenceNumber = Integer.parseInt(httpExchange.getRequestHeaders().get(HeaderType.NSEQ.getHeaderCode()).get(0));

        httpExchange.getResponseHeaders().add(HeaderType.ACCESS_CONTROL_ALLOW_ORIGIN.getHeaderCode(), "*");
        httpExchange.getResponseHeaders().add(HeaderType.NSEQ.getHeaderCode(), String.valueOf(sequenceNumber + 1));

        String authorizationString = getDecodedAuthorizationString(httpExchange);

        String response;
        if (authenticateUser(authorizationString, clientAddress)) {
            ClientEntry key = new ClientEntry(clientAddress, sequenceNumber - 2);
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

    private String getDecodedAuthorizationString(HttpExchange httpExchange) {
        String encodedAuthString = String.valueOf(httpExchange.getRequestHeaders().get(HeaderType.AUTHORIZATION.getHeaderCode()).get(0));

        return new String(Base64.getDecoder().decode(encodedAuthString));
    }

    private boolean authenticateUser(String authString, String clientAddress) {
        int delimiterIndex = authString.indexOf(":");
        String login = authString.substring(0, delimiterIndex);
        String password = authString.substring(delimiterIndex + 1);

        if (authorizedUsers.containsKey(clientAddress)) {
            if (authorizedUsers.get(clientAddress).contains(login)) {
                return true;
            }
        }

        if (userAuthorizator.authorizeUser(login, password)) {
            if (authorizedUsers.containsKey(clientAddress)) {
                authorizedUsers.get(clientAddress).add(login);
            } else {
                authorizedUsers.put(clientAddress, Lists.newArrayList(login));
            }

            return true;
        }

        return false;
    }
}
