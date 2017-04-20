package org.treeleafj.xmax.export;

import lombok.Data;

import java.util.Date;

/**
 * Created by leaf on 2017/4/20.
 */
@Data
public class Merchant {

    private String merchantNo;
    private String name;
    private Double balance;
    private Date createTime;

}
