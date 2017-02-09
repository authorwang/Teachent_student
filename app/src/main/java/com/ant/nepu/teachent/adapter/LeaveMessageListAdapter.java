package com.ant.nepu.teachent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.fragment.LeaveMessageDetailFragment;
import com.ant.nepu.teachent.fragment.LeaveMessageFragment;

/**
 * 留言板RecyclerView自定义适配器
 * Created by wang1 on 2017/1/11.
 */

public class LeaveMessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private String[] leaveMessageNameList;
    private String[] leaveMessageSimpleTextList;
    private LayoutInflater layoutInflater;
    private LeaveMessageFragment fragment;

    public LeaveMessageListAdapter(Context context, String[] leaveMessageNameList, String[] leaveMessageSimpleTextList, LeaveMessageFragment fragment) {
        this.context = context;
        this.leaveMessageNameList = leaveMessageNameList;
        this.leaveMessageSimpleTextList = leaveMessageSimpleTextList;
        this.fragment = fragment;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.leave_message_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder mHolder = (ViewHolder) holder;
        bindItem(mHolder.tv_name,mHolder.tv_simple_text,mHolder.iv,position);
    }

    /**
     * 绑定一条数据
     * @param
     * @param iv
     */
    private void bindItem(TextView tv_name,TextView tv_simple_text, ImageView iv, int position) {
        tv_name.setText(leaveMessageNameList[position]);
        tv_simple_text.setText(leaveMessageSimpleTextList[position]);
        iv.setImageResource(R.mipmap.avatar_student_male);
    }


    @Override
    public int getItemCount() {
        return leaveMessageNameList.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_simple_text;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_frag_leave_message_name);
            tv_simple_text = (TextView) itemView.findViewById(R.id.tv_frag_leave_message_text);
            iv = (ImageView) itemView.findViewById(R.id.iv_frag_leave_message_icon);
            itemView.findViewById(R.id.cv_frag_leave_message).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLeaveMessageDetails(getPosition());
                }
            });
        }

        /**
         * 显示留言板详情
         * @param position
         */
        private void showLeaveMessageDetails(int position) {
            Toast.makeText(context,"显示留言板详情"+leaveMessageSimpleTextList[position],Toast.LENGTH_SHORT).show();
            CommonData.leaveMessagePosition =position;
            fragment.getFragmentManager().beginTransaction().replace(R.id.content_teachent_main,new LeaveMessageDetailFragment()).commit();
        }
    }
}


