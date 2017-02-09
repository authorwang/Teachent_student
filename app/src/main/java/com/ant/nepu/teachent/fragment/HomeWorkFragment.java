package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.HomeworkListAdapter;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.DownloadingDialog;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.GetDataCallback;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 作业Fragment
 */
public class HomeworkFragment extends Fragment {
    /**
     * UI组件
     */

    View mView;
    ListView listView;
    HomeworkListAdapter adapter;
    Button btnBack;
    private LoadingDialog loadingDialog;

//    private HomeworkListAdapter homeworkListAdapter;

    public HomeworkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home_work, container, false);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constants.PRE_DATA_PREPARED:
                        loadPreData(this);
                        break;
                    case Constants.DATA_PREPARED:
                        adapter = new HomeworkListAdapter(mView.getContext());
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                    CommonData.homeworkPosition = position;
                                    CommonData.homeworkNo = CommonData.homeworkIdList.get(position);
                                    getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new HomeworkDetailFragment()).commit();
                            }
                        });

                        loadingDialog.dismiss();
                        break;
                }
            }
        };
        //findViews
        listView = (ListView) mView.findViewById(R.id.lv_frag_homework);
        btnBack = (Button) mView.findViewById(R.id.btn_frag_homework_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new HomeworkBaseFragment()).commit();
            }
        });
        //预加载数据
        loadPrePreData(handler);
        return mView;
    }

    /**
     * 起始预加载数据
     */
    private void loadPrePreData(final Handler handler) {
        CommonData.homeworkIdList = new ArrayList<>();
        CommonData.homeworkNameList = new ArrayList<>();

        final String classHomeworkCountCql = "select count(*) from classhw where classid='" + CommonData.homeworkClassNo + "'";
        AVQuery.doCloudQueryInBackground(classHomeworkCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                final int classHomeworkCount = avCloudQueryResult.getCount();
                if (classHomeworkCount == 0) {
                    loadingDialog.dismiss();
                    Toast.makeText(mView.getContext(), "无匹配数据！", Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction().replace(R.id.content_teachent_main, new HomeworkBaseFragment()).commit();
                }
                Log.d("classNo", CommonData.homeworkClassNo);
                String classHomeworkIdCql = "select hwid from classhw where classid='" + CommonData.homeworkClassNo + "'";
                AVQuery.doCloudQueryInBackground(classHomeworkIdCql, new CloudQueryCallback<AVCloudQueryResult>() {
                    @Override
                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                        for (int i = 0; i < classHomeworkCount; i++) {
                            CommonData.homeworkIdList.add(avCloudQueryResult.getResults().get(i).getString("hwid"));
                        }
                        handler.sendEmptyMessage(Constants.PRE_DATA_PREPARED);
                    }
                });
            }
        });
    }


    /**
     * 预加载数据
     */
    private LinkedList<String> sqlQueue = new LinkedList<>();
    private Handler xxHandler;
    private boolean taskRunning = false;


    private void startLoadData() {
        taskRunning = true;
        final String hwid = sqlQueue.poll();
        if (hwid == null) {
            taskRunning = false;
            return;
        }
//        Log.d("classNo",CommonData.pptClassNo);
        Log.d("hwid", hwid);
//        for(int i=0;i<100000000;i++){}
//        AVCloudUtils.registerApp(mView.getContext());
        String pptNameCql = "select hwtitle from homework where hid='" + hwid + "'";
        AVQuery.doCloudQueryInBackground(pptNameCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {

                CommonData.homeworkNameList.add(avCloudQueryResult.getResults().get(0).getString("hwtitle"));

                xxHandler.sendEmptyMessage(Constants.DATA_PREPARED);

                startLoadData();
            }
        });
    }

    private void loadPreData(Handler handler) {
        xxHandler = handler;
        for (final String hwid : CommonData.homeworkIdList) {
//            Log.d("pptid",pptid);
            sqlQueue.offer(hwid);
        }
        if (!taskRunning) {
            startLoadData();
        }
    }


}


