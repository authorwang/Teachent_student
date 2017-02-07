package com.ant.nepu.teachent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ant.nepu.teachent.R;
import com.ant.nepu.teachent.common.CommonData;

import java.util.HashMap;

/**
 * 课件ListView自定义适配器
 * Created by wang1 on 2017/1/9.
 */

public class PPTBaseListAdapter extends BaseAdapter {

    public static HashMap<Integer,Boolean> isSelected;//checkbox选中状态
    private String[] texts;//数据显示文本数组
    private Context context;//上下文

//    private int[] icons;

    public PPTBaseListAdapter(Context context){
        this.context = context;
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
            convertView = inflater.inflate(R.layout.ppt_base_list_item,null);
            vh = new ViewHolder();
            vh.tv_id = (TextView) convertView.findViewById(R.id.tv_frag_ppt_base_id);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_frag_ppt_base_classname);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_id.setText(CommonData.classIdList.get(position));
        vh.tv_name.setText(CommonData.classNameList.get(position));

        return convertView;
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
