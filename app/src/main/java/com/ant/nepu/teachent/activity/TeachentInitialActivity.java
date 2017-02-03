package com.ant.nepu.teachent.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.InitialListAdapter;
import com.ant.nepu.teachent.common.Constants;

public class TeachentInitialActivity extends AppCompatActivity {



    private ListView listView;
    private InitialListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachent_initial);

        listView = (ListView) findViewById(R.id.lv_activity_settings);
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
                        break;
                    case 3://班级
                        break;
                    case 4://完成设置
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
        }

        }
    }

