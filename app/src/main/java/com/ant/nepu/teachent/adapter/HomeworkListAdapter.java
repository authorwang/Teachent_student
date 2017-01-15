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
import com.ant.nepu.teachent.entity.Homework;

import java.util.List;

/**
 * 作业RecyclerView自定义适配器
 * Created by wang1 on 2017/1/11.
 */

public class HomeworkListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private String[] homeworkList;
    private LayoutInflater layoutInflater;

    public HomeworkListAdapter(Context context, String[] homeworkList) {
        this.context = context;
        this.homeworkList = homeworkList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.homework_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder mHolder = (ViewHolder) holder;
        bindItem(mHolder.tv,mHolder.iv,position);
    }

    /**
     * 绑定一条数据
     * @param
     * @param tv
     * @param iv
     */
    private void bindItem(TextView tv, ImageView iv, int position) {
        tv.setText(homeworkList[position]);
    }


    @Override
    public int getItemCount() {
        return homeworkList.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_frag_homework_text);
            iv = (ImageView) itemView.findViewById(R.id.iv_frag_homework_icon);
            itemView.findViewById(R.id.cv_frag_homework).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showHomeworkDetails(getPosition());
                }
            });
        }

        /**
         * 显示作业详情
         * @param position
         */
        private void showHomeworkDetails(int position) {
            Toast.makeText(context,"显示作业详情"+homeworkList[position],Toast.LENGTH_SHORT).show();
        }
    }
}


