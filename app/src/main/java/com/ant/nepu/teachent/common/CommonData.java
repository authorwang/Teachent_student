package com.ant.nepu.teachent.common;

import android.graphics.Bitmap;

import com.avos.avoscloud.AVFile;

import java.util.ArrayList;

/**
 * 公共数据
 * *********************************************
 * 需要在登录成功后加载（TeachentMainActivity加载时）
 * *********************************************
 * 包括用户信息等公有信息
 * Created by WMS on 2016/12/3.
 */

public class CommonData {

    public static String[] initialSchoolId;
    public static String[] initialSchoolName;
    public static String[] initialClassId;
    public static String[] initialClassName;
    public static String initialSelectedNo = "";
    public static String initialSelectedName = "";
    public static String initialSelectedSchoolId = "";
    public static String initialSelectedClassId = "";


    //是否初始化标记
    public static boolean isInitial;
    //头像
    public static AVFile userRawAvatar;
    public static Bitmap userAvatar;
    //用户名
    public static String userName;
    //Email
    public static String userEmail;
    //学校代号
    public static String userSchoolId;
    //A类积分
    public static int userCreditA;
    //B类积分
    public static int userCreditB;
    //A类考勤数:已考勤次数
    public static int stateACheckIn = 0;
    //B类考勤数:总共需考勤次数
    public static  int stateBCheckIn = 0;


    //学生id
    public static String studentId;
    //班级id列表
    public static ArrayList<String> classIdList = new ArrayList<>();
    //班级名称列表
    public static ArrayList<String> classNameList = new ArrayList<>();



    //课件名称列表
    public static String[] pptNameList;
    //课件班级编号
    public static String  pptClassNo;

    //作业名称列表
    public static String[] homeworkList;
    //作业详情题组列表
    public static String[] homeworkDetailList;
    //作业项目对应位置
    public static int homeworkPosition;

    //联系老师姓名列表
    public static String[] contactNameList;
    //联系老师电话列表
    public static String[] contactTelList;

    //留言板姓名列表
    public static String[] leaveMessageNameList;
    //留言板简略文本列表
    public static String[] leaveMessageSimpleTextList;
    //留言板详情文本列表
    public static String[] leaveMessageDetailTextList;
    //留言板项目对应位置
    public static int leaveMessagePosition;

}
