package com.ant.nepu.teachent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ant.nepu.teachent.R;

import java.util.HashMap;

/**
 * 课件ListView自定义适配器
 * Created by wang1 on 2017/1/9.
 */

public class PPTListAdapter extends BaseAdapter {

    public static HashMap<Integer,Boolean> isSelected;//checkbox选中状态
    private String[] texts;//数据显示文本数组
    private Context context;//上下文

//    private int[] icons;

    public PPTListAdapter(Context context, String[] texts){
        this.context = context;
        this.texts = texts;
        isSelected = new HashMap<>();

        initCheckBoxState();//初始化checkbox状态
//        this.icons = icons;
    }

    /**
     * 初始化checkbox状态
     */
    private void initCheckBoxState() {
        for(int i=0;i<texts.length;i++){
            isSelected.put(i,false);//默认未选中
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.ppt_list_item,null);
            vh = new ViewHolder();
            vh.tv_text = (TextView) convertView.findViewById(R.id.tv_frag_ppt_text);
            vh.cb = (CheckBox) convertView.findViewById(R.id.cb_frag_ppt);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }


//        TextView tv_text =(TextView) convertView.findViewById(R.id.tv_frag_ppt_text);
//        TextView tv_text =(TextView) convertView.findViewById(R.id.tv_frag_ppt_text);
        vh.tv_text.setText(texts[position]);
        /**
         * checkbox监听
         */
        vh.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelected.get(position)){
                    isSelected.put(position,false);
                }else{
                    isSelected.put(position,true);
                }
            }
        });

        /**
         * 设置checkbox状态
         */
        vh.cb.setChecked(isSelected.get(position));
        return convertView;
    }

    /**
     * 用以保存第一次查找的组件，避免重复查找
     */
    static class ViewHolder{
        TextView tv_text;
        CheckBox cb;
    }
}
