package com.hh.urm.notify.consts;

/**
 * @ClassName: NotifyConst
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/3 16:30
 * @Version: 1.0
 */
public class NotifyConst {

    public static final String LACK_TEMPLATE_PARAMS_MSG = "缺少模板参数";
    public static final String NOTIFY_TYPE_NOT_IN_RULES = "请检查通知方式是否符合规范";
    public static final String NO_REQUEST_DATA = "no data";
    /**
     * 接收者
     */
    public final static String RECEIVER = "receiver";
    public static final String REQUEST_ID = "requestId";

    public static final String ERROR_MAPS_LIST = "errorMapsList";
    public static final String APP_CODE = "appCode";
    public static final String SMS_CODE = "smsCode";
    /**
     * 模板code
     */
    public final static String TEMPLATE_CODE = "templateCode";
    /**
     * 模板名称
     */
    public final static String TEMPLATE_NAME = "templateName";

    /**
     * 模板Id
     */
    public final static String TEMPLATE_ID = "templateId";
    /**
     * 系统
     */
    public static final String SYSTEM = "system";

    public enum KafkaStateEnums {
        /**
         * 异常
         */
        EXCEPTION(-1),
        /**
         * 就绪
         */
        READY(0),
        /**
         * 成功
         */
        SUCCESS(1),
        /**
         * 失败
         */
        FAILED(2);

        private final int code;

        KafkaStateEnums(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public class Sms {
        /**
         * AppKey
         */
        public final static String APP_KEY = "appKey";
        /**
         * appSecret
         */
        public final static String APP_SECRET = "appSecret";
        /**
         * 签明
         */
        public final static String SIGNATURE = "signature";

        /**
         * 拓展字段
         */
        public final static String EXTEND = "extend";
        /**
         * 回调地址
         */
        public final static String STATUS_CALL_BACK = "statusCallback";
        /**
         * 模板参数
         */
        public final static String TEMPLATE_PARAMS = "templateParas";
        /**
         * 发送方
         */
        public final static String FROM = "from";
        /**
         * 接收方
         */
        public final static String TO = "to";
        /**
         * 通道号
         */
        public final static String SENDER = "sender";
        /**
         * 短信内容
         */
        public final static String SMS_CONTENT = "smsContent";
    }
}
