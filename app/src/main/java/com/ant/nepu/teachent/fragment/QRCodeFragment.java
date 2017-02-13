package com.ant.nepu.teachent.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.activity.TeachentMainActivity;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.ant.nepu.teachent.util.AVCloudUtils;
import com.ant.nepu.teachent.util.UserInfoUtils;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.SaveCallback;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRCodeFragment extends Fragment {
    /**
     *    UI组件
     */
    PieChart mChart;
    View mView;
    private ImageView iv_avatar;
    private TextView tv_username;
    private TextView tv_greeting;
    private String greeting_Text;
    private LoadingDialog loadingDialog;
    private static Handler handler;

    /**
     * PieChart数据
     */
//    private String[] mChecks = new String[] {
//            getString(R.string.fragment_check_in_chart_yes_check),//正常出勤
//            getString(R.string.fragment_check_in_chart_no_check)//未出勤
//
//    };
    private int totalCheck;//总数
    private int isChecked;//正常出勤数

    public QRCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.fragment_qrcode, container, false);

        doQRScan();

        return mView;
    }

    private void doQRScan() {
        startActivityForResult(new Intent(getContext(),CaptureActivity.class),0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (resultCode==-1){

                Bundle bundle=data.getExtras();
                String result= bundle.getString("result");
//                Toast.makeText(getContext(),"扫描结果是："+result,Toast.LENGTH_SHORT).show();
                updateCheckInData(result);

        }else{
            Toast.makeText(getContext(),"未获得相机调用权限或者取消了操作。",Toast.LENGTH_SHORT).show();
            getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new HomeFragment()).commit();
        }
    }

    //将扫描结果应用到数据库
    private void updateCheckInData(String result) {
        if(CommonData.qrCodeResult[0].equals("teachent") &&
                CommonData.qrCodeResult[1].equals("student")){
            CommonData.qrCodeResult[2] = result;
        }

        if(!CommonData.qrCodeResult[2].equals("")){
            String studentclassCountCql = "select count(*) from studentclass where classid='"+CommonData.qrCodeResult[2]+"' and studentid='"+CommonData.studentId+"'";
            AVQuery.doCloudQueryInBackground(studentclassCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
                @Override
                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                    if(avCloudQueryResult.getCount()==0){
                        Toast.makeText(getContext(),"二维码信息无效！",Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new HomeFragment()).commit();
                    }else{
                        String studentclassCql = "select objectId,studentcheck from studentclass where classid='"+CommonData.qrCodeResult[2]+"' and studentid='"+CommonData.studentId+"'";
                        AVQuery.doCloudQueryInBackground(studentclassCql, new CloudQueryCallback<AVCloudQueryResult>() {
                            @Override
                            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {

                                int studentCheck = avCloudQueryResult.getResults().get(0).getInt("studentcheck");
                                String objectId = avCloudQueryResult.getResults().get(0).getObjectId();
                                studentCheck++;
                                AVObject studentclass = AVObject.createWithoutData("studentclass",objectId);
                                studentclass.put("studentcheck",studentCheck);
                                studentclass.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        Toast.makeText(getContext(),"签到成功！",Toast.LENGTH_SHORT).show();
                                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new HomeFragment()).commit();
                                    }
                                });
                            }


                        });
                    }
                }
            });

        }
    }


}
