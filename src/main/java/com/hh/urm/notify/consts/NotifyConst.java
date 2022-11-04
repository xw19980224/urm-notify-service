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

    public enum TopicEnums {
        /**
         * all
         */
        ALL("0", "all"),
        /**
         * 短信
         */
        SMS("1", "sms");

        private final String code;
        private final String topicName;

        TopicEnums(String code, String topicName) {
            this.code = code;
            this.topicName = topicName;
        }

        public String getCode() {
            return code;
        }

        public String getTopicName() {
            return topicName;
        }
    }

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
}
