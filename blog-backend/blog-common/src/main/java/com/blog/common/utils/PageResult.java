package com.blog.common.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private long total;
    private List<T> list;
    private long pageNum;
    private long pageSize;
    private long totalPages;

    public PageResult() {
    }

    public PageResult(long total, List<T> list, long pageNum, long pageSize) {
        this.total = total;
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = (total + pageSize - 1) / pageSize;
    }
}
