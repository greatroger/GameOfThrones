package com.example.gameofthrones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class ListAdapter extends BaseAdapter {
    private LinkedList<BookContent.BookCover> mData;
    private Context mContext;

    public ListAdapter(LinkedList<BookContent.BookCover> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.book_item,parent,false);
        ImageView img_book = (ImageView) convertView.findViewById(R.id.img_book);
        TextView name_book = (TextView) convertView.findViewById(R.id.txt_book);
        TextView engName_book=(TextView) convertView.findViewById(R.id.engName_book);
        img_book.setBackgroundResource(mData.get(position).cover);
        name_book.setText(mData.get(position).bookName);
        engName_book.setText(mData.get(position).engName);
        return convertView;
    }
}
