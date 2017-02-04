package com.ant.nepu.teachent.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.fragment.AboutUsFragment;
import com.ant.nepu.teachent.fragment.ContactFragment;
import com.ant.nepu.teachent.fragment.HomeFragment;
import com.ant.nepu.teachent.fragment.HomeworkFragment;
import com.ant.nepu.teachent.fragment.InformationFragment;
import com.ant.nepu.teachent.fragment.LeaveMessageDetailFragment;
import com.ant.nepu.teachent.fragment.LeaveMessageFragment;
import com.ant.nepu.teachent.fragment.PPTFragment;
import com.ant.nepu.teachent.fragment.TestLCFragment;
import com.ant.nepu.teachent.util.ImageUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.okhttp.internal.framed.FrameReader;

/**
 * App主界面
 * 管理Fragment
 */
public class TeachentMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    /**
     * UI组件
     *
     * @param savedInstanceState
     */
    public  static TextView tv_nav_username;
    public  static TextView tv_nav_email;
    public  static ImageView iv_nav_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_teachent_main);

//        final View navView = getLayoutInflater().inflate(R.layout.nav_header_teachent_main,null);
        //findViews
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        View nav_header_view = nav_view.getHeaderView(0);
        tv_nav_username = (TextView) nav_header_view.findViewById(R.id.tv_nav_nickname);
        tv_nav_email = (TextView) nav_header_view.findViewById(R.id.tv_nav_username);
        iv_nav_avatar = (ImageView) nav_header_view.findViewById(R.id.iv_nav_avatar);

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.UPDATE_USERAVATAR:
                        iv_nav_avatar.setImageBitmap(CommonData.userAvatar);
                        break;
                    case Constants.UPDATE_USERNAME:
                        tv_nav_username.setText(CommonData.userName);
                        break;
                    case Constants.UPDATE_USEREMAIL:
                        tv_nav_email.setText(CommonData.userEmail);
                        break;
                }
            }
        };
        //预加载用户数据
        loadUserData(handler);
        //初始化导航栏()
        initNavigationView();
        //判断用户是否初始化
        CommonData.isInitial = AVUser.getCurrentUser().getBoolean("isInitial");
        if (!CommonData.isInitial) {
            startActivity(new Intent(TeachentMainActivity.this,TeachentInitialActivity.class));
            TeachentMainActivity.this.finish();
        }
        // 显示欢迎界面
        goWelcome();
    }





    /**
     * 预加载用户数据
     * 更新到UI并写入commonData
     */
    private void loadUserData(final Handler handler) {
        /**
         * 从缓存获取用户数据
         */
        String userid = AVUser.getCurrentUser().getObjectId();
        String userrealname = AVUser.getCurrentUser().getString("userrealname");
        String email = AVUser.getCurrentUser().getUsername();
        String schoolid = AVUser.getCurrentUser().getString("schoolid");
        int userCreditA = AVUser.getCurrentUser().getInt("usercreditA");
        int userCreditB = AVUser.getCurrentUser().getInt("usercreditB");
        AVFile userAvatar = AVUser.getCurrentUser().getAVFile("useravatar");

        /**
         * 将有效值写入到CommonData
         */
        //写入用户名 CommonData
        if (userrealname==null) {//未设置用户名
            CommonData.userName = getString(R.string.text_user_null);
        } else {
            CommonData.userName = userrealname;
        }
        handler.sendEmptyMessage(Constants.UPDATE_USERNAME);
//        Toast.makeText(this,CommonData.userName,Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,tv_nav_username.toString(),Toast.LENGTH_SHORT).show();

        //写入email CommonData
        CommonData.userEmail = email;
        //tv_nav_email.setText(CommonData.userEmail);
        handler.sendEmptyMessage(Constants.UPDATE_USEREMAIL);
//        Toast.makeText(this,CommonData.userEmail,Toast.LENGTH_SHORT).show();

        //写入学校id CommonData
        if (schoolid == null) {
            CommonData.userSchoolId = "-1";
        } else {
            CommonData.userSchoolId = schoolid;
        }

        //写入积分 CommonData
        CommonData.userCreditA = userCreditA;
        CommonData.userCreditB = userCreditB;

        /**
         * 写入头像 CommonData
         */
        CommonData.userRawAvatar = userAvatar;
//        //从服务器获取AVFile文件
//        AVQuery<AVObject> avQuery = new AVQuery<>("_User");
//        avQuery.getInBackground(userid, new GetCallback<AVObject>() {
//            @Override
//            public void done(AVObject avObject, AVException e) {
//                if (e == null) {
//                    if (avObject != null) {
//                        CommonData.userRawAvatar = avObject.getAVFile("useravatar");
//                    } else {
//                        CommonData.userRawAvatar = null;
//                    }
//                } else {
//                    Log.e("avQuery error:", e.getMessage());
//                    Toast.makeText(TeachentMainActivity.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        //将AVFile转换为Bitmap
        if (CommonData.userRawAvatar != null) {
            //下载AVFile
            CommonData.userRawAvatar.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, AVException e) {
                    if (e == null) {//转换为bitmap
                        CommonData.userAvatar = ImageUtils.getPicFromBytes(bytes, null);
//                        Toast.makeText(TeachentMainActivity.this,CommonData.userAvatar.toString(),Toast.LENGTH_LONG).show();
                        //iv_nav_avatar.setImageBitmap(CommonData.userAvatar);
                        handler.sendEmptyMessage(Constants.UPDATE_USERAVATAR);

                    } else {
                        Log.e("download avfile error:", e.getMessage());
                        Toast.makeText(TeachentMainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {//使用默认头像
            CommonData.userAvatar = BitmapFactory.decodeResource(this.getResources(), R.mipmap.avatar_teacher_female);
            //iv_nav_avatar.setImageBitmap(CommonData.userAvatar);
            handler.sendEmptyMessage(Constants.UPDATE_USERAVATAR);
        }



//        /**
//         * 将CommonData更新到UI
//         */
//
//        //更新用户头像
//        iv_nav_avatar.setImageBitmap(CommonData.userAvatar);
//        //更新用户名
//        tv_nav_username.setText(CommonData.userName);
//        //更新用户账号
//        tv_nav_email.setText(CommonData.userEmail);
//        //更新用户积分


    }

    /**
     * 欢迎界面
     */
    private void goWelcome() {
        //临时：考勤界面
        goCheckIn();
//        TestLCFragment fragment = new TestLCFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
    }

    private void initNavigationView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teachent_main, menu);
        return true;
    }

    /**
     * ActionBar OnClick事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_qrcode) {
            /**
             * 调用二维码扫描功能
             */
            goQRCode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 二维码扫描
     */
    private void goQRCode() {
        Toast.makeText(TeachentMainActivity.this, getString(R.string.action_qrcode), Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_checkin://考勤
                goCheckIn();
                break;
            case R.id.nav_ppt://课件
                goPPT();
                break;
            case R.id.nav_homework://作业
                goHomework();
                break;
            case R.id.nav_contact://联系人
                goContact();
                break;
            case R.id.nav_leave_message://留言板
                goLeaveMessage();
                break;
            case R.id.nav_information://我的信息
                goInformation();
                break;
            case R.id.nav_about://关于我们
                goAbout();
                break;
            case R.id.nav_logout://退出当前账号
                goLogout();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 我的信息
     */
    private void goInformation() {
        InformationFragment fragment = new InformationFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_teachent_main, fragment).commit();
    }

    /**
     * 退出当前账号
     */
    private void goLogout() {
//        Toast.makeText(TeachentMainActivity.this, getString(R.string.drawer_menu_item_settings_logout), Toast.LENGTH_SHORT).show();
        AVUser.logOut();
        resetCommonData();
        AVUser user = AVUser.getCurrentUser();
        startActivity(new Intent(TeachentMainActivity.this,TeachentLoginActivity.class));
        TeachentMainActivity.this.finish();
    }

    /**
     * 重置CommonData数据
     */
    private void resetCommonData() {
        CommonData.userAvatar =null;
        CommonData.userName = null;
        CommonData.userEmail = null;
        CommonData.userCreditA = 0;
        CommonData.userCreditB = 0;
        CommonData.initialSelectedNo = "";
        CommonData.initialSelectedName = "";
        CommonData.initialSelectedClassId = "";
        CommonData.initialSelectedSchoolId = "";
    }

    /**
     * 关于
     */
    private void goAbout() {
//        Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_settings_about),Toast.LENGTH_SHORT).show();
        AboutUsFragment fragment = new AboutUsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_teachent_main, fragment).commit();
    }


    /**
     * 留言板
     */
    private void goLeaveMessage() {
//        Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_communicate_leave_message),Toast.LENGTH_SHORT).show();
        LeaveMessageFragment fragment = new LeaveMessageFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_teachent_main, fragment).commit();
    }

    /**
     * 联系教师
     */
    private void goContact() {
//        Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_communicate_contact),Toast.LENGTH_SHORT).show();
        ContactFragment fragment = new ContactFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_teachent_main, fragment).commit();
    }


    /**
     * 作业
     */
    private void goHomework() {
//        Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_learn_homework),Toast.LENGTH_SHORT).show();
        HomeworkFragment fragment = new HomeworkFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_teachent_main, fragment).commit();
    }

    /**
     * 课件
     */
    private void goPPT() {
        PPTFragment fragment = new PPTFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_teachent_main, fragment).commit();
    }

    /**
     * 考勤
     */
    private void goCheckIn() {
        //Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_learn_checkin),Toast.LENGTH_SHORT).show();
        HomeFragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_teachent_main, fragment).commit();
    }
}
