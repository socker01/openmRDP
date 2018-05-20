package cz.vutbr.fit.openmrdp.messages;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.security.AuthorizationLevel;

/**
 * Object that represents Connection Information Message
 *
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public final class ConnectionInformationMessage {
    @NotNull
    private final String serverAddress;
    @NotNull
    private final MessageProtocol messageProtocol;
    @NotNull
    private final AuthorizationLevel authorizationLevel;

    ConnectionInformationMessage(@NotNull String serverAddress, @NotNull MessageProtocol messageProtocol, @NotNull AuthorizationLevel authorizationLevel) {
        this.serverAddress = Preconditions.checkNotNull(serverAddress);
        this.messageProtocol = Preconditions.checkNotNull(messageProtocol);
        this.authorizationLevel = Preconditions.checkNotNull(authorizationLevel);
    }

    @NotNull
    public String getServerAddress() {
        return serverAddress;
    }

    @NotNull
    public MessageProtocol getMessageProtocol() {
        return messageProtocol;
    }

    @NotNull
    public AuthorizationLevel getAuthorizationLevel() {
        return authorizationLevel;
    }
}
