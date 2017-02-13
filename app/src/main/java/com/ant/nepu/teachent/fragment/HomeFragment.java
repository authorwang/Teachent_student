package com.ant.nepu.teachent.fragment;


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
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.ant.nepu.teachent.util.AVCloudUtils;
import com.ant.nepu.teachent.util.UserInfoUtils;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tencent.qc.stat.common.User;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 主页（考勤）Fragment
 */
public class HomeFragment extends Fragment {

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

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.fragment_home, container, false);
        AVCloudUtils.registerApp(mView.getContext());
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();

        // Inflate the layout for this fragment
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.UPDATE_USERAVATAR:
                        iv_avatar.setImageBitmap(CommonData.userAvatar);
                        break;
                    case Constants.UPDATE_USER_INFO:
                        updateUserInfo(this);
                        break;
                    case Constants.PRE_DATA_PREPARED:
                        loadFirstData(this);
                        break;
                    case Constants.DATA_PREPARED:
                        initSecondaryData();
                        //设置控件
                        setControls();
                        loadingDialog.dismiss();
                        break;
                }
            }
        };


        loadClassData(handler);

        //findViews
        mChart = (PieChart) mView.findViewById(R.id.fragment_check_in_chart_times);
        tv_greeting = (TextView) mView.findViewById(R.id.tv_frag_home_greeting);
        tv_username  = (TextView) mView.findViewById(R.id.tv_frag_home_nickname);
        iv_avatar = (ImageView) mView.findViewById(R.id.iv_frag_home_avatar);

        return mView;
    }


    private void updateUserInfo(final Handler handler) {
        UserInfoUtils.refreshAvatar(getContext(),handler);
        String _userCql = "select userrealname from _User where objectId='"+AVUser.getCurrentUser().getObjectId()+"'";
        AVQuery.doCloudQueryInBackground(_userCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if(e==null){
                    CommonData.userName = avCloudQueryResult.getResults().get(0).getString("userrealname");
                    handler.sendEmptyMessage(Constants.PRE_DATA_PREPARED);
                }
            }
        });
    }

    private void loadFirstData(final Handler handler){
        String studentclassCountCql = "select count(*) from studentclass where studentid=" +
                "(select relatedid from userrole where userid='"+AVUser.getCurrentUser().getObjectId()+"') " +
                "and " +
                "classid in " +
                "(select classid from studentclass where studentid=" +
                "(select relatedid from userrole where userid='"+AVUser.getCurrentUser().getObjectId()+"'))";
        AVQuery.doCloudQueryInBackground(studentclassCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                final int studentClassCount = avCloudQueryResult.getCount();
                String studentclassCql = "select studentcheck from studentclass where studentid=" +
                        "(select relatedid from userrole where userid='"+AVUser.getCurrentUser().getObjectId()+"') " +
                        "and " +
                        "classid in " +
                        "(select classid from studentclass where studentid=" +
                        "(select relatedid from userrole where userid='"+AVUser.getCurrentUser().getObjectId()+"'))";
                AVQuery.doCloudQueryInBackground(studentclassCql, new CloudQueryCallback<AVCloudQueryResult>() {
                    @Override
                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                        for(int i=0;i<studentClassCount;i++){
                            CommonData.stateACheckIn+=avCloudQueryResult.getResults().get(i).getInt("studentcheck");
                        }
                        String teacherclassCountCql = "select count(*) from teacherclass where classid in " +
                                "(select classid from studentclass where studentid=" +
                                "(select relatedid from userrole where userid='"+AVUser.getCurrentUser().getObjectId()+"') )";
                        AVQuery.doCloudQueryInBackground(teacherclassCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
                            @Override
                            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                final int teacherclassCount = avCloudQueryResult.getCount();
                                String teacherclassCql = "select teachercheck from teacherclass where classid in " +
                                        "(select classid from studentclass where studentid=" +
                                        "(select relatedid from userrole where userid='"+AVUser.getCurrentUser().getObjectId()+"') )";
                                AVQuery.doCloudQueryInBackground(teacherclassCql, new CloudQueryCallback<AVCloudQueryResult>() {
                                    @Override
                                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                        for(int i=0;i<teacherclassCount;i++){
                                            CommonData.stateBCheckIn+=avCloudQueryResult.getResults().get(i).getInt("teachercheck");
                                        }
                                        handler.sendEmptyMessage(Constants.DATA_PREPARED);
                                    }
                                });
                            }
                        });

                    }
                });
            }
        });
    }


    private void loadClassData(final Handler handler) {
        CommonData.classIdList = new ArrayList<>();
        CommonData.classNameList =new ArrayList<>();

        String userid = AVUser.getCurrentUser().getObjectId();
        String userroleCql = "select relatedid from userrole where userid='"+userid+"'";
        AVQuery.doCloudQueryInBackground(userroleCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                final String studentId = avCloudQueryResult.getResults().get(0).getString("relatedid");
                CommonData.studentId = studentId;
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
//                                    String s = Integer.toString(studentClassCount);
//                                    Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
                                    for(int i=0;i<studentClassCount;i++){
                                        CommonData.classIdList.add(avCloudQueryResult.getResults().get(i).getString("classid"));
//                                        Toast.makeText(context,avCloudQueryResult.getResults().get(i).getString("classid"),Toast.LENGTH_SHORT).show();
                                    }
                                    handler.sendEmptyMessage(Constants.UPDATE_USER_INFO);
                                }else{
                                    Log.e("error:studentclass",e.getMessage());
                                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 设置控件
     */
    private void setControls() {
        //设置Textview
        tv_greeting.setText(greeting_Text);
        tv_username.setText(CommonData.userName+"！");
        //设置pieChart
        setPieChart();
    }





    /**
     * 初始化二次数据
     */
    private void initSecondaryData() {
        //判断问候时间
        android.text.format.Time t = new android.text.format.Time();
        t.setToNow();
        int hour = t.hour;
        if((hour>=0 && hour<=6) || (hour>=18 && hour<=23)){//晚上
            greeting_Text = getString(R.string.fragment_check_in_greeting_evening);
        }else if(hour>6 && hour<12){//上午
            greeting_Text = getString(R.string.fragment_check_in_greeting_morning);
        }else if(hour>=12 && hour<18){
            greeting_Text = getString(R.string.fragment_check_in_greeting_afternoon);
        }

//        //载入学生信息
//        String userName = CommonData.userEmail;
//        String nickName = CommonData.userName;
//        int typeAScore = CommonData.userCreditA;
//        int typeBScore = CommonData.userCreditB;

//        String a = String.valueOf(CommonData.stateACheckIn);
//        String b = String.valueOf(CommonData.stateBCheckIn);
//        Toast.makeText(getContext(),a,Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(),b,Toast.LENGTH_SHORT).show();
        if(CommonData.stateBCheckIn == CommonData.stateACheckIn && CommonData.stateBCheckIn == 0){
            totalCheck=1;
            isChecked = 1;
        }else{
            totalCheck = CommonData.stateBCheckIn;//总共需考勤数
            isChecked = CommonData.stateACheckIn;//实际已考勤次数
        }



    }

    /**
     *设置考勤饼状统计图
     */
    private void setPieChart() {

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

//        mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

//        // add a selection listener
//        mChart.setOnChartValueSelectedListener(this);
        setData(4, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
//        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);
    }

    /**
     * 考勤饼状图填充数据
     * @param count
     * @param range
     */
    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (int i = 0; i < count ; i++) {
//            entries.add(i,new PieEntry(isChecked));
//            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), mChecks[i % mChecks.length]));
//        }

        entries.add(0,new PieEntry(((float)isChecked/totalCheck),getString(R.string.fragment_check_in_chart_yes_check)));
        entries.add(1,new PieEntry(((float)(totalCheck-isChecked)/totalCheck),getString(R.string.fragment_check_in_chart_no_check)));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
        colors.add(0,ColorTemplate.MATERIAL_COLORS[1]);//绿色
        colors.add(0,ColorTemplate.MATERIAL_COLORS[0]);//黄色


//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    /**
     * 考勤饼状图中心文字
     * @return
     */
    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("");
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 4, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 4, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 4, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 4, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 4, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 4, s.length(), 0);
        return s;
    }

}
