package com.ant.nepu.teachent.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.activity.TeachentMainActivity;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.ant.nepu.teachent.util.ImageUtils;
import com.ant.nepu.teachent.util.UserInfoUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.io.File;

/**
 * 我的信息
 * 头像信息Fragment
 */
public class InformationAvatarFragment extends Fragment {

    private View mView;
    private Button btn_back;
    private Button btn_select;
    private ImageView iv_main;
    private Handler handler;
    private LoadingDialog loadingDialog;

    public InformationAvatarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_information_avatar, container, false);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.UPDATE_USERAVATAR:
                        iv_main.setImageBitmap(CommonData.userAvatar);
                        loadingDialog.dismiss();
                        break;
                }
            }
        };

        //findViews
        btn_back = (Button) mView.findViewById(R.id.btn_frag_information_avatar_back);
        btn_select = (Button) mView.findViewById(R.id.btn_frag_information_avatar_select);
        iv_main = (ImageView) mView.findViewById(R.id.iv_frag_information_avatar_preview);

        //setOnClickListeners
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new InformationFragment()).commit();
            }
        });
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gallery = new Intent();
                intent_gallery.setType("image/*");
                intent_gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_gallery,
                        Constants.IMAGE_REQUEST_CODE);
            }
        });

        loadPreData();
        return mView;
    }

    private void loadPreData() {
        UserInfoUtils.refreshAvatar(getContext(),handler);
    }

    /**
     * 回调结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Constants.RESULT_CANCELED) {
            switch (requestCode) {
                case Constants.IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case Constants.CAMERA_REQUEST_CODE:
                    startPhotoZoom(Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), "avatar_" + CommonData.studentId + ".png")));
                    break;
                case Constants.RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 裁剪图片方法实现
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 192);
        intent.putExtra("outputY", 192);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Constants.RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            final LoadingDialog loadingDialog = new LoadingDialog(mView.getContext());
            loadingDialog.show();
            Bitmap photo = extras.getParcelable("data");
            CommonData.userAvatar = photo;
            final AVFile avFile = new AVFile("avatar_" + CommonData.studentId + ".png", ImageUtils.Bitmap2Bytes(CommonData.userAvatar));
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    AVObject _user = AVObject.createWithoutData("_User", AVUser.getCurrentUser().getObjectId());
                    _user.put("useravatar",avFile);
                    _user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            loadingDialog.dismiss();
                            TeachentMainActivity.handler.sendEmptyMessage(Constants.UPDATE_USERAVATAR);
                            getFragmentManager().beginTransaction().replace(R.id.content_teachent_main, new InformationFragment()).commit();
//                            Toast.makeText(mView.getContext(),"提交成功！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }
}
