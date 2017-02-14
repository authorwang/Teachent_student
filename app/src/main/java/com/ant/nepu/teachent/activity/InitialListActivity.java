package com.ant.nepu.teachent.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.InitialListListAdapter;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.ant.nepu.teachent.util.AVCloudUtils;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;

public class InitialListActivity extends AppCompatActivity {

    private ListView listView;
    private InitialListListAdapter adapter;
    private Button btn_cancel;
    private TextView tv_title;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_list);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        AVCloudUtils.registerApp(this);
        listView = (ListView) findViewById(R.id.lv_activity_initial_items);
        btn_cancel = (Button) findViewById(R.id.btn_activity_initial_list_cancel);
        tv_title = (TextView) findViewById(R.id.tv_activity_initial_list_title);

        //改变标题和内容
        Intent intent  = getIntent();
        String title = intent.getStringExtra("title");
        tv_title.setText(title);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitialListActivity.this.finish();
            }
        });

        if(title.equals("学校")){
            loadSchoolData();
        }else if(title.equals("班级")){
            loadClassData();
        }

    }

    /**
     * 加载班级数据
     */
    private void loadClassData() {
        String cqlCount = "select count(*) from class";
        AVQuery.doCloudQueryInBackground(cqlCount, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                final int count = avCloudQueryResult.getCount();
                String cqlSchools = "select * from class";
                AVQuery.doCloudQueryInBackground(cqlSchools, new CloudQueryCallback<AVCloudQueryResult>() {
                    @Override
                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                        if(e==null){
                            CommonData.initialClassId = new String[count];
                            CommonData.initialClassName = new String[count];
                            for(int i=0;i<count;i++){
                                CommonData.initialClassId[i] = avCloudQueryResult.getResults().get(i).getString("classid");
                                CommonData.initialClassName[i] = avCloudQueryResult.getResults().get(i).getString("classname");
                            }
                            adapter = new InitialListListAdapter(InitialListActivity.this,CommonData.initialClassId,CommonData.initialClassName);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Log.e("position",String.valueOf(position));
                                    View itemView = listView.getChildAt(position-listView.getFirstVisiblePosition());
                                    TextView tv_id = (TextView) itemView.findViewById(R.id.tv_activity_initial_list_id);
                                    TextView tv_name = (TextView) itemView.findViewById(R.id.tv_activity_initial_list_name);
                                    if(tv_title.getText().toString().equals("学校")){
                                        CommonData.initialSelectedSchoolId = tv_id.getText().toString();
                                        Intent intent = new Intent();
                                        intent.putExtra("result",tv_name.getText().toString());
                                        setResult(Constants.RESULT_SCHOOL,intent);
                                    }else if(tv_title.getText().toString().equals("班级")){
                                        CommonData.initialSelectedClassId = tv_id.getText().toString();
                                        Intent intent = new Intent();
                                        intent.putExtra("result",tv_name.getText().toString());
                                        setResult(Constants.RESULT_CLASS,intent);
                                    }
                                    finish();

                                }
                            });
                            loadingDialog.dismiss();

                        }
                        else{
                            Log.e("error getSchool:",e.getMessage());
                            Toast.makeText(InitialListActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 加载学校数据
     */
    private void loadSchoolData() {
        String cqlCount = "select count(*) from school";
        AVQuery.doCloudQueryInBackground(cqlCount, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                final int count = avCloudQueryResult.getCount();
                String cqlSchools = "select * from school";
                AVQuery.doCloudQueryInBackground(cqlSchools, new CloudQueryCallback<AVCloudQueryResult>() {
                    @Override
                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                        if(e==null){
                            CommonData.initialSchoolId = new String[count];
                            CommonData.initialSchoolName = new String[count];
                            for(int i=0;i<count;i++){
                                CommonData.initialSchoolId[i] = avCloudQueryResult.getResults().get(i).getString("schoolid");
                                CommonData.initialSchoolName[i] = avCloudQueryResult.getResults().get(i).getString("schoolname");
                            }
                            adapter = new InitialListListAdapter(InitialListActivity.this,CommonData.initialSchoolId,CommonData.initialSchoolName);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    View itemView = listView.getChildAt(position-listView.getFirstVisiblePosition());
                                    TextView tv_id = (TextView) itemView.findViewById(R.id.tv_activity_initial_list_id);
                                    TextView tv_name = (TextView) itemView.findViewById(R.id.tv_activity_initial_list_name);
                                    if(tv_title.getText().toString().equals("学校")){
                                        CommonData.initialSelectedSchoolId = tv_id.getText().toString();
                                        Intent intent = new Intent();
                                        intent.putExtra("result",tv_name.getText().toString());
                                        setResult(Constants.RESULT_SCHOOL,intent);
                                    }else if(tv_title.getText().toString().equals("班级")){
                                        CommonData.initialSelectedClassId = tv_id.getText().toString();
                                        Intent intent = new Intent();
                                        intent.putExtra("result",tv_name.getText().toString());
                                        setResult(Constants.RESULT_CLASS,intent);
                                    }
                                    finish();

                                }
                            });
                            loadingDialog.dismiss();

                        }
                       else{
                            Log.e("error getSchool:",e.getMessage());
                            Toast.makeText(InitialListActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });



    }
}
