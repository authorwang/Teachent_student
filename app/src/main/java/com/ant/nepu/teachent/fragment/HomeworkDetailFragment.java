package com.ant.nepu.teachent.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.DownloadingDialog;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.ant.nepu.teachent.util.ImageUtils;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;

/**
 * 作业-作业详情Fragment
 */
public class HomeworkDetailFragment extends Fragment {

    private View mView;
    private TextView tv_name;
    private ImageView iv_detail;
    private ImageView iv_answer;
    private ImageView iv_popup_detail;
    private Button btn_back;
    private Button btn_submit;
    private LoadingDialog loadingDialog;
    private PopupWindow popupWindow;

    public HomeworkDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_homework_detail, container, false);
        loadingDialog = new LoadingDialog(mView.getContext());
        loadingDialog.show();

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constants.DATA_PREPARED:
                        tv_name.setText(CommonData.homeworkNameList.get(CommonData.homeworkPosition));
                        iv_detail.setImageBitmap(CommonData.homeworkDetailBitmap);
                        if (CommonData.homeworkAnswerBitmap != null){
                            iv_answer.setImageBitmap(CommonData.homeworkAnswerBitmap);
                            iv_answer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    View popupView = inflater.inflate(R.layout.popup_window_hw_detail, null);
                                    popupWindow = new PopupWindow(popupView, 1000, 2000, true);
                                    popupWindow.setTouchable(true);
                                    popupWindow.setOutsideTouchable(true);
                                    iv_popup_detail = (ImageView) popupView.findViewById(R.id.iv_popup_window_hw_detail);
                                    iv_popup_detail.setImageBitmap(CommonData.homeworkAnswerBitmap);
                                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                                }
                            });
                        }

                        iv_detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                View popupView = inflater.inflate(R.layout.popup_window_hw_detail, null);
                                popupWindow = new PopupWindow(popupView, 1000,2000, true);
                                popupWindow.setTouchable(true);
                                popupWindow.setFocusable(true);
                                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                                popupWindow.setOutsideTouchable(true);
                                iv_popup_detail = (ImageView) popupView.findViewById(R.id.iv_popup_window_hw_detail);
                                iv_popup_detail.setImageBitmap(CommonData.homeworkDetailBitmap);
                                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                            }
                        });
                        btn_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction().replace(R.id.content_teachent_main, new HomeworkFragment()).commit();
                            }
                        });
                        if(!CommonData.hasSubmit){
                            btn_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intentFromCapture = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                            .fromFile(new File(Environment
                                                    .getExternalStorageDirectory(),
                                                    "hw_myanswer_" + CommonData.homeworkNo + ".png")));
                                    startActivityForResult(intentFromCapture,
                                            Constants.CAMERA_REQUEST_CODE);
                                }
                            });
                        }else{
                            btn_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(mView.getContext(),"你已经提交过本作业！",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        loadingDialog.dismiss();
                        break;

                }

            }
        };
        //findViews
        tv_name = (TextView) mView.findViewById(R.id.tv_frag_homework_detail_name);
        iv_detail = (ImageView) mView.findViewById(R.id.iv_frag_homework_detail);
        iv_answer = (ImageView) mView.findViewById(R.id.iv_frag_homework_answer);
        btn_back = (Button) mView.findViewById(R.id.btn_frag_homework_detail_back);
        btn_submit = (Button) mView.findViewById(R.id.btn_frag_homework_detail_submit);

        //预加载数据
        loadPreData(handler);
        return mView;
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

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {

                case Constants.CAMERA_REQUEST_CODE:
                    startPhotoZoom(Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), "hw_myanswer_" + CommonData.homeworkNo + ".png")));
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
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
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
            CommonData.homeworkAnswerBitmap = photo;
            final AVFile avFile = new AVFile("hw_myanswer_" + CommonData.homeworkNo + ".png",ImageUtils.Bitmap2Bytes(CommonData.homeworkAnswerBitmap));
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    AVObject studenthw = new AVObject("studenthw");
                    studenthw.put("studentid",CommonData.studentId);
                    studenthw.put("hwid",CommonData.homeworkIdList.get(CommonData.homeworkPosition));
                    studenthw.put("hwanswer",avFile);
                    studenthw.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            loadingDialog.dismiss();
                            getFragmentManager().beginTransaction().replace(R.id.content_teachent_main, new HomeworkFragment()).commit();
                            Toast.makeText(mView.getContext(),"提交成功！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }


    /**
     * 预加载数据
     *
     * @param handler
     */
    private void loadPreData(final Handler handler) {
        CommonData.hasSubmit = false;
        String homeworkCql = "select include hwdetail from homework where hid='" + CommonData.homeworkNo + "'";
        AVQuery.doCloudQueryInBackground(homeworkCql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if (e == null) {
                    AVFile file = avCloudQueryResult.getResults().get(0).getAVFile("hwdetail");
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, AVException e) {
                            CommonData.homeworkDetailBitmap = ImageUtils.getPicFromBytes(bytes, null);
                            String homeworkAnswerCountCql = "select count(*) from studenthw where hwid='" + CommonData.homeworkNo + "' and studentid='" + CommonData.studentId + "'";
                            AVQuery.doCloudQueryInBackground(homeworkAnswerCountCql, new CloudQueryCallback<AVCloudQueryResult>() {
                                @Override
                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                    if (e == null) {
                                        if (avCloudQueryResult.getCount() == 0) {
                                            CommonData.homeworkAnswerBitmap = null;

                                            handler.sendEmptyMessage(Constants.DATA_PREPARED);
                                        } else {
                                            String homeworkAnswerCql = "select include hwanswer from studenthw where hwid='" + CommonData.homeworkNo + "' and studentid='" + CommonData.studentId + "'";
                                            AVQuery.doCloudQueryInBackground(homeworkAnswerCql, new CloudQueryCallback<AVCloudQueryResult>() {
                                                @Override
                                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                                    AVFile avFile = avCloudQueryResult.getResults().get(0).getAVFile("hwanswer");
                                                    avFile.getDataInBackground(new GetDataCallback() {
                                                        @Override
                                                        public void done(byte[] bytes, AVException e) {
                                                            CommonData.homeworkAnswerBitmap = ImageUtils.getPicFromBytes(bytes, null);
                                                            CommonData.hasSubmit = true;
                                                            handler.sendEmptyMessage(Constants.DATA_PREPARED);
                                                        }
                                                    }, new ProgressCallback() {
                                                        @Override
                                                        public void done(Integer integer) {

                                                        }
                                                    });
                                                }
                                            });
                                        }


                                    }
                                }

                            });
                        }
                    }, new ProgressCallback() {
                        @Override
                        public void done(Integer integer) {

                        }
                    });


            }
        }
    }

    );
}

}
