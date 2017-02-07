package com.ant.nepu.teachent.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.InitialListAdapter;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

public class TeachentInitialActivity extends AppCompatActivity {



    private ListView listView;
    private InitialListAdapter adapter;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachent_initial);

        listView = (ListView) findViewById(R.id.lv_activity_settings);
        loadingDialog = new LoadingDialog(this);
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        //配置数据
        String[] titles ={

                "学号",
                "姓名",
                "学校",
                "班级",
                "完成设置"
        };
        //测试数据

        String[] texts = {
                "",
                "",
                "",
                "",
                ""
        };

        adapter = new InitialListAdapter(this,titles,texts);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){//对应上述配置数据
                    case 0://学号
                        Intent intent_no = new Intent(TeachentInitialActivity.this,InitialTextActivity.class);
                        intent_no.putExtra("title","学号");
                        View view_no = listView.getChildAt(0);
                        TextView tv_no = (TextView) view_no.findViewById(R.id.tv_activity_initial_text);
                        intent_no.putExtra("content",tv_no.getText().toString());
                        startActivityForResult(intent_no, Constants.REQUEST_TEXT_NO);
                        break;
                    case 1://姓名
                        Intent intent_name = new Intent(TeachentInitialActivity.this,InitialTextActivity.class);
                        intent_name.putExtra("title","姓名");
                        View view_name = listView.getChildAt(1);
                        TextView tv_name = (TextView) view_name.findViewById(R.id.tv_activity_initial_text);
                        intent_name.putExtra("content",tv_name.getText().toString());
                        startActivityForResult(intent_name, Constants.REQUEST_TEXT_NAME);
                        break;
                    case 2://学校
                        Intent intent_school = new Intent(TeachentInitialActivity.this,InitialListActivity.class);
                        intent_school.putExtra("title","学校");
                        startActivityForResult(intent_school,Constants.REQUEST_LIST_SCHOOL);
                        break;
                    case 3://班级
                        Intent intent_class = new Intent(TeachentInitialActivity.this,InitialListActivity.class);
                        intent_class.putExtra("title","班级");
                        startActivityForResult(intent_class,Constants.REQUEST_LIST_CLASS);
                        break;
                    case 4://完成设置
                        //判空
                        if(CommonData.initialSelectedNo.equals("") ||
                                CommonData.initialSelectedName.equals("") ||
                                CommonData.initialSelectedSchoolId.equals("") ||
                                CommonData.initialSelectedClassId.equals("")){
                            Toast.makeText(TeachentInitialActivity.this,getString(R.string.error_initial_finish_setting_empty),Toast.LENGTH_SHORT).show();
                            return;
                        }
                        loadingDialog.show();
                        //更新相关数据库
                        //更新student表
                        AVObject student = new AVObject("student");
                        student.put("studentid",CommonData.initialSelectedNo);
                        student.put("studentname",CommonData.initialSelectedName);
                        student.saveInBackground();
                        //更新userrole表
                        AVObject userrole = new AVObject("userrole");
                        userrole.put("rolename","student");
                        userrole.put("userid", AVUser.getCurrentUser().getObjectId());
                        userrole.put("relatedid",CommonData.initialSelectedNo);
                        userrole.saveInBackground();
                        //更新studentclass表
                        AVObject studentclass = new AVObject("studentclass");
                        studentclass.put("studentid",CommonData.initialSelectedNo);
                        studentclass.put("classid",CommonData.initialSelectedClassId);
                        studentclass.saveInBackground();
                        //更新_User表
                        AVObject _user = AVObject.createWithoutData("_User",AVUser.getCurrentUser().getObjectId());
                        _user.put("isInitial",true);
                        _user.put("userrealname",CommonData.initialSelectedName);
                        _user.put("schoolid",CommonData.initialSelectedSchoolId);
                        _user.saveInBackground();
                        AVUser.logOut();
                        Intent intent_finish = new Intent(TeachentInitialActivity.this,TeachentLoginActivity.class);
                        startActivity(intent_finish);
                        Toast.makeText(TeachentInitialActivity.this,getString(R.string.tip_initial_finish_setting_relogin),Toast.LENGTH_LONG).show();
                        loadingDialog.dismiss();
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Constants.RESULT_NO && requestCode==Constants.REQUEST_TEXT_NO){//返回学号
            String resultNo = data.getStringExtra("result");
            View view = listView.getChildAt(0);
            TextView tv = (TextView) view.findViewById(R.id.tv_activity_initial_text);
            tv.setText(resultNo);
        }else if(resultCode==Constants.RESULT_NAME && requestCode==Constants.REQUEST_TEXT_NAME){
            String resultName = data.getStringExtra("result");
            View view = listView.getChildAt(1);
            TextView tv = (TextView) view.findViewById(R.id.tv_activity_initial_text);
            tv.setText(resultName);
        }else if(resultCode==Constants.RESULT_SCHOOL && requestCode==Constants.REQUEST_LIST_SCHOOL){
            String resultSchool = data.getStringExtra("result");
            View view = listView.getChildAt(2);
            TextView tv  = (TextView) view.findViewById(R.id.tv_activity_initial_text);
            tv.setText(resultSchool);
        }else if(resultCode==Constants.RESULT_CLASS && requestCode==Constants.REQUEST_LIST_CLASS){
            String resultClass = data.getStringExtra("result");
            View view = listView.getChildAt(3);
            TextView tv  = (TextView) view.findViewById(R.id.tv_activity_initial_text);
            tv.setText(resultClass);
        }

        }
    }

