package cz.vutbr.fit.openmrdp.security;

import javax.net.ssl.*;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author Jiri Koudelka
 * @since 15.05.2018
 */
public final class ConnectionTrustVerifier {

    private static final SelfSignedHostnameVerifier TRUSTING_HOSTNAME_VERIFIER = new SelfSignedHostnameVerifier();
    private static SSLSocketFactory factory;

    public static void trustSelfSignedCertificates(HttpURLConnection conn) throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection httpsConnection = (HttpsURLConnection) conn;
        SSLSocketFactory factory = initializeFactory();
        httpsConnection.setSSLSocketFactory(factory);
        httpsConnection.setHostnameVerifier(TRUSTING_HOSTNAME_VERIFIER);
    }

    private static synchronized SSLSocketFactory initializeFactory() throws NoSuchAlgorithmException, KeyManagementException {
        if (factory == null) {
            SSLContext ctx = SSLContext.getInstance(SecurityConstants.SSL_CONTEXT);
            ctx.init(null, new TrustManager[]{ new AlwaysTrustManager() }, null);

            factory = ctx.getSocketFactory();
        }

        return factory;
    }

    private static final class SelfSignedHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class AlwaysTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
            // Do nothing
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
            // Do nothing
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}
