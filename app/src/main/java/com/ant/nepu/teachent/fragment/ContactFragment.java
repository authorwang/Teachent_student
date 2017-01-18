package com.ant.nepu.teachent.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.ContactAdapter;
import com.ant.nepu.teachent.common.CommonData;

/**
 * 联系教师Fragment
 */
public class ContactFragment extends Fragment {


    private View mView;
    private RecyclerView rv;
    private ContactAdapter contactAdapter;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_contact, container, false);

        //findViews
        rv = (RecyclerView) mView.findViewById(R.id.rv_fragment_contact);

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
        String[] contactNameList = {
                "张三",
                "李四",
                "王五",
                "赵六",
                "孙七",
                "钱八",
                "周九",
                "吴十",
                "冯一",
                "陈二",
                "卫三",
                "沈四",
        };

        String[] contactTelList = {
                "13902070000",
                "13902070001",
                "13902070002",
                "13902070003",
                "13902070004",
                "13902070005",
                "13902070006",
                "13902070007",
                "13902070008",
                "13902070009",
                "13902070010",
                "13902070011",
        };

        CommonData.contactNameList = contactNameList;
        CommonData.contactTelList = contactTelList;

        contactAdapter = new ContactAdapter(mView.getContext(),CommonData.contactNameList,CommonData.contactTelList);
        rv.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        rv.setAdapter(contactAdapter);
    }

}
