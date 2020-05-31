package org.treeleafj.xmax.boot.handler.security;

import org.junit.Assert;
import org.junit.Test;

public class XssValidatiorTest {

    @Test
    public void isXss() {

        Assert.assertFalse(XssValidatior.isXss("a", "dasdnioqw"));
        Assert.assertTrue(XssValidatior.isXss("a<>", "dasdnioqwdasdnioqw"));
        Assert.assertTrue(XssValidatior.isXss("123", "dasdnioqw\"dasdnioqw"));
        Assert.assertTrue(XssValidatior.isXss("", "dasdnioqw'dasdnioqw"));
        Assert.assertFalse(XssValidatior.isXss("", "{\"status\":1,\"msg\":\"success\"}"));
        Assert.assertTrue(XssValidatior.isXss("", "{\"status\":1,\"msg\":\"suc<>cess\"}"));
        Assert.assertTrue(XssValidatior.isXss("", "{\"sta<>tus\":1,\"msg\":\"success\"}"));
        Assert.assertFalse(XssValidatior.isXss("", "{\"status\":1,\"msg\":\"success\", \"data\":[\"a\", \"b\"]}"));
        Assert.assertTrue(XssValidatior.isXss("", "{\"status\":1,\"msg\":\"success\", \"data\":[{\"a\":\"b&b\"}]}"));

    }
}