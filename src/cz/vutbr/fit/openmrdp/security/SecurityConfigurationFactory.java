package cz.vutbr.fit.openmrdp.security;

import com.sun.istack.internal.NotNull;

/**
 * @author Jiri Koudelka
 * @since 07.05.2018
 */
public final class SecurityConfigurationFactory {

    public static SecurityConfiguration createSecureSecurityConfiguration(@NotNull UserAuthorizator userAuthorizator){
        return new SecurityConfiguration(true, userAuthorizator);
    }

    public static SecurityConfiguration createNonSecureSecurityConfiguration(){
        return new SecurityConfiguration(false, null);
    }
}
