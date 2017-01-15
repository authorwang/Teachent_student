package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.HomeworkDetailListAdapter;
import com.ant.nepu.teachent.adapter.HomeworkListAdapter;
import com.ant.nepu.teachent.common.CommonData;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeworkDetailFragment extends Fragment {
    /**
     *    UI组件
     */

    View mView;
    private RecyclerView recyclerView;
    private Button mButton;
    private HomeworkDetailListAdapter homeworkDetailListAdapter;

    public HomeworkDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_homework_detail, container, false);

        //findViews
        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_fragment_homework_detail);
        mButton = (Button) mView.findViewById(R.id.btn_frag_homework_detail_close);

        /**
         * RecyclerView加载数据
         */
        loadData();

        //返回
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new HomeworkFragment()).commit();
            }
        });

        return mView;
    }

    /**
     * RecyclerView加载数据
     */
    private void loadData() {
        //测试数据
        String[] homeworkDetailList = {
                "题组一",
                "题组二",
                "题组三",
                "题组四",
                "题组五",
                "题组六",
                "题组七",
                "题组八",
                "题组九",
                "题组十",
                "题组十一",
                "题组十二",
        };
        CommonData.homeworkDetailList = homeworkDetailList;
        homeworkDetailListAdapter = new HomeworkDetailListAdapter(mView.getContext(),CommonData.homeworkDetailList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        recyclerView.setAdapter(homeworkDetailListAdapter);
    }

}
