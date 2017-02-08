package com.ant.nepu.teachent.util;

import android.content.Context;

import com.avos.avoscloud.AVOSCloud;

/**
 * LeanCloud后端云工具类
 * Created by WMS on 2017/2/8.
 */

public class AVCloudUtils {
    public static void registerApp(Context context){
        String appId = "RzID2Ekq4Ys2noDDUfQ0bM4T-gzGzoHsz";
        String appKey = "BC8eOnrXGpDK3ITcHmLALGOR";

        //初始化LeanCloud
        AVOSCloud.initialize(context,appId,appKey);

    }
}
