package cz.vutbr.fit.openmrdp.security;

import java.util.HashMap;
import java.util.Map;

/**
 * Test implementation of the {@link UserAuthorizator}. This implementation validates users against the static {@link Map}.
 * <p>
 * Do not use this implementation in the production code!
 *
 * @author Jiri Koudelka
 * @since 25.04.2018
 */
public final class UserAuthorizatorTestImpl implements UserAuthorizator {

    private static final Map<String, String> users = new HashMap<>();

    static {
        users.put("testUser", "Password123");
        users.put("testUser1", "pass123456");
    }

    @Override
    public boolean authorizeUser(String login, String password) {
        for (String userName : users.keySet()) {
            if (users.get(userName).equals(password)) {
                return true;
            }
        }

        return false;
    }
}
