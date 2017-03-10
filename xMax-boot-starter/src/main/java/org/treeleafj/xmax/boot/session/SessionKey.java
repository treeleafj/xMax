package org.treeleafj.xmax.boot.session;

/**
 * @author leaf
 * @date 2017-02-10 15:59
 */
public class SessionKey {

    private String key;

    public SessionKey() {
    }

    public SessionKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public SessionKey setKey(String key) {
        this.key = key;
        return this;
    }
}
