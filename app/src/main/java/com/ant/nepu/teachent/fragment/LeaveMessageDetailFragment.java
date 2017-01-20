package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ant.nepu.teachent.R;

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
        return inflater.inflate(R.layout.fragment_leave_message_detail, container, false);
    }

}
