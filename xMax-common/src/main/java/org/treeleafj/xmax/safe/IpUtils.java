package org.treeleafj.xmax.safe;

import lombok.Data;
import org.treeleafj.xmax.exception.ServiceException;
import org.treeleafj.xmax.http.httpclient.Get;
import org.treeleafj.xmax.json.Jsoner;

import java.util.Map;

/**
 * ip工具
 * <p>
 * Created by leaf on 2016/5/4.
 */
public class IpUtils {

    /**
     * 基于淘宝的ip地址查询接口查询ip地址
     *
     * @param ip
     * @return
     */
    public static IpAddress query(String ip) {
        String r = new Get("http://ip.taobao.com/service/getIpInfo2.php").param("ip", ip).send();
        Map result = Jsoner.toObj(r, Map.class);
        if (!"0".equals(String.valueOf(result.get("code")))) {
            throw new ServiceException("查询ip失败," + result.get("data"));
        }

        return Jsoner.toObj(result.get("data").toString(), IpAddress.class);
    }

    /**
     * ip地址
     */
    @Data
    public static class IpAddress {

        /**
         * 地区(华南,华中,华北等)
         */
        private String area;

        /**
         * 国家
         */
        private String country;

        /**
         * 省份
         */
        private String region;

        /**
         * 城市
         */
        private String city;

        /**
         * 运营商
         */
        private String isp;
    }
}
