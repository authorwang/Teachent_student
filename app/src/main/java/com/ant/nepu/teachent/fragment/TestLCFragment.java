package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestLCFragment extends Fragment {

    private View mView;
    private Button mButton;
    private TextView mTextView;

    public TestLCFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_test_lc, container, false);

        mButton = (Button) mView.findViewById(R.id.test_btn_query_role);
        mTextView = (TextView) mView.findViewById(R.id.test_tv_query_role);

        registerForQueryRole();
        return mView;
    }

    /**
     * 注册查询role表事件
     */
    private void registerForQueryRole() {
        /**
         * cql查询方式
         */
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String cql2 = "select count(*) from role";
//                AVQuery.doCloudQueryInBackground(cql2, new CloudQueryCallback<AVCloudQueryResult>() {
//                    @Override
//                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
//                       final  int count = avCloudQueryResult.getCount();
//                        String cql = "select * from role";
//                        AVQuery.doCloudQueryInBackground(cql, new CloudQueryCallback<AVCloudQueryResult>() {
//                            @Override
//                            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
////                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
////                                String m = avCloudQueryResult.getResults().get(2).getString("rolename");
////                                int number = avCloudQueryResult.getCount();
////                                Toast.makeText(getContext(), number, Toast.LENGTH_SHORT).show();
//                                String msg = "";
//                                for (int i = 0; i < count; i++) {
//                                    msg += avCloudQueryResult.getResults().get(i).getString("rolename") + "\r\n";
//                                }
//                                mTextView.setText(msg);
//
//                            }
//                        });
//                    }
//                });
//
//            }
//        });

        /**
         * object查询方式
         */
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVObject role = new AVObject("role");
                role.put("rolename","testRole1");
                role.saveInBackground();
                role.put("rolename","testRole2");
                role.saveInBackground();
            }
        });
    }

}
