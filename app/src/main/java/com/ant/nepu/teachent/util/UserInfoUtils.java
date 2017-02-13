package com.ant.nepu.teachent.util;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.activity.TeachentMainActivity;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.GetDataCallback;

/**
 * 用户信息工具类
 * 将用户信息从服务器下载到CommonData
 * Created by WMS on 2017/2/6.
 */

public class UserInfoUtils {

    //刷新用户名
    public static boolean refreshUserName(){
        String userrealname = AVUser.getCurrentUser().getString("userrealname");
        if (userrealname==null) {//未设置用户名
            return false;
        } else {
            CommonData.userName = userrealname;
            return true;
        }
    }

    //刷新email
    public static boolean refreshEmail(){
        String email = AVUser.getCurrentUser().getUsername();
        CommonData.userEmail = email;
        if(email==null) return false;
        return true;
    }

    //刷新学校id
    public static boolean refreshSchoolId(){
        String schoolid = AVUser.getCurrentUser().getString("schoolid");
        if (schoolid == null) {
           return false;
        } else {
            CommonData.userSchoolId = schoolid;
        }
        return true;
    }

    //刷新积分A
    public static boolean refreshCreditA(){
        int userCreditA = AVUser.getCurrentUser().getInt("usercreditA");
        CommonData.userCreditA = userCreditA;
        if(userCreditA<0) return false;
        return true;
    }

    //刷新积分B
    public static boolean refreshCreditB(){
        int userCreditB = AVUser.getCurrentUser().getInt("usercreditB");
        CommonData.userCreditB = userCreditB;
        if(userCreditB<0) return false;
        return true;
    }

    //刷新头像
    public static boolean refreshAvatar(final Context context, final Handler handler){
        //CommonData.userRawAvatar = AVUser.getCurrentUser().getAVFile("useravatar");
        String userCql = "select include useravatar from _User where objectId='"+AVUser.getCurrentUser().getObjectId()+"'";
        AVQuery.doCloudQueryInBackground(userCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {

                    AVFile file = avCloudQueryResult.getResults().get(0).getAVFile("useravatar");
                    if(file==null){
                        CommonData.userAvatar = BitmapFactory.decodeResource(context.getResources(), R.mipmap.avatar_student_male);
                        handler.sendEmptyMessage(Constants.UPDATE_USERAVATAR);
                    }else{
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, AVException e) {
                                if(e==null){
                                    CommonData.userAvatar = ImageUtils.getPicFromBytes(bytes, null);
                                    handler.sendEmptyMessage(Constants.UPDATE_USERAVATAR);
                                }
                            }
                        });

                    }

            }
        });
        return true;
    }

    //刷新班级信息:
    public static boolean refreshClass(final Context context){
        String userid = AVUser.getCurrentUser().getObjectId();
        String userroleCql = "select relatedid from userrole where userid='"+userid+"'";
        AVQuery.doCloudQueryInBackground(userroleCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                final String studentId = avCloudQueryResult.getResults().get(0).getString("relatedid");
                final String studentClassCountCql = "select count(*) from studentclass where studentid='"+studentId+"'";
                AVQuery.doCloudQueryInBackground(studentClassCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
                    @Override
                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                        final int studentClassCount = avCloudQueryResult.getCount();
//                        String s = Integer.toString(studentClassCount);
//                        Toast.makeText(TeachentMainActivity.this,s,Toast.LENGTH_SHORT).show();
                        String studentClassCql = "select classid from studentclass where studentid='"+studentId+"'";
                        AVQuery.doCloudQueryInBackground(studentClassCql, new CloudQueryCallback<AVCloudQueryResult>() {
                            @Override
                            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                if(e==null){
                                    String s = Integer.toString(studentClassCount);
//                                    Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
                                    for(int i=0;i<studentClassCount;i++){
                                        CommonData.classIdList.add(avCloudQueryResult.getResults().get(i).getString("classid"));
//                                        Toast.makeText(TeachentMainActivity.this,avCloudQueryResult.getResults().get(i).getString("classid"),Toast.LENGTH_SHORT).show();
                                    }
//                                    handler.sendEmptyMessage(Constants.FRAGMENT_HOME);
                                }else{
                                    Log.e("error:studentclass",e.getMessage());
                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                });
            }
        });
        return true;
    }


}
