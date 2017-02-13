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
import com.ant.nepu.teachent.common.CommonData;
import com.ant.nepu.teachent.common.Constants;

import java.util.HashMap;

/**
 * 我的信息ListView自定义适配器
 * Created by wang1 on 2017/1/9.
 */

public class InformationListAdapter extends BaseAdapter {

    private Context context;//上下文

//    private int[] icons;

    public InformationListAdapter(Context context){
        this.context = context;
    }



    @Override
    public int getCount() {
        return Constants.mainMenuTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return Constants.mainMenuTitles[position];
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
            }else if(position==4){
                convertView = inflater.inflate(R.layout.information_text_list_item,null);
            }else{
                convertView = inflater.inflate(R.layout.information_text_no_arrow_list_item,null);
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



        if(position==0){//若为头像，设置头像
            vh.iv_avatar.setImageBitmap(CommonData.userAvatar);
        }else{
            vh.tv_title.setText(Constants.mainMenuTitles[position]);
            vh.tv_text.setText(CommonData.informationMainMenuList.get(position));
        }

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
