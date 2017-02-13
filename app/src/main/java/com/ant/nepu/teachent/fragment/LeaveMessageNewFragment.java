package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;

/**
 * 新留言Fragment
 */
public class LeaveMessageNewFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private TextView tv_title;
    private Button btn_save;
    private Button btn_cancel;
    private EditText et_content;
    private Handler handler;
    private Handler mainHandler;

    public LeaveMessageNewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_leave_message_new, container, false);

        mainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.SAVE_READY:
                        Toast.makeText(getContext(),"留言成功！",Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new LeaveMessageFragment()).commit();
                        break;
                }
            }
        };

        //findViews
        btn_save = (Button) mView.findViewById(R.id.btn_frag_leave_message_new_text_save);
        btn_cancel = (Button) mView.findViewById(R.id.btn_frag_leave_message_new_text_cancel);
        et_content = (EditText) mView.findViewById(R.id.et_frag_leave_message_new_text_content);
        tv_title = (TextView) mView.findViewById(R.id.tv_frag_leave_message_new_text_title);
        tv_title.setText("新留言");

        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        return mView;
    }


    @Override
    public void onClick(View v) {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.SAVE_READY:
                        String content = et_content.getText().toString();
                        //isEmpty
                        if(content.equals("")){
                            Toast.makeText(getContext(),"留言内容不能为空！",Toast.LENGTH_SHORT).show();
                        }else{
                            AVObject leaveMessage = new AVObject("leavemessage");
                            leaveMessage.put("userid",AVUser.getCurrentUser().getObjectId());
                            leaveMessage.put("username",CommonData.userName);
                            leaveMessage.put("messagecontent",content);
                            leaveMessage.saveInBackground();
                            mainHandler.sendEmptyMessage(Constants.SAVE_READY);
                        }
                        break;
                }
            }
        };
        switch (v.getId()){
            case R.id.btn_frag_leave_message_new_text_save:
                loadPreData();
                handler.sendEmptyMessage(Constants.SAVE_READY);
                break;
            case R.id.btn_frag_leave_message_new_text_cancel:
                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new LeaveMessageFragment()).commit();
                break;
        }
    }

    /**
     * 预加载数据
     */
    private void loadPreData() {
        String _userCql = "select userrealname from _User where objectId='"+ AVUser.getCurrentUser().getObjectId()+"'";
        AVQuery.doCloudQueryInBackground(_userCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if(e==null){
                    CommonData.userName = avCloudQueryResult.getResults().get(0).getString("userrealname");
                }
            }
        });
    }
}
