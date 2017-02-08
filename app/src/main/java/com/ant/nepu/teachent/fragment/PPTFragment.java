package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.PPTListAdapter;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.DownloadingDialog;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.ant.nepu.teachent.util.AVCloudUtils;
import com.ant.nepu.teachent.util.FileUtils;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.okio.ByteString;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static com.ant.nepu.teachent.common.Constants.DATA_PREPARED;

/**
 * 课件Fragment
 */
public class PPTFragment extends Fragment {
    /**
     *    UI组件
     */

    View mView;
    private ListView listView;
    private PPTListAdapter pptListAdapter;
    private Button btnBack;
    private LoadingDialog loadingDialog;

    public PPTFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_ppt, container, false);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();
        Handler handler = new Handler(){
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.PRE_DATA_PREPARED:
                        loadPreData(this);
                        break;
                    case Constants.DATA_PREPARED:
                        pptListAdapter = new PPTListAdapter(mView.getContext());
                        listView.setAdapter(pptListAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                final DownloadingDialog downloadingDialog = new DownloadingDialog(mView.getContext());
                                downloadingDialog.show();
                                String targetPPTId = CommonData.pptIdList.get(position);
                                String pptFileCql = "select include pptfile from ppt where pid='"+targetPPTId+"'";
                                AVQuery.doCloudQueryInBackground(pptFileCql, new CloudQueryCallback<AVCloudQueryResult>() {
                                    @Override
                                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                        if(e==null){
                                            AVFile file = avCloudQueryResult.getResults().get(0).getAVFile("pptfile");
                                            file.getDataInBackground(new GetDataCallback() {
                                                @Override
                                                public void done(byte[] bytes, AVException e) {
                                                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+
                                                            "/"+"TeachentFiles/PPTFiles/"
                                                            +CommonData.pptNameList.get(position)+".ppt";

                                                    String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                                            File.separator + "TeachentFiles" +
                                                            File.separator+"PPTFiles";
                                                    File directory = new File(fileName);
                                                    directory.mkdirs();

                                                    File file = FileUtils.getFileFromBytes(bytes,path);
                                                    try {
                                                   file.createNewFile();
                                                    } catch (IOException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                }
                                            }, new ProgressCallback() {
                                                @Override
                                                public void done(Integer integer) {
                                                    if(integer==100){
                                                        downloadingDialog.dismiss();
                                                        Toast.makeText(mView.getContext(),"下载完成！",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                        loadingDialog.dismiss();
                        break;
                }
            }
        };
        //findView
        listView = (ListView) mView.findViewById(R.id.lv_frag_ppt);
        btnBack = (Button) mView.findViewById(R.id.btn_frag_ppt_back);

        //setOnClickListener

        /**
         * 下载按钮点击监听事件
         */
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new PPTBaseFragment()).commit();
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
        CommonData.pptIdList = new ArrayList<>();
        CommonData.pptNameList = new ArrayList<>();

        final String classPPTCountCql = "select count(*) from classppt where classid='"+CommonData.pptClassNo+"'";
        AVQuery.doCloudQueryInBackground(classPPTCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                final int classPPTCount = avCloudQueryResult.getCount();
                if(classPPTCount==0){
                    loadingDialog.dismiss();
                    Toast.makeText(mView.getContext(),"无匹配数据！",Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new PPTBaseFragment()).commit();
                }
                Log.d("classNo",CommonData.pptClassNo);
                String classPPTIdCql = "select pptid from classppt where classid='"+CommonData.pptClassNo+"'";
                AVQuery.doCloudQueryInBackground(classPPTIdCql, new CloudQueryCallback<AVCloudQueryResult>() {
                    @Override
                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                        for(int i=0;i<classPPTCount;i++){
                            CommonData.pptIdList.add(avCloudQueryResult.getResults().get(i).getString("pptid"));
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


    private void startLoadData(){
        taskRunning = true;
        final String pptid = sqlQueue.poll();
        if(pptid==null){
            taskRunning = false;
            return;
        }
//        Log.d("classNo",CommonData.pptClassNo);
        Log.d("pptid",pptid);
//        for(int i=0;i<100000000;i++){}
//        AVCloudUtils.registerApp(mView.getContext());
        String pptNameCql = "select ppttitle from ppt where pid='"+pptid+"'";
        AVQuery.doCloudQueryInBackground(pptNameCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {

                CommonData.pptNameList.add(avCloudQueryResult.getResults().get(0).getString("ppttitle"));

                xxHandler.sendEmptyMessage(Constants.DATA_PREPARED);

                startLoadData();
            }
        });
    }
    private void loadPreData(Handler handler) {
        xxHandler = handler;
        for(final String pptid:CommonData.pptIdList){
//            Log.d("pptid",pptid);
            sqlQueue.offer(pptid);
        }
        if(!taskRunning){
            startLoadData();
        }
    }

}
