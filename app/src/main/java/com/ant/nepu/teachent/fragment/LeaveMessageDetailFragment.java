package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;

import org.w3c.dom.Text;

/**
 * 留言详情Fragment
 *
 */
public class LeaveMessageDetailFragment extends Fragment {

    /**
     * UI组件
     */
    View mView;
    Button btn_back;
    Button btn_comment;
    TextView tv_text;
    TextView tv_comment;

    public LeaveMessageDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_leave_message_detail, container, false);

        //findViews
        btn_back = (Button) mView.findViewById(R.id.btn_frag_leave_message_detail_close);
        btn_comment = (Button) mView.findViewById(R.id.btn_frag_leave_message_detail_comment);
        tv_text = (TextView) mView.findViewById(R.id.tv_frag_leave_message_detail_text);
        tv_comment = (TextView) mView.findViewById(R.id.tv_frag_leave_message_detail_comment);

        //加载数据
        loadData();

        //onClickListeners
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new LeaveMessageFragment()).commit();
            }
        });

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"评论提交",Toast.LENGTH_SHORT).show();
            }
        });
        return mView;
    }

    /**
     * 加载留言板详情
     */
    private void loadData() {
//        tv_text.setText(CommonData.leaveMessageDetailTextList[CommonData.leaveMessagePosition]);
    }

}
