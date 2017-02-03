package com.ant.nepu.teachent.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.ant.nepu.teachent.R;

public class TeachentInitialActivity extends AppCompatActivity {

    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachent_initial);

        lv = (ListView) findViewById(R.id.lv_activity_settings);
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        //配置数据
        String[] titles ={
                "头像",
                "学号",
                "姓名",
                "学校",
                "班级"
        };
        //测试数据
        String[] texts = {
                "",
                "",
                "张三",
                "东油",
                "A25"
        };

        adapter = new InformationListAdapter(mView.getContext(),titles,texts);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){//对应上述配置数据
                    case 0://头像
                        fragment = new InformationAvatarFragment();
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                        break;
                    case 1://学号
                        fragment = InformationTextFragment.getInstance("学号");
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                        break;
                    case 2://姓名
                        fragment = InformationTextFragment.getInstance("姓名");
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                        break;
                    case 3://学校
                        fragment = InformationTextFragment.getInstance("学校");
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                        break;
                    case 4://班级
                        fragment = InformationTextFragment.getInstance("班级");
                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                        break;
                }

            }
        });
    }
}
