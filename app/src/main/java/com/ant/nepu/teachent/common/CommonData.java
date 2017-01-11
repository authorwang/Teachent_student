package com.ant.nepu.teachent.common;

/**
 * 公共数据
 * *********************************************
 * 需要在登录成功后加载（TeachentMainActivity加载时）
 * *********************************************
 * 包括用户信息等公有信息
 * Created by WMS on 2016/12/3.
 */

public class CommonData {
    //用户名
    public static String userName;
    //昵称
    public static String nickName;
    //A类积分
    public static int typeAScore;
    //B类积分
    public static int typeBScore;
    //A类考勤数:已考勤次数
    public static int stateACheckIn;
    //B类考勤数:总共需考勤次数
    public static  int stateBCheckIn;
    //课件名称列表
    public static String[] pptNameList;
    //作业名称列表
    public static String[] homeworkList;
}
