package com.dch.vo;

import org.apache.shiro.ShiroException;

/**
 * Created by sunkqa on 2018/8/1.
 */
public class PlatFormException extends ShiroException {
    public PlatFormException() {
    }

    public PlatFormException(String message) {
        super(message);
    }

    public PlatFormException(Throwable cause) {
        super(cause);
    }

    public PlatFormException(String message, Throwable cause) {
        super(message, cause);
    }
}
