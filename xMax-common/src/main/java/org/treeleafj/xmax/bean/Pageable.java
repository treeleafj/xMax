package org.treeleafj.xmax.bean;

import lombok.Data;
import org.treeleafj.xmax.safe.Assert;

import java.io.Serializable;

/**
 * 分页请求对象
 *
 * @Author leaf
 * 2015/10/28 0028 23:04.
 */
@Data
public class Pageable implements Serializable {

    /**
     * 页码,从1开始
     */
    private int pageNo = 1;

    /**
     * 每页大小,从1开始
     */
    private int pageSize = 10;

    /**
     * 是否统计总记录数
     */
    private boolean countRows = true;

    public Pageable() {
    }

    public Pageable(int pageNo, int pageSize) {
        Assert.isTrue(pageNo > 0, "pageNo不能小于1");
        Assert.isTrue(pageSize > 0, "pageSize不能小于1");

        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return (this.pageNo - 1) * this.pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置页码,从1开始,兼容Extjs等框架
     *
     * @param page
     */
    public Pageable setPage(int page) {
        this.setPageNo(page);
        return this;
    }

    /**
     * 设置每页大小,兼容Extjs等框架
     *
     * @param limit
     */
    public Pageable setLimit(int limit) {
        this.setPageSize(limit);
        return this;
    }
}
