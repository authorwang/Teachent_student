package com.ant.nepu.teachent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;

/**
 * 我的信息-班级列表ListView自定义适配器
 * Created by wang1 on 2017/1/9.
 */

public class InformationClassListAdapter extends BaseAdapter {


    private Context context;//上下文
    private ListView listView;


    public InformationClassListAdapter(Context context, ListView listView){
        this.context = context;
        this.listView = listView;
    }


    @Override
    public int getCount() {
        return CommonData.classIdList.size();
    }

    @Override
    public Object getItem(int position) {
        return CommonData.classIdList.get(position);
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
            convertView = inflater.inflate(R.layout.information_class_list_item,null);
            vh = new ViewHolder();
            vh.tv_id = (TextView) convertView.findViewById(R.id.tv_frag_information_class_class_id);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_frag_information_class_class_name);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_id.setText(CommonData.classIdList.get(position));
        vh.tv_name.setText(CommonData.classNameList.get(position));
        updateBackground(position,convertView);
        return convertView;
    }

    private void updateBackground(int position, View convertView) {
        if(listView.isItemChecked(position)){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.avoscloud_timestamp_gray));
        }else{
            convertView.setBackgroundColor(context.getResources().getColor(R.color.avoscloud_feedback_white));
        }
    }

    /**
     * 用以保存第一次查找的组件，避免重复查找
     */
    static class ViewHolder{
        TextView tv_id;
        TextView tv_name;
//        CheckBox cb;
    }
}
