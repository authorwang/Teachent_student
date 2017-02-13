package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.InformationClassListAdapter;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 我的信息
 * 文本信息Fragment
 */
public class InformationTextFragment extends Fragment{


    private View mView;
    private TextView tv;
    private ListView listView;
    private Button btn_save;
    private Button btn_cancel;
    private String title;
    private static Handler handler;
    private static  Handler saveHandler;
    private InformationClassListAdapter adapter;
    private LoadingDialog loadingDialog;

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
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.DATA_PREPARED:
                        adapter = new InformationClassListAdapter(getContext(),listView);
                        listView.setAdapter(adapter);
                        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        btn_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadingDialog = new LoadingDialog(getContext());
                                loadingDialog.show();
                                saveHandler = new Handler(){
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        switch (msg.what){
                                            case Constants.DATA_PREPARED:
                                                loadingDialog.dismiss();
                                                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new InformationFragment()).commit();
                                                break;
                                        }
                                    }
                                };
                                saveClassInBackground();
                            }
                        });
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new InformationFragment()).commit();
                            }
                        });
                        loadingDialog.dismiss();
                        break;
                }
            }
        };
        //findViews
        tv = (TextView) mView.findViewById(R.id.tv_frag_information_text_title);
        listView = (ListView) mView.findViewById(R.id.lv_frag_information_text_content);
        btn_save = (Button) mView.findViewById(R.id.btn_frag_information_text_save);
        btn_cancel = (Button) mView.findViewById(R.id.btn_frag_information_text_cancel);

        //setArgs
        if(getArguments()!=null){
           title =  getArguments().getString("title");
            tv.setText(title);
        }

        loadPreData();

        return mView;
    }

    private void saveClassInBackground() {
        SparseBooleanArray checkedItemPositonList = listView.getCheckedItemPositions();
        for(int i=0;i<listView.getAdapter().getCount();i++){
            if(checkedItemPositonList.get(i)){
                Log.e("position",String.valueOf(i));
                View selectedView = listView.getChildAt(i);
                if(selectedView!=null){
                    TextView tv = (TextView) selectedView.findViewById(R.id.tv_frag_information_class_class_id);
                    String selectedClassId = tv.getText().toString();
                    CommonData.informationSelectedClassIdList.add(selectedClassId);
                }
            }
        }
        saveHandler.sendEmptyMessage(Constants.DATA_PREPARED);
        saveSecondaryData();
    }
    private LinkedList<String> sqlQueue = new LinkedList<>();
    private Handler xxHandler;
    private boolean taskRunning = false;


    private void startLoadData(){
        taskRunning = true;
        final String classId = sqlQueue.poll();
        if(classId==null){
            taskRunning = false;
            return;
        }
        AVObject studentclass = new AVObject("studentclass");
        studentclass.put("studentid",CommonData.studentId);
        studentclass.put("classid",classId);
        studentclass.put("studentcheck",0);
        studentclass.saveInBackground();
        startLoadData();

}

    private void saveSecondaryData() {
        xxHandler = saveHandler;
        for(final String classId:CommonData.informationSelectedClassIdList){
            Log.d("classId",classId);
            sqlQueue.offer(classId);
        }

        if(!taskRunning){
            startLoadData();
        }
    }

    //预加载班级信息
    private void loadPreData() {
        CommonData.classIdList = new ArrayList<>();
        CommonData.classNameList = new ArrayList<>();
        String classCountCql = "select count(*) from class";
        AVQuery.doCloudQueryInBackground(classCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if(e==null){
                    final int classCount = avCloudQueryResult.getCount();
                    String classCql = "select classid,classname from class";
                    AVQuery.doCloudQueryInBackground(classCql, new CloudQueryCallback<AVCloudQueryResult>() {
                        @Override
                        public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                            if(e==null){
                                for (int i=0;i<classCount;i++){
                                    CommonData.classIdList.add(avCloudQueryResult.getResults().get(i).getString("classid"));
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
