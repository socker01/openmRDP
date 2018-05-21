package cz.vutbr.fit.openmrdp.security;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class SecurityConfigurationFactoryTest {

    private static final String EXAMPLE_KEYSTORE_PATH = "examplekeystore.jks";
    private static final String EXAMPLE_PASSWORD = "password";

    @Test
    public void createSecureSecurityConfiguration() {
        UserAuthorizator userAuthorizator = new UserAuthorizatorTestImpl();

        SecurityConfiguration securityConfiguration = SecurityConfigurationFactory.createSecureSecurityConfiguration(
                userAuthorizator, EXAMPLE_KEYSTORE_PATH, EXAMPLE_PASSWORD);

        assertThat(securityConfiguration.getUserAuthorizator(), is(userAuthorizator));
        assertThat(securityConfiguration.isSecureConnectionSupported(), is(true));
        assertThat(securityConfiguration.getKeyStorePath(), is(EXAMPLE_KEYSTORE_PATH));
        assertThat(securityConfiguration.getKeyStorePassword(), is(EXAMPLE_PASSWORD));
    }

    @Test
    public void createNonSecureSecurityConfiguration() {
        SecurityConfiguration securityConfiguration = SecurityConfigurationFactory.createNonSecureSecurityConfiguration();

        assertThat(securityConfiguration.isSecureConnectionSupported(), is(false));
        assertThat(securityConfiguration.getUserAuthorizator(), is(nullValue()));
        assertThat(securityConfiguration.getKeyStorePath(), is(nullValue()));
        assertThat(securityConfiguration.getKeyStorePassword(), is(nullValue()));
    }

}