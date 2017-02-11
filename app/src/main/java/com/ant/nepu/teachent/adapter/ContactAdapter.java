package com.ant.nepu.teachent.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.activity.TeachentMainActivity;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;

/**
 * 联系老师列表自定义适配器
 * Created by wang1 on 2017/1/18.
 */

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    public ContactAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.contact_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder mHolder = (ViewHolder) holder;
        bindItem(mHolder.tv_name,mHolder.tv_tel, mHolder.iv, position);
    }

    /**
     * 绑定联系信息
     * @param tv_name
     * @param tv_tel
     * @param iv
     * @param position
     */
    private void bindItem(TextView tv_name, TextView tv_tel, ImageView iv, int position) {
        tv_name.setText(Constants.frag_contact_cv_name_title+ CommonData.contactTeacherNameList.get(position));
        tv_tel.setText(Constants.frag_contact_cv_tel_title+CommonData.contactTeacherTelList.get(position));
    }


    @Override
    public int getItemCount() {
        return CommonData.contactTeacherNameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_tel;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_frag_contact_name);
            tv_tel = (TextView) itemView.findViewById(R.id.tv_frag_contact_tel);
            iv = (ImageView) itemView.findViewById(R.id.iv_frag_contact_icon);
            itemView.findViewById(R.id.cv_frag_contact).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialContactTel(getPosition());
                }
            });
        }


        /**
         * 拨打联系电话
         */
        private void dialContactTel(int position) {
//            Toast.makeText(context,"拨打电话："+contactTelList[position],Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(context.checkSelfPermission(Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                            + CommonData.contactTeacherTelList.get(position)));
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context,"请先获取拨打电话权限！",Toast.LENGTH_SHORT).show();
                    return;
                }
            }else{
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + CommonData.contactTeacherTelList.get(position)));
                context.startActivity(intent);
            }

        }

    }

    /**
     * 调用电话拨打
     */
    private void callPhone(int position) {

    }
}
