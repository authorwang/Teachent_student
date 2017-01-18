package com.ant.nepu.teachent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;
import com.ant.nepu.teachent.fragment.ContactFragment;
import com.ant.nepu.teachent.fragment.HomeworkDetailFragment;

/**
 * 联系老师列表自定义适配器
 * Created by wang1 on 2017/1/18.
 */

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private String[] contactNameList;
    private String[] contactTelList;
    private LayoutInflater layoutInflater;

    public ContactAdapter(Context context, String[] contactNameList, String[] contactTelList) {
        this.context = context;
        this.contactNameList = contactNameList;
        this.contactTelList = contactTelList;
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
        tv_name.setText(Constants.frag_contact_cv_name_title+contactNameList[position]);
        tv_tel.setText(Constants.frag_contact_cv_tel_title+contactTelList[position]);
    }


    @Override
    public int getItemCount() {
        return contactNameList.length;
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
            Toast.makeText(context,"拨打电话："+contactTelList[position],Toast.LENGTH_SHORT).show();
        }
    }
}
