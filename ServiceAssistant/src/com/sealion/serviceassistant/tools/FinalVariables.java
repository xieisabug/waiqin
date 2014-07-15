package com.sealion.serviceassistant.tools;

public class FinalVariables {
    //-----------------------------------工单状态-------------------------------
    //0：未阅读，1:已经阅读，2:接受任务，
    // 3：申请改派，4：改派审批通过，5：改派审批未通过，
    // 6：到达现场，7: 电话解决， 10：工单完成
    /**
     * 0：未阅读
     */
    public static final int ORDER_STATE_NOT_READ = 0;
    /**
     * 1:已经阅读
     */
    public static final int ORDER_STATE_IS_READ = 1;
    /**
     * 2:接受任务
     */
    public static final int ORDER_STATE_ACCEPT_TASK = 2;
    /**
     * 3：申请改派
     */
    public static final int ORDER_STATE_APPLY_CHANGE = 3;
    //public static final int ORDER_STATE_APPLY_PASS = 4;
    //public static final int ORDER_STATE_APPLY_NOT_PASS = 5;
    /**
     * 6：到达现场
     */
    public static final int ORDER_STATE_ARRIVE_TARGET = 6;
    /**
     * 7: 电话解决
     */
    public static final int ORDER_STATE_PHONE_SOLVE = 7;
    /**
     * 10：工单完成
     */
    public static final int ORDER_STATE_ORDER_COMPLISH = 10;

    //-----------------------------------------------------------------------

    //-----------------------------------工单完成状态-------------------------------
    /**
     * 现场解决
     */
    public static final int ORDER_FINISH_TYPE_SPOT_SOLVE = 1; //现场解决
    /**
     * 电话解决
     */
    public static final int ORDER_FINISH_TYPE_PHONE_SOLVE = 2; //电话解决
    /**
     * 改派
     */
    public static final int ORDER_FINISH_TYPE_CHANGE_SOLVE = 3; //改派
    /**
     * 回访工单
     */
    public static final int ORDER_FINISH_TYPE_VISIT_SOLVE = 4;  //回访工单
    //-----------------------------------------------------------------------

    //-----------------------------------显示工单类型----------------------------
    /**
     * 0:显示全部工单
     */
    public static final int ORDER_TYPE_ALL = 0;
    /**
     * 1:显示新工单
     */
    public static final int ORDER_TYPE_NEW = 1;
    /**
     * 2:显示未完成工单
     */
    public static final int ORDER_TYPE_NOT_COMPLISH = 2;
    /**
     * 3:显示已完成工单
     */
    public static final int ORDER_TYPE_COMPLISH = 3;
    /**
     * 4:显示紧急工单
     */
    public static final int ORDER_TYPE_URGENCY = 4;
    /**
     * 5: 显示回访工单
     */
    public static final int ORDER_TYPE_BACK = 5;
    /**
     * 6: 显示维护工单
     */
    public static final int ORDER_TYPE_MAINTAIN = 6;
    //-----------------------------------------------------------------------

    //-----------------------------------更多菜单-------------------------------
    /**
     * 重置密码
     */
    public static final int MORE_MENU_CHANGE_PWD = 0;
    /**
     * 同步基础数据
     */
    public static final int MORE_MENU_SYNC_DATA = 1;
    /**
     * 版本升级
     */
    public static final int MORE_MENU_VERSION_UPDATE = 2;
    /**
     * 上传文件
     */
    public static final int MORE_MENU_UPLOAD_FILE = 3;
    /**
     * wifi设置
     */
    public static final int MORE_MENU_WIFI_SETTING = 4;
    /**
     * 设置
     */
    public static final int MORE_MENU_SETTING = 5;
    /**
     * 关于
     */
    public static final int MORE_MENU_ABOUT = 6;
    /**
     * 退出程序
     */
    public static final int MORE_MENU_LOGOUT = 7;

    //-----------------------------------------------------------------------
}
