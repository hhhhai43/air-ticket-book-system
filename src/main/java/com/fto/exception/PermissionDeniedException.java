package com.fto.exception;

/**
 * 权限不匹配异常
 */
public class PermissionDeniedException extends BaseException{
    public PermissionDeniedException() {
    }

    public PermissionDeniedException(String msg) {
        super(msg);
    }

}
