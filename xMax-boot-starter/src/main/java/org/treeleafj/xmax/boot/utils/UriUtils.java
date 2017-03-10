package org.treeleafj.xmax.boot.utils;

/**
 * @author leaf
 * @date 2017-03-10 16:40
 */
public class UriUtils {

    /**
     * 获取地址的后缀
     *
     * @param path
     * @return
     */
    public static String getExt(String path) {
        String ext = "";
        int index = path.lastIndexOf(".");
        if (index > 0) {
            ext = path.substring(index);
        }
        return ext;
    }

}
