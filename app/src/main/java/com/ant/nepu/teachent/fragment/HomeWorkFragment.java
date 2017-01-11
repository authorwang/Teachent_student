package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.HomeworkListAdapter;
import com.ant.nepu.teachent.common.CommonData;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeworkFragment extends Fragment {
    /**
     *    UI组件
     */

    View mView;
    private RecyclerView recyclerView;
    private HomeworkListAdapter homeworkListAdapter;

    public HomeworkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_home_work, container, false);

        //findViews
        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_fragment_homework);

        /**
         * RecyclerView加载数据
         */
        loadData();

        return mView;
    }

    /**
     * RecyclerView加载数据
     */
    private void loadData() {
        //测试数据
        String[] homeworkList = {
                "高等数学 习题一",
                "高等数学 习题二",
                "高等数学 习题三",
                "高等数学 习题四",
                "高等数学 习题五",
                "高等数学 习题六",
                "高等数学 习题七",
                "高等数学 习题八",
                "高等数学 习题九",
                "高等数学 习题十",
                "高等数学 习题十一",
                "高等数学 习题十二",
        };
        CommonData.homeworkList = homeworkList;
        homeworkListAdapter = new HomeworkListAdapter(mView.getContext(),CommonData.homeworkList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        recyclerView.setAdapter(homeworkListAdapter);
    }

}
