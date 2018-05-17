package cz.vutbr.fit.openmrdp.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import cz.vutbr.fit.openmrdp.cache.ClientEntry;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.HeaderType;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.Map;

/**
 * Non-secure server handler which implements {@link HttpHandler}.
 *
 * This handler is used for service of the HTTP requests
 *
 * @author Jiri Koudelka
 * @since 29.04.2018
 */
public final class NonSecureServerHandler implements HttpHandler {

    private static final int NON_SECURE_GET_MESSAGES_COUNT = 2;

    private final Map<ClientEntry, BaseMessage> preparedMessages;

    public NonSecureServerHandler(Map<ClientEntry, BaseMessage> preparedMessages) {
        this.preparedMessages = preparedMessages;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String clientAddress = String.valueOf(httpExchange.getRequestHeaders().get(HeaderType.CLIENT_ADDRESS.getHeaderCode()).get(0));
        int sequenceNumber = Integer.parseInt(httpExchange.getRequestHeaders().get(HeaderType.NSEQ.getHeaderCode()).get(0));

        ClientEntry key = new ClientEntry(clientAddress.substring(0, clientAddress.length()-1), sequenceNumber - NON_SECURE_GET_MESSAGES_COUNT, Instant.now());
        String response = preparedMessages.get(key).getMessageBody().getQuery();

        httpExchange.getResponseHeaders().add(HeaderType.ACCESS_CONTROL_ALLOW_ORIGIN.getHeaderCode(), "*");
        httpExchange.getResponseHeaders().add(HeaderType.NSEQ.getHeaderCode(), String.valueOf(sequenceNumber + 1));
        httpExchange.sendResponseHeaders(ResponseCode.OK.getCode(), response.length());

        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

        preparedMessages.remove(key);
    }
}
