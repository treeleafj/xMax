package org.treeleafj.xmax.bean;

import lombok.Data;

/**
 * 客户端信息工具
 *
 * @Author leaf
 * 2015/9/4 0004 18:58.
 */
@Data
public class ClientInfo {

    /**
     * 客户端IP
     */
    private String ip;

    /**
     * 客户端是否是移动端
     */
    private boolean mobile;
}
