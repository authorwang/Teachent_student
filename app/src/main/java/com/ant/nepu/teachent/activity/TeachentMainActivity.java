package com.ant.nepu.teachent.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.fragment.HomeFragment;
import com.ant.nepu.teachent.fragment.PPTFragment;

/**
 * App主界面
 * 管理Fragment
 */
public class TeachentMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachent_main);
        //初始化导航栏()
        initNavigationView();
        // 显示欢迎界面
        goWelcome();
    }

    /**
     * 欢迎界面
     */
    private void goWelcome() {
        //临时：考勤界面
        goCheckIn();

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
        Toast.makeText(TeachentMainActivity.this,getString(R.string.action_qrcode),Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id){
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
        Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_settings_information),Toast.LENGTH_SHORT).show();
    }

    /**
     * 退出当前账号
     */
    private void goLogout() {
        Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_settings_logout),Toast.LENGTH_SHORT).show();
    }

    /**
     * 关于
     */
    private void goAbout() {
        Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_settings_about),Toast.LENGTH_SHORT).show();
    }

    /**
     * 留言板
     */
    private void goLeaveMessage() {
        Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_communicate_leave_message),Toast.LENGTH_SHORT).show();
    }

    /**
     * 联系教师
     */
    private void goContact() {
        Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_communicate_contact),Toast.LENGTH_SHORT).show();
    }

    /**
     * 作业
     */
    private void goHomework() {
        Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_learn_homework),Toast.LENGTH_SHORT).show();
    }

    /**
     * 课件
     */
    private void goPPT() {
        PPTFragment fragment = new PPTFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
    }

    /**
     * 考勤
     */
    private void goCheckIn() {
        //Toast.makeText(TeachentMainActivity.this,getString(R.string.drawer_menu_item_learn_checkin),Toast.LENGTH_SHORT).show();
        HomeFragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_teachent_main,fragment).commit();
    }
}
