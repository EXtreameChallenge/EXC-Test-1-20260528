/*
 * Decompiled with CFR 0.152.
 */
package com.claw.common;

import java.io.Serializable;
import java.util.List;

public class PageResult<T>
implements Serializable {
    private List<T> list;
    private long total;
    private int page;
    private int size;
    private int totalPages;

    public PageResult(List<T> list, long total, int page, int size) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.size = size;
        this.totalPages = (int)Math.ceil((double)total / (double)size);
    }

    public List<T> getList() {
        return this.list;
    }

    public long getTotal() {
        return this.total;
    }

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PageResult)) {
            return false;
        }
        PageResult other = (PageResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTotal() != other.getTotal()) {
            return false;
        }
        if (this.getPage() != other.getPage()) {
            return false;
        }
        if (this.getSize() != other.getSize()) {
            return false;
        }
        if (this.getTotalPages() != other.getTotalPages()) {
            return false;
        }
        List<T> this$list = this.getList();
        List<T> other$list = other.getList();
        return !(this$list == null ? other$list != null : !((Object)this$list).equals(other$list));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageResult;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $total = this.getTotal();
        result = result * 59 + (int)($total >>> 32 ^ $total);
        result = result * 59 + this.getPage();
        result = result * 59 + this.getSize();
        result = result * 59 + this.getTotalPages();
        List<T> $list = this.getList();
        result = result * 59 + ($list == null ? 43 : ((Object)$list).hashCode());
        return result;
    }

    public String toString() {
        return "PageResult(list=" + this.getList() + ", total=" + this.getTotal() + ", page=" + this.getPage() + ", size=" + this.getSize() + ", totalPages=" + this.getTotalPages() + ")";
    }
}
