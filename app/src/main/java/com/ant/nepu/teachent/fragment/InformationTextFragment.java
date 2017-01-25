package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;

/**
 * 我的信息
 * 文本信息Fragment
 */
public class InformationTextFragment extends Fragment{


    private View mView;
    private TextView tv;
    private EditText et;
    private Button btn_save;
    private Button btn_cancel;
    String title;

    public InformationTextFragment() {
        // Required empty public constructor
    }

    /**
     * 给Fragment传递参数
     * @param title
     * @return
     */
    public static InformationTextFragment getInstance(String title){
        InformationTextFragment fragment = new InformationTextFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_information_text, container, false);

        //findViews
        tv = (TextView) mView.findViewById(R.id.tv_frag_information_text_title);
        et = (EditText) mView.findViewById(R.id.et_frag_information_text_content);
        btn_save = (Button) mView.findViewById(R.id.btn_frag_information_text_save);
        btn_cancel = (Button) mView.findViewById(R.id.btn_frag_information_text_cancel);

        //setArgs
        if(getArguments()!=null){
           title =  getArguments().getString("title");
            tv.setText(title);
        }

        //setOnClickListeners
        /**
         * 保存操作
         */
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"保存"+" from "+title+" ，内容是："+et.getText().toString(),Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new InformationFragment()).commit();
            }
        });

        /**
         * 取消操作
         */
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"取消"+" from "+title,Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new InformationFragment()).commit();
            }
        });
        return mView;
    }

}
