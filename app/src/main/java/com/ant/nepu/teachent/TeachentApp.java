package com.ant.nepu.teachent;

import android.app.Application;

import com.ant.nepu.teachent.util.Utils;
import com.avos.avoscloud.AVOSCloud;

/**
 * 程序Application入口
 * 进行相关组件的初始化
 */
public class TeachentApp extends Application {
    public static boolean debug = true;
    public static TeachentApp ctx;

    @Override
    public void onCreate() {
        super.onCreate();
//        ctx = this;
//        Utils.fixAsyncTaskBug();



//        LeanchatUser.alwaysUseSubUserClass(LeanchatUser.class);
//
//        AVObject.registerSubclass(AddRequest.class);
//        AVObject.registerSubclass(UpdateInfo.class);
//
//        // 节省流量
//        AVOSCloud.setLastModifyEnabled(true);
//
//        AVIMMessageManager.registerAVIMMessageType(LCIMRedPacketMessage.class);
//        AVIMMessageManager.registerAVIMMessageType(LCIMRedPacketAckMessage.class);
//        LCChatKit.getInstance().setProfileProvider(new LeanchatUserProvider());
//        LCChatKit.getInstance().init(this, appId, appKey);


//        // 初始化红包操作
//        RedPacket.getInstance().initContext(ctx, RPConstant.AUTH_METHOD_SIGN);
//        //控制红包SDK中Log输出
//        RedPacket.getInstance().setDebugMode(false);
//
//        PushManager.getInstance().init(ctx);
//        AVOSCloud.setDebugLogEnabled(debug);
//        AVAnalytics.enableCrashReport(this, !debug);
//        initBaiduMap();
//        if (App.debug) {
//            openStrictMode();
//        }
    }

//    public void openStrictMode() {
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()   // or .detectAll() for all detectable problems
//                .penaltyLog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                //.penaltyDeath()
//                .build());
//    }

//    private void initBaiduMap() {
//        SDKInitializer.initialize(this);
//    }
}


