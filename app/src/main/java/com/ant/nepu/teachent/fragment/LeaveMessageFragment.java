package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.LeaveMessageListAdapter;
import com.ant.nepu.teachent.common.CommonData;

/**
 *留言板Fragment
 *
 *
 */
public class LeaveMessageFragment extends Fragment {

    /**
     * UI组件
     */
    View mView;
    private RecyclerView recyclerView;
    private LeaveMessageListAdapter leaveMessageListAdapter;

    public LeaveMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       mView =  inflater.inflate(R.layout.fragment_leave_message, container, false);

        //findViews
        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_fragment_leave_message);

        /**
         * recyclerView加载数据
         */
        loadData();
        return mView;
    }

    /**
     * recyclerView加载数据
     */
    private void loadData() {
        //测试数据
        String[] leaveMessageNameList = {
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

        String[] leaveMessageSimpleTextList = {

                "有山无水难成景，有酒无朋难聚欢；曾经沧海...",
                "露水的晶莹是对你的眷恋，霜花的曼妙是对你...",
                "用微笑装作不在意你的嘲笑，不关心你的离去...",
                "孤单时，友谊是一柄利剑，助你披荆斩棘；迷...",
                "别把分手说得那么好听，再怎么好听心还是会...",
                "我很想告诉你。我好想你。可是我怕得到的只...",
                "没有经历过的人，没有对于他的意义。",
                "剪不断，理还乱，是离愁。",
                "妄想去留住原本应该消失的人和事，其实是一...",
                "因为不曾相识，所以也不曾悲伤。更不曾快...",
                "有时候，放弃一些东西才能得到一些什么，...",
                "你对我很好，很爱我，很呵护我，我谢谢你...",
        };

        String[] leaveMessageDetailTextList = {

                "有山无水难成景，有酒无朋难聚欢；曾经沧海成桑田，情意交心亘不变；红梅飘香话思念，惹落雪花两三片；大好时光多缠绵，愿你快乐每一天！",
                "露水的晶莹是对你的眷恋，霜花的曼妙是对你的期盼，飞雪的覆盖是欣慰一年的平安，亲爱的朋友，友谊的长久牵着流年，情意的甘甜陈酿青春的诺言，祝愿友谊地久天长。",
                "用微笑装作不在意你的嘲笑，不关心你的离去。",
                "孤单时，友谊是一柄利剑，助你披荆斩棘；迷茫时，朋友是一盏明灯，为你点亮心灵；疲乏时，祝福是一弯山泉，让你净化心灵。每天把好运装满，开心前行。",
                "别把分手说得那么好听，再怎么好听心还是会痛的。",
                "我很想告诉你。我好想你。可是我怕得到的只是一个简单的回答“哦”字。",
                "没有经历过的人，没有对于他的意义。",
                "剪不断，理还乱，是离愁。",
                "妄想去留住原本应该消失的人和事，其实是一种悲剧。",
                "因为不曾相识，所以也不曾悲伤。更不曾快乐过。",
                "有时候，放弃一些东西才能得到一些什么，得到了以后又想再得到放弃的那些。",
                "你对我很好，很爱我，很呵护我，我谢谢你。但不知道从什么时候开始，你就慢慢地不在乎我了，我到底做错什么了，以至于你不要我了？我可是你的，脸啊！",
        };

        CommonData.leaveMessageNameList = leaveMessageNameList;
        CommonData.leaveMessageSimpleTextList = leaveMessageSimpleTextList;
        CommonData.leaveMessageDetailTextList = leaveMessageDetailTextList;

        leaveMessageListAdapter = new LeaveMessageListAdapter(mView.getContext(),leaveMessageNameList,leaveMessageSimpleTextList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        recyclerView.setAdapter(leaveMessageListAdapter);


    }

}
