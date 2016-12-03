package com.ant.nepu.teachent.fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * 考勤Fragment
 */
public class CheckInFragment extends Fragment {

    /**
     *    UI组件
     */
    PieChart mChart;
    View mView;

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

    public CheckInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mView=inflater.inflate(R.layout.fragment_check_in, container, false);
        mChart = (PieChart) mView.findViewById(R.id.fragment_check_in_chart_times);

        /**
         * 初始化数据
         */
        initData();
        /**
         * 设置PieChart
         */
        setPieChart();
        return mView;
    }





    /**
     * 初始化数据
     */
    private void initData() {
        /*
        载入学生信息
         */
        String userName = CommonData.userName;
        String nickName = CommonData.nickName;
        int typeAScore = CommonData.typeAScore;
        int typeBScore = CommonData.typeBScore;

        /*
        载入考勤数据
         */
        totalCheck = CommonData.stateBCheckIn;//总共需考勤数
        isChecked = CommonData.stateACheckIn;//实际已考勤次数


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

        entries.add(0,new PieEntry((float)((float)isChecked/totalCheck),getString(R.string.fragment_check_in_chart_yes_check)));
        entries.add(1,new PieEntry((float)((float)(totalCheck-isChecked)/totalCheck),getString(R.string.fragment_check_in_chart_no_check)));

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
