package com.ant.nepu.teachent.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.util.AVCloudUtils;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;

/**
 * 闪屏页
 * 常量：SPLASH_DURATION 闪屏页持续时间
 * GO_MAIN_MSG 主界面
 * GO_LOGIN_MSG 登录界面
 *
 */
public class TeachentSplashActivity extends Activity {

    public static final int SPLASH_DURATION = 2000;
    private static final int GO_MAIN_MSG = 0x1;
    private static final int GO_LOGIN_MSG = 0x2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachent_splash);

        AVCloudUtils.registerApp(this);
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);
//        if (LeanchatUser.getCurrentUser() != null) {
//            LeanchatUser.getCurrentUser().updateUserInfo();
//            handler.sendEmptyMessageDelayed(GO_MAIN_MSG, SPLASH_DURATION);
//        } else {
//            handler.sendEmptyMessageDelayed(GO_LOGIN_MSG, SPLASH_DURATION);
//        }

        /**
         * 判断用户登录状态
         */
        boolean isLogin = false;
        if(AVUser.getCurrentUser()!=null){
            isLogin = true;
        }
        //相关判断代码
        if(isLogin){//已登录
            handler.sendEmptyMessageDelayed(GO_MAIN_MSG,SPLASH_DURATION);
        }else{//未登录
            handler.sendEmptyMessageDelayed(GO_LOGIN_MSG,SPLASH_DURATION);
        }
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case GO_MAIN_MSG://已登录
                    Intent intent1 = new Intent(TeachentSplashActivity.this,TeachentMainActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
                case GO_LOGIN_MSG://未登录
                    Intent intent2 = new Intent(TeachentSplashActivity.this,TeachentLoginActivity.class);
                    startActivity(intent2);
                    finish();
            }
        }
    };

}
