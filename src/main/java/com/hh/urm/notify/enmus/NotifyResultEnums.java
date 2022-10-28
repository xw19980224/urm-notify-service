package com.hh.urm.notify.enmus;

/**
 * 通知接口返回状态枚举
 */
public enum NotifyResultEnums {
    FAULT(0, "失败"),
    SUCCESS(1, "成功"),
    NO_TRACE_ID(0, "参数不足");

    private int index;
    private String msg;

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
