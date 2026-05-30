package com.claw.common;

import com.claw.common.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class Result<T>
implements Serializable {
    private int code;
    private String message;
    private T data;
    private long timestamp = System.currentTimeMillis();

    private Result() {
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.setCode(ErrorCode.SUCCESS.getCode());
        result.setMessage(ErrorCode.SUCCESS.getMessage());
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setCode(ErrorCode.SUCCESS.getCode());
        result.setMessage(ErrorCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<T>();
        result.setCode(ErrorCode.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        Result<T> result = new Result<T>();
        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());
        return result;
    }

    public static <T> Result<T> error(ErrorCode errorCode, String message) {
        Result<T> result = new Result<T>();
        result.setCode(errorCode.getCode());
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Result)) {
            return false;
        }
        Result other = (Result)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getCode() != other.getCode()) {
            return false;
        }
        if (this.getTimestamp() != other.getTimestamp()) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        Object this$data = this.getData();
        Object other$data = other.getData();
        return !(this$data == null ? other$data != null : !this$data.equals(other$data));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Result;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getCode();
        long $timestamp = this.getTimestamp();
        result = result * 59 + (int)($timestamp >>> 32 ^ $timestamp);
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        T $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        return "Result(code=" + this.getCode() + ", message=" + this.getMessage() + ", data=" + this.getData() + ", timestamp=" + this.getTimestamp() + ")";
    }
}
