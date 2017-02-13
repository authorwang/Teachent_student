package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.LeaveMessageListAdapter;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;

import java.util.ArrayList;

/**
 * 留言板Fragment
 */
public class LeaveMessageFragment extends Fragment {

    /**
     * UI组件
     */
    View mView;
    private RecyclerView recyclerView;
    private LeaveMessageListAdapter leaveMessageListAdapter;
    private LoadingDialog loadingDialog;
    private FloatingActionButton fab_New;

    public LeaveMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_leave_message, container, false);
        loadingDialog = new LoadingDialog(mView.getContext());
        loadingDialog.show();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constants.DATA_PREPARED:
                        leaveMessageListAdapter = new LeaveMessageListAdapter(mView.getContext(), LeaveMessageFragment.this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
                        recyclerView.setAdapter(leaveMessageListAdapter);
                        fab_New.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(mView.getContext(),"新留言",Toast.LENGTH_SHORT).show();
                                    newLeaveMessage();
                            }
                        });
                        loadingDialog.dismiss();
                        break;
                }
            }
        };
        //findViews
        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_fragment_leave_message);
        fab_New = (FloatingActionButton) mView.findViewById(R.id.fab_frag_leave_message_new);

        /**
         * recyclerView加载数据
         */
        loadPreData(handler);
        return mView;
    }

    /**
     * 新留言
     */
    private void newLeaveMessage() {
getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new LeaveMessageNewFragment()).commit();
    }

    /**
     * recyclerView加载数据
     */
    private void loadPreData(final Handler handler) {
        CommonData.leaveMessageNameList = new ArrayList<>();
        CommonData.leaveMessageContentList = new ArrayList<>();
       String leaveMessageCountCql = "select count(*) from leavemessage";
        AVQuery.doCloudQueryInBackground(leaveMessageCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if(e==null){
                    final int leaveMessageCount = avCloudQueryResult.getCount();
                    String leaveMessageCql = "select username,messagecontent from leavemessage";
                    AVQuery.doCloudQueryInBackground(leaveMessageCql, new CloudQueryCallback<AVCloudQueryResult>() {
                        @Override
                        public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                            if(e==null){
                                for(int i=0;i<leaveMessageCount;i++){
                                    CommonData.leaveMessageNameList.add(avCloudQueryResult.getResults().get(i).getString("username"));
                                    CommonData.leaveMessageContentList.add(avCloudQueryResult.getResults().get(i).getString("messagecontent"));
                                }
                                handler.sendEmptyMessage(Constants.DATA_PREPARED);
                            }
                        }
                    });
                }

            }
        });


    }

}
