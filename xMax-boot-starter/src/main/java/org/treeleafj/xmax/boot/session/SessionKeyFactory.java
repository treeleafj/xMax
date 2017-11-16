package org.treeleafj.xmax.boot.session;

/**
 * @author leaf
 * @date 2017/11/16
 */
public class SessionKeyFactory {

    private static SessionKey DEFAULT_SESSION_KEY = new SessionKey("_login_user");

    public static SessionKey buildDefaultSessionKey() {
        return DEFAULT_SESSION_KEY;
    }
}
