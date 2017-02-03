package com.ant.nepu.teachent.common;

/**
 * 常量池
 * 统一管理App的常量信息
 * *********************
 * 对于App只读，对于开发者可读写
 * *********************
 * Created by wang1 on 2017/1/18.
 */

public class  Constants {


    /**
     * TeachentMainActivity更新UI
     */
    public static final int UPDATE_USERNAME = 0x1;
    public static final int UPDATE_USEREMAIL = 0x2;
    public static final int UPDATE_USERAVATAR = 0x3;

    /**
     * TeachentInitialActivity传递参数
     */
    public static final int REQUEST_TEXT_NO = 0x1;
    public static final int REQUEST_TEXT_NAME = 0x2;
    public static final int REQUEST_LIST_SCHOOL = 0x3;
    public static final int REQUEST_LIST_CLASS = 0x4;

    public static final int RESULT_NO = 0x1;
    public static final int RESULT_NAME = 0x2;
    public static final int RESULT_SCHOOL = 0x3;
    public static final int RESULT_CLASS = 0x4;

    public static String frag_contact_cv_name_title = "老师姓名：";
    public static String frag_contact_cv_tel_title = "联系电话：";


    public static String frag_leave_message_cv_text_mid_word = "说：";

}
