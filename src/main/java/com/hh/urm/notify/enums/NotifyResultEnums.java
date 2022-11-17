package com.hh.urm.notify.enums;

/**
 * 通知接口返回状态枚举
 */
public enum NotifyResultEnums {
    /**
     * 失败
     */
    FAULT(0, "失败"),
    /**
     * 成功
     */
    SUCCESS(1, "成功"),
    /**
     *参数不足
     */
    NO_TRACE_ID(0, "参数不足"),
    /**
     * 校验未通过
     */
    VERIFY_FAILED(0, "校验未通过");
    private final int index;
    private final String msg;

    NotifyResultEnums(int index, String msg) {
        this.index = index;
        this.msg = msg;
    }

    public int getIndex() {
        return index;
    }

    public String getMsg() {
        return msg;
    }
}
