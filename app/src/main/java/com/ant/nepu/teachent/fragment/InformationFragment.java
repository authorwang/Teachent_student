package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.InformationListAdapter;

/**
 * 我的信息Fragment
 */
public class InformationFragment extends Fragment {


    private View mView;
    private ListView listView;
    private InformationListAdapter adapter;
    private Fragment fragment;

    public InformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_information, container, false);

        //findViews
        listView = (ListView) mView.findViewById(R.id.lv_frag_information);

        /**
         * 加载数据
         */
        loadData();

        return mView;
    }

    /**
     * 加载数据
     */
    private void loadData() {
        //配置数据
        String[] titles ={
                "头像",
                "学号",
                "姓名",
                "学校",
                "班级"
        };
        //测试数据
        String[] texts = {
                "",
                "",
                "张三",
                "东油",
                "A25"
        };

        adapter = new InformationListAdapter(mView.getContext(),titles,texts);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){//对应上述配置数据
                    case 0://头像
                        fragment = new InformationAvatarFragment();
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                        break;
                    case 1://学号
                         fragment = InformationTextFragment.getInstance("学号");
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                        break;
                    case 2://姓名
                        fragment = InformationTextFragment.getInstance("姓名");
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                        break;
                    case 3://学校
                        fragment = InformationTextFragment.getInstance("学校");
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                        break;
                    case 4://班级
                        fragment = InformationTextFragment.getInstance("班级");
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                        break;
                }

            }
        });
    }

}