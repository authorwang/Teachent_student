package com.ant.nepu.teachent.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;

public class InitialTextActivity extends AppCompatActivity {
    private Button btn_cancel;
    private Button btn_save;
    private TextView tv_title;
    private EditText et_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_text);

        //findViews
        btn_cancel = (Button) findViewById(R.id.btn_activity_initial_text_cancel);
        btn_save = (Button) findViewById(R.id.btn_activity_initial_text_save);
        tv_title = (TextView) findViewById(R.id.tv_activity_initial_text_title);
        et_content = (EditText) findViewById(R.id.et_activity_initial_text_content);

        //改变标题和内容
        Intent intent = getIntent();
        final String title = intent.getStringExtra("title");
        final String content = intent.getStringExtra("content");

        tv_title.setText(title);
        et_content.setText(content);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitialTextActivity.this.finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = et_content.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("result",result);
                if(title.equals("学号")){
                    setResult(Constants.RESULT_NO,intent);
                    CommonData.initialSelectedNo = result;
                }else if(title.equals("姓名")){
                    setResult(Constants.RESULT_NAME,intent);
                    CommonData.initialSelectedName = result;
                }
                finish();

            }
        });
    }
}
