package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.PPTListAdapter;
import com.ant.nepu.teachent.common.CommonData;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPTFragment extends Fragment {
    /**
     *    UI组件
     */

    View mView;
    private ListView listView;
    private PPTListAdapter pptListAdapter;
    private Button btnDownload;
    private String result;//测试：download获取信息

    public PPTFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        result = "";//测试显示结果置空

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_ppt, container, false);

        //findView
        listView = (ListView) mView.findViewById(R.id.lv_frag_ppt);
        btnDownload = (Button) mView.findViewById(R.id.btn_frag_ppt_download);

        //setOnClickListener

        /**
         * 下载按钮点击监听事件
         */
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Integer,Boolean> isSelected = PPTListAdapter.isSelected;
                for(int i=0;i<pptListAdapter.getCount();i++){
                    if(isSelected.get(i)){
                        String text = (String) pptListAdapter.getItem(i);
                        result+="\n"+text;
                    }
                }
                Toast.makeText(mView.getContext(),result,Toast.LENGTH_SHORT).show();

            }


        });

        //加载listview数据
        loadData();
        return mView;
    }

    /**
     * 加载listview数据
     */
    private void loadData() {
       //测试：数据
        String[] texts = {"大学英语一级Chapter1",
                "大学英语一级Chapter2",
                "大学英语一级Chapter3",
                "大学英语一级Chapter4",
                "大学英语一级Chapter5",
                "大学英语二级Chapter1",
                "大学英语二级Chapter2",
                "大学英语二级Chapter3",
                "大学英语二级Chapter4",
                "高等数学（上）Chapter1",
                "高等数学（上）Chapter2",
                "高等数学（上）Chapter3",
                "高等数学（上）Chapter4",
                "高等数学（上）Chapter5",
                "高等数学（上）Chapter6"
        };
        CommonData.pptNameList = texts;

        pptListAdapter = new PPTListAdapter(mView.getContext(), CommonData.pptNameList);
        listView.setAdapter(pptListAdapter);

    }

}
