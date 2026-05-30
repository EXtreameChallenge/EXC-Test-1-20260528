package com.claw.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.Serializable;

public class PageQuery
implements Serializable {
    @Min(value=1L, message="\u9875\u7801\u6700\u5c0f\u4e3a1")
    private @Min(value=1L, message="\u9875\u7801\u6700\u5c0f\u4e3a1") int page = 1;
    @Min(value=1L, message="\u6bcf\u9875\u6761\u6570\u6700\u5c0f\u4e3a1")
    @Max(value=100L, message="\u6bcf\u9875\u6761\u6570\u6700\u5927\u4e3a100")
    private @Min(value=1L, message="\u6bcf\u9875\u6761\u6570\u6700\u5c0f\u4e3a1") @Max(value=100L, message="\u6bcf\u9875\u6761\u6570\u6700\u5927\u4e3a100") int size = 20;

    public int getOffset() {
        return (this.page - 1) * this.size;
    }

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PageQuery)) {
            return false;
        }
        PageQuery other = (PageQuery)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPage() != other.getPage()) {
            return false;
        }
        return this.getSize() == other.getSize();
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageQuery;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPage();
        result = result * 59 + this.getSize();
        return result;
    }

    public String toString() {
        return "PageQuery(page=" + this.getPage() + ", size=" + this.getSize() + ")";
    }
}
