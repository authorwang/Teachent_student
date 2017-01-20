package com.ant.nepu.teachent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;

/**
 * 留言板详情RecyclerView自定义适配器
 * Created by wang1 on 2017/1/11.
 */

public class LeaveMessageDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private String[] homeworkDetailList;
    private LayoutInflater layoutInflater;

    public LeaveMessageDetailListAdapter(Context context, String[] homeworkDetailList) {
        this.context = context;
        this.homeworkDetailList = homeworkDetailList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.homework_detail_list_item,parent,false));
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
        tv.setText(homeworkDetailList[position]);
        iv.setImageResource(R.mipmap.app_icon);
    }


    @Override
    public int getItemCount() {
        return homeworkDetailList.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView iv;
        Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_frag_homework_detail_title);
            iv = (ImageView) itemView.findViewById(R.id.iv_frag_homework_detail_capture);
            btn = (Button) itemView.findViewById(R.id.btn_frag_homework_detail_button_select);

            itemView.findViewById(R.id.cv_frag_homework_detail).setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(context,"题组 from position"+ CommonData.homeworkPosition+homeworkDetailList[position],Toast.LENGTH_SHORT).show();
        }
    }
}


