package com.ant.nepu.teachent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ant.nepu.teachent.R;

/**
 * 课件ListView自定义适配器
 * Created by wang1 on 2017/1/9.
 */

public class PPTListAdapter extends BaseAdapter {
    private String[] texts;
    private Context context;
//    private int[] icons;

    public PPTListAdapter(Context context, String[] texts){
        this.context = context;
        this.texts = texts;
//        this.icons = icons;
    }
    @Override
    public int getCount() {
        return texts.length;
    }

    @Override
    public Object getItem(int position) {
        return texts[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.ppt_list_item,null);
            vh = new ViewHolder();
            vh.tv_text = (TextView) convertView.findViewById(R.id.tv_frag_ppt_text);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }


//        TextView tv_text =(TextView) convertView.findViewById(R.id.tv_frag_ppt_text);
//        TextView tv_text =(TextView) convertView.findViewById(R.id.tv_frag_ppt_text);
        vh.tv_text.setText(texts[position]);
        return convertView;
    }

    /**
     * 用以保存第一次查找的组件，避免重复查找
     */
    static class ViewHolder{
        TextView tv_text;
    }
}
