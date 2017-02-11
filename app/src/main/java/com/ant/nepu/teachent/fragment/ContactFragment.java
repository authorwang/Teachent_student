package com.ant.nepu.teachent.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.ContactAdapter;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;

import java.util.ArrayList;

/**
 * 联系教师Fragment
 */
public class ContactFragment extends Fragment {


    private View mView;
    private RecyclerView rv;
    private ContactAdapter contactAdapter;
    private LoadingDialog loadingDialog;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_contact, container, false);
        loadingDialog = new LoadingDialog(mView.getContext());
        loadingDialog.show();
        //findViews
        rv = (RecyclerView) mView.findViewById(R.id.rv_fragment_contact);



        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.DATA_PREPARED:
                        contactAdapter = new ContactAdapter(mView.getContext());
                        rv.setLayoutManager(new LinearLayoutManager(mView.getContext()));
                        rv.setAdapter(contactAdapter);
                        loadingDialog.dismiss();
                        break;
                }

            }
        };

        /**
         * RecyclerView加载数据
         */
        loadData(handler);
        return mView;
    }

    /**
     * RecyclerView加载数据
     */
    private void loadData(final Handler handler) {
        CommonData.contactTeacherNameList = new ArrayList<>();
        CommonData.contactTeacherTelList = new ArrayList<>();
       String teacherCountCql = "select count(*) from teacher where teacherid in" +
               "(select teacherid from teacherclass where classid in" +
               "(select classid from studentclass where studentid=(" +
               "select relatedid from userrole where rolename='student' and userid='"+ AVUser.getCurrentUser().getObjectId()+"')))";
        AVQuery.doCloudQueryInBackground(teacherCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if(e==null){
                    final int teacherCount = avCloudQueryResult.getCount();
                    String teacherNameCql = "select teachername from teacher where teacherid in" +
                            "(select teacherid from teacherclass where classid in" +
                            "(select classid from studentclass where studentid=(" +
                            "select relatedid from userrole where rolename='student' and userid='"+ AVUser.getCurrentUser().getObjectId()+"')))";
                    AVQuery.doCloudQueryInBackground(teacherNameCql, new CloudQueryCallback<AVCloudQueryResult>() {
                        @Override
                        public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                            if(e==null){
                                for(int i=0;i<teacherCount;i++){
                                    CommonData.contactTeacherNameList.add(avCloudQueryResult.getResults().get(i).getString("teachername"));
                                }
                                String teacherTelCql = "select teachertel from teacher where teacherid in" +
                                        "(select teacherid from teacherclass where classid in" +
                                        "(select classid from studentclass where studentid=(" +
                                        "select relatedid from userrole where rolename='student' and userid='"+ AVUser.getCurrentUser().getObjectId()+"')))";
                                AVQuery.doCloudQueryInBackground(teacherTelCql, new CloudQueryCallback<AVCloudQueryResult>() {
                                    @Override
                                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                        if(e==null){
                                            for(int i=0;i<teacherCount;i++){
                                                CommonData.contactTeacherTelList.add(avCloudQueryResult.getResults().get(i).getString("teachertel"));
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
        });


    }

}
