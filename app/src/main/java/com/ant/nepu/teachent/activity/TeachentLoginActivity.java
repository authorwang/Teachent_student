package com.ant.nepu.teachent.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.dialog.LoadingDialog;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import static android.Manifest.permission.READ_CONTACTS;
/**
 * 登录界面
 * attemptLogin:登录相关代码（包括注册）
 * 使用AsyncTask线程完成
 */

/**
 * A login screen that offers login via email/password.
 */
public class TeachentLoginActivity extends AppCompatActivity {


    private LoadingDialog loadingDialog;
    private String email;
    private String password;
    private boolean cancel;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constants.CHECK_ROLE_PROCESS:
                        doCheckRoleProcess(this);
                        break;
                    case Constants.LOGIN_PROCESS:
                        doLoginProcess(this);
                        break;
                    case Constants.REGISITER_PROCESS:
                        doRegisterProcess();
                        break;
                }
            }
        };
        setContentView(R.layout.activity_teachent_login);
        loadingDialog = new LoadingDialog(this);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    CommonData.hasLogin = false;
                    CommonData.hasRegistered = false;
                    handler.sendEmptyMessage(Constants.CHECK_ROLE_PROCESS);
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonData.hasLogin = false;
                CommonData.hasRegistered = false;
                handler.sendEmptyMessage(Constants.CHECK_ROLE_PROCESS);
            }
        });

    }

    /**
     * 检查权限
     */
    private void doCheckRoleProcess(final Handler handler) {
        clearErrors();
        if(!cancel){
            final String _userCql = "select objectId from _User where username='"+email+"'";
            AVQuery.doCloudQueryInBackground(_userCql, new CloudQueryCallback<AVCloudQueryResult>() {
                @Override
                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                        if (!avCloudQueryResult.getResults().isEmpty()){
                            String userid = avCloudQueryResult.getResults().get(0).getObjectId();
                            String userroleCql = "select rolename from userrole where userid='"+userid+"'";
                            AVQuery.doCloudQueryInBackground(userroleCql, new CloudQueryCallback<AVCloudQueryResult>() {
                                @Override
                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                    if(!avCloudQueryResult.getResults().isEmpty()){
                                        if(avCloudQueryResult.getResults().get(0).get("rolename")==null){
                                            handler.sendEmptyMessage(Constants.LOGIN_PROCESS);
                                        }else if(!avCloudQueryResult.getResults().get(0).getString("rolename").equals("teacher")){
                                            handler.sendEmptyMessage(Constants.LOGIN_PROCESS);
                                        }else{
                                            Toast.makeText(TeachentLoginActivity.this,"登录失败，请验证Email身份。",Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismiss();
                                        }
                                    }else{
                                        handler.sendEmptyMessage(Constants.LOGIN_PROCESS);
                                    }

                                }

                            });
                        }else{
                            handler.sendEmptyMessage(Constants.LOGIN_PROCESS);
                        }

                    }

            });
        }
    }

    /**
     * 清空错误记录器
     */
    private void clearErrors() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;

        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            focusView.requestFocus();
        } else {
            loadingDialog.show();
        }
    }

    /**
     * 注册流程
     */
    private void doRegisterProcess() {
            AVUser user = new AVUser();
            user.setUsername(email);
            user.setPassword(password);
            user.setEmail(email);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    CommonData.hasRegistered = true;
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                        if (e == null) {
                            startActivity(new Intent(TeachentLoginActivity.this, TeachentMainActivity.class));
                            TeachentLoginActivity.this.finish();
                        }else if (e.getMessage().contains("203")){
                            Toast.makeText(TeachentLoginActivity.this,"登录或失败，请检查Email和密码是否正确填写！",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
    }


    /**
     * 登录流程
     */
    private void doLoginProcess(final Handler handler) {
        if(!cancel){
            AVUser.logInInBackground(email, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        CommonData.hasLogin = true;
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                        startActivity(new Intent(TeachentLoginActivity.this, TeachentMainActivity.class));
                        TeachentLoginActivity.this.finish();
                    } else {
                        if(e.getMessage().contains("219")){
                            Toast.makeText(TeachentLoginActivity.this,"登录失败次数达到峰值，请在15min后尝试登录。",Toast.LENGTH_LONG).show();
                            if (loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                        }else{
                            handler.sendEmptyMessage(Constants.REGISITER_PROCESS);
                        }
                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }



}

