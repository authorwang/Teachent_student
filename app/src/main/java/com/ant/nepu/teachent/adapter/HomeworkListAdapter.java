package com.ant.nepu.teachent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;

/**
 * 作业-作业列表ListView自定义适配器
 * Created by wang1 on 2017/1/9.
 */

public class HomeworkListAdapter extends BaseAdapter {

    private Context context;//上下文


    public HomeworkListAdapter(Context context){
        this.context = context;

    }


    @Override
    public int getCount() {
        return CommonData.homeworkNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return CommonData.homeworkNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.homework_list_item,null);
            vh = new ViewHolder();
            vh.tv_id = (TextView) convertView.findViewById(R.id.tv_frag_homework_id);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_frag_homework_name);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_id.setText(CommonData.homeworkIdList.get(position));
        vh.tv_name.setText(CommonData.homeworkNameList.get(position));
        return convertView;
    }

    /**
     * 用以保存第一次查找的组件，避免重复查找
     */
    static class ViewHolder{
        TextView tv_id;
        TextView tv_name;
    }
}
