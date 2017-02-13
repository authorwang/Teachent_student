package com.ant.nepu.teachent.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.adapter.InformationListAdapter;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.ant.nepu.teachent.util.UserInfoUtils;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;

import java.util.ArrayList;

/**
 * 我的信息Fragment
 */
public class InformationFragment extends Fragment {


    private View mView;
    private ListView listView;
    private InformationListAdapter adapter;
    private Fragment fragment;
    private static Handler handler;
    private LoadingDialog loadingDialog;
    public InformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_information, container, false);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.DATA_PREPARED:
                        adapter = new InformationListAdapter(mView.getContext());
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position){//对应上述配置数据
                                    case 0://头像
                                        fragment = new InformationAvatarFragment();
                                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                                        break;
                                    case 4://班级
                                        fragment = InformationTextFragment.getInstance("班级");
                                        getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
                                        break;
                                }

                            }
                        });
                        loadingDialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };
        //findViews
        listView = (ListView) mView.findViewById(R.id.lv_frag_information);

        loadPreData();

        return mView;
    }

    private void loadPreData() {
        CommonData.informationMainMenuList = new ArrayList<>();
        CommonData.informationMainMenuList.add("填充项：头像");
        UserInfoUtils.refreshAvatar(getContext(),handler);
        UserInfoUtils.refreshUserName();
        UserInfoUtils.refreshSchoolId();
        CommonData.informationMainMenuList.add(CommonData.studentId);
        CommonData.informationMainMenuList.add(CommonData.userName);
        String schoolCql = "select schoolname from school where schoolid='"+ CommonData.userSchoolId+"'";
        AVQuery.doCloudQueryInBackground(schoolCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {

                    CommonData.informationMainMenuList.add(avCloudQueryResult.getResults().get(0).getString("schoolname"));
                    String studentClassCql = "select classname from class where classid in (" +
                            "select classid from studentclass where studentid='"+CommonData.studentId+"')";
                    AVQuery.doCloudQueryInBackground(studentClassCql, new CloudQueryCallback<AVCloudQueryResult>() {
                        @Override
                        public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                CommonData.informationMainMenuList.add(avCloudQueryResult.getResults().get(0).getString("classname")+"...");
                                handler.sendEmptyMessage(Constants.DATA_PREPARED);
                        }
                    });
                }

        });
    }


}
