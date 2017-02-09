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
     * 全局数据更新
     */
    public static final int DATA_PREPARED = 0x1;
    public static final int PRE_DATA_PREPARED = 0x2;



    /**
     * TeachentMainActivity更新UI
     */
    public static final int UPDATE_USERNAME = 0x1;
    public static final int UPDATE_USEREMAIL = 0x2;
    public static final int UPDATE_USERAVATAR = 0x3;
    public static final int FRAGMENT_HOME = 0x4;

    /**
     * TeachentMainActivity 导航Fragment
     */
    public static final int FRAGMENT_CHECK_IN = 0x1;
    public static final int FRAGMENT_PPT = 0x2;
    public static final int FRAGMENT_HOMEWORK = 0x3;
    public static final int FRAGMENT_CONTACT = 0x4;
    public static final int FRAGMENT_LEAVE_MESSAGE = 0x5;
    public static final int FRAGMENT_INFORMATION = 0x7;
    public static final int FRAGMENT_ABOUT= 0x8;
    public static final int FRAGMENT_LOG_OUT= 0x9;

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

    /**
     * HomeworkDetailFragment 参数
     */
    public static final int RESULT_CANCELED = 0x1;
    public static final int IMAGE_REQUEST_CODE = 0x2;
    public static final int CAMERA_REQUEST_CODE = 0x3;
    public static final int RESULT_REQUEST_CODE = 0x4;
    public static final int ANSWER_SUBMITTED = 0x5;
}
