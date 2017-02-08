package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.PPTBaseListAdapter;
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
 * 课件Fragment初始界面
 */
public class PPTBaseFragment extends Fragment {

    private View mView;
    private PPTBaseListAdapter adapter;
    private ListView listView;
    private LoadingDialog loadingDialog;

    public PPTBaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_pptbase, container, false);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();
        //findViews
        listView = (ListView) mView.findViewById(R.id.lv_frag_base_ppt);

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constants.DATA_PREPARED:
                        adapter = new PPTBaseListAdapter(mView.getContext());
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                CommonData.pptClassNo = CommonData.classIdList.get(position);
                                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main, new PPTFragment()).commit();
                            }
                        });
                        loadingDialog.dismiss();
                        break;
                }
            }
        };

        loadPreData(handler);
        return mView;
    }


    //预加载数据
    private void loadPreData(final Handler handler) {
        CommonData.classIdList = new ArrayList<>();
        CommonData.classNameList = new ArrayList<>();
        String userid = AVUser.getCurrentUser().getObjectId();
        String userroleCql = "select relatedid from userrole where userid='" + userid + "'";
        AVQuery.doCloudQueryInBackground(userroleCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                final String studentId = avCloudQueryResult.getResults().get(0).getString("relatedid");
                CommonData.studentId = studentId;
                final String studentClassCountCql = "select count(*) from studentclass where studentid='" + studentId + "'";
                AVQuery.doCloudQueryInBackground(studentClassCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
                    @Override
                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                        final int studentClassCount = avCloudQueryResult.getCount();
//                        String s = Integer.toString(studentClassCount);
//                        Toast.makeText(TeachentMainActivity.this,s,Toast.LENGTH_SHORT).show();
                        String studentClassCql = "select classid from studentclass where studentid='" + studentId + "'";
                        AVQuery.doCloudQueryInBackground(studentClassCql, new CloudQueryCallback<AVCloudQueryResult>() {
                            @Override
                            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                if (e == null) {
//                                    String s = Integer.toString(studentClassCount);
//                                    Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < studentClassCount; i++) {
                                        CommonData.classIdList.add(avCloudQueryResult.getResults().get(i).getString("classid"));
//                                        Toast.makeText(context,avCloudQueryResult.getResults().get(i).getString("classid"),Toast.LENGTH_SHORT).show();
                                    }
//                                    handler.sendEmptyMessage(Constants.DATA_PREPARED);
                                    String classCql = "select classname from class where classid in (select classid from studentclass where studentid='" + studentId + "')";
                                    AVQuery.doCloudQueryInBackground(classCql, new CloudQueryCallback<AVCloudQueryResult>() {
                                        @Override
                                        public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                            for (int i = 0; i < studentClassCount; i++) {
                                                CommonData.classNameList.add(avCloudQueryResult.getResults().get(i).getString("classname"));
                                            }
                                            handler.sendEmptyMessage(Constants.DATA_PREPARED);
                                        }
                                    });
                                } else {
                                    Log.e("error:studentclass", e.getMessage());
                                    Toast.makeText(mView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                });
            }
        });
    }


}
