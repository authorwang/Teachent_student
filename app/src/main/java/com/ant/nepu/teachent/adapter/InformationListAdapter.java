package com.ant.nepu.teachent.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ant.nepu.teachent.R;

import java.util.HashMap;

/**
 * 我的信息ListView自定义适配器
 * Created by wang1 on 2017/1/9.
 */

public class InformationListAdapter extends BaseAdapter {

//    public static HashMap<Integer,Boolean> isSelected;//checkbox选中状态
    private String[] titles;//标题数组
    private String[] texts;//数据数组
    private Context context;//上下文

//    private int[] icons;

    public InformationListAdapter(Context context, String[] titles, String[] texts){
        this.context = context;
        this.titles = titles;
        this.texts = texts;
//        isSelected = new HashMap<>();

//        initCheckBoxState();//初始化checkbox状态
//        this.icons = icons;
    }

//    /**
//     * 初始化checkbox状态
//     */
//    private void initCheckBoxState() {
//        for(int i=0;i<texts.length;i++){
//            isSelected.put(i,false);//默认未选中
//        }
//    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
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
            if(position==0){//若为头像
                convertView = inflater.inflate(R.layout.information_avatar_list_item,null);
            }else{
                convertView = inflater.inflate(R.layout.information_text_list_item,null);
            }
            vh = new ViewHolder();
            if(position==0){//若为头像
                vh.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_frag_information_avatar);
            }else {
                vh.tv_title = (TextView) convertView.findViewById(R.id.tv_frag_information_title);
                vh.tv_text = (TextView) convertView.findViewById(R.id.tv_frag_information_text);
            }
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }


//        TextView tv_text =(TextView) convertView.findViewById(R.id.tv_frag_ppt_text);
//        TextView tv_text =(TextView) convertView.findViewById(R.id.tv_frag_ppt_text);
        if(position==0){//若为头像，设置头像
            vh.iv_avatar.setImageResource(R.mipmap.avatar_student_female);
        }else{
            vh.tv_title.setText(titles[position]);
            vh.tv_text.setText(texts[position]);
        }


//        /**
//         * checkbox监听
//         */
//        vh.cb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isSelected.get(position)){
//                    isSelected.put(position,false);
//                }else{
//                    isSelected.put(position,true);
//                }
//            }
//        });

//        /**
//         * 设置checkbox状态
//         */
//        vh.cb.setChecked(isSelected.get(position));
        return convertView;
    }

    /**
     * 用以保存第一次查找的组件，避免重复查找
     */
    static class ViewHolder{
        TextView tv_title;
        TextView tv_text;
        ImageView iv_avatar;
    }
}
