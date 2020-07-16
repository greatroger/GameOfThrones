package com.example.gameofthrones;

import android.app.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater=null;
    private ArrayList<ImageAndText> data;

    public MyAdapter(Activity a, ArrayList<ImageAndText> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.item_grid_icon, null);
        }
        TextView title = (TextView)vi.findViewById(R.id.txt_icon); // 姓名
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.img_icon); // 图片
        ImageAndText page = new ImageAndText();
        page = data.get(position);
        title.setText(page.getText());
        thumb_image.setImageBitmap(page.getBitmap());
        return vi;
    }

}
