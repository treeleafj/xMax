package org.treeleafj.xmax.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 *
 * @Author leaf
 * 2015/10/12 0012 22:30.
 */
public class PageResult<T> implements Serializable {

    private Pageable pageable;

    /**
     * 查出来的数据
     */
    private List<T> list;

    /**
     * 总条数
     */
    private long total;

    public PageResult() {
    }

    /**
     * 构造分页查询结果对象
     *
     * @param pageable 分页请求
     * @param list     查询的结果数据
     */
    public PageResult(Pageable pageable, List<T> list) {
        this(pageable, list, Long.valueOf(String.valueOf(list.size())));
    }

    /**
     * 构造分页查询结果对象
     *
     * @param pageable 分页请求
     * @param list     查询的结果数据
     * @param total    总条数
     */
    public PageResult(Pageable pageable, List<T> list, long total) {
        this.pageable = pageable;
        this.list = list;
        this.total = total;
    }

    /**
     * 构造分页查询结果对象
     *
     * @param pageNo   页码
     * @param pageSize 每页大小
     * @param list     查询的结果数据
     */
    public PageResult(int pageNo, int pageSize, List<T> list) {
        this(new Pageable(pageNo, pageSize), list);
    }

    /**
     * 构造分页查询结果对象
     *
     * @param pageNo   页码
     * @param pageSize 每页大小
     * @param list     查询的结果数据
     * @param total    总条数
     */
    public PageResult(int pageNo, int pageSize, List<T> list, long total) {
        this(new Pageable(pageNo, pageSize), list, total);
    }

    /**
     * 获取总分页数
     *
     * @return
     */
    public long getTotalPage() {
        long l = total / this.pageable.getPageSize();
        return total % this.pageable.getPageSize() == 0 ? l : l + 1;
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<T> getList() {
        return list;
    }

    public long getTotal() {
        return total;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public PageResult setPageable(Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    public PageResult setList(List<T> list) {
        this.list = list;
        return this;
    }

    public PageResult setTotal(long total) {
        this.total = total;
        return this;
    }
}
