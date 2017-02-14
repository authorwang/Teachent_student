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
        String studentclassCountCql = "select count(*) from studentclass where studentid=" +
                "(select relatedid from userrole where userid='" + AVUser.getCurrentUser().getObjectId() + "')";
        AVQuery.doCloudQueryInBackground(studentclassCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if (e == null) {
                    final int studentClassCount = avCloudQueryResult.getCount();
                    String classCql = "select classid,classname from class where classid in " +
                            "(select classid from studentclass where studentid=" +
                            "(select relatedid from userrole where userid='" + AVUser.getCurrentUser().getObjectId() + "')) ";
                    AVQuery.doCloudQueryInBackground(classCql, new CloudQueryCallback<AVCloudQueryResult>() {
                        @Override
                        public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                            if (e == null) {
                                for(int i=0;i<studentClassCount;i++){
                                    CommonData.classIdList.add(avCloudQueryResult.getResults().get(i).getString("classid"));
                                }
                                for(int i=0;i<studentClassCount;i++){
                                    CommonData.classNameList.add(avCloudQueryResult.getResults().get(i).getString("classname"));
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
