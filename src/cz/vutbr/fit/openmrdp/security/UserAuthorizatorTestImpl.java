package cz.vutbr.fit.openmrdp.security;

/**
 * @author Jiri Koudelka
 * @since 25.04.2018
 */
public final class UserAuthorizatorTestImpl implements UserAuthorizator {

    //TODO static array of users
    @Override
    public boolean authorizeUser(String login, String password) {
        return login.equals("testUser") && password.equals("Password123");
    }
}
