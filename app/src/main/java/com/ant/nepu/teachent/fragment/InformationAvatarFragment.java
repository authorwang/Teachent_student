package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.ant.nepu.teachent.util.UserInfoUtils;

/**
 * 我的信息
 * 头像信息Fragment
 */
public class InformationAvatarFragment extends Fragment {

    private View mView;
    private Button btn_back;
    private Button btn_select;
    private ImageView iv_main;
    private Handler handler;
    private LoadingDialog loadingDialog;

    public InformationAvatarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_information_avatar, container, false);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.UPDATE_USERAVATAR:
                        iv_main.setImageBitmap(CommonData.userAvatar);
                        loadingDialog.dismiss();
                        break;
                }
            }
        };

        //findViews
        btn_back = (Button) mView.findViewById(R.id.btn_frag_information_avatar_back);
        btn_select = (Button) mView.findViewById(R.id.btn_frag_information_avatar_select);
        iv_main = (ImageView) mView.findViewById(R.id.iv_frag_information_avatar_preview);

        //setOnClickListeners
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new InformationFragment()).commit();
            }
        });
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"选取照片",Toast.LENGTH_SHORT).show();
            }
        });

        loadPreData();
        return mView;
    }

    private void loadPreData() {
        UserInfoUtils.refreshAvatar(getContext(),handler);
    }

}
