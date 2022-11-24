package com.hh.urm.notify.consts;

/**
 * @ClassName: DBTableConst
 * @Author: MaxWell
 * @Description: 数据库表参量
 * @Date: 2022/11/4 15:59
 * @Version: 1.0
 */
public class DbTableConst {


    //Postgres

    /**
     * t_app_template
     */
    static class AppTemplate {
        /**
         *
         */
        public static final String PUSH_TYPE_S = "pushTypes";
    }

    // MongoDB

    /**
     * t_user_send_history表
     */
    public static class UserSendHistory {
        /**
         * 用户消息历史表
         */
        public final static String T_USER_SEND_HISTORY = "t_user_send_history";

        /**
         * name
         */
        public static final String NAME = "name";
    }

}
