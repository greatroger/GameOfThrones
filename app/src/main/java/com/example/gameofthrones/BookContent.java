package com.example.gameofthrones;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class BookContent extends AppCompatActivity implements AdapterView.OnItemClickListener {
    class BookCover{
        public String bookName;
        public int cover;
        public String engName;

        public BookCover(String bookName,String engName,int cover){
            this.bookName=bookName;
            this.cover=cover;
            this.engName=engName;
        }
    }
    private List<BookCover> mData = null;
    private Context mContext;
    private ListAdapter mAdapter = null;
    private ListView bookList;
    public String paraBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_content);
        mContext = BookContent.this;
        bookList = (ListView) findViewById(R.id.list_book);

        mData = new LinkedList<BookCover>();
        mData.add(new BookCover("权力的游戏","A Game of Thrones" ,R.mipmap.cover1));
        mData.add(new BookCover("列王的纷争","A Clash of Kings", R.mipmap.cover2));
        mData.add(new BookCover("冰雨的风暴","A Storm of Swords", R.mipmap.cover3));
        mData.add(new BookCover("群鸦的盛宴","A Feast for Crows", R.mipmap.cover4));
        mData.add(new BookCover("魔龙的狂舞","A Dance with Dragons", R.mipmap.cover5));
        mAdapter = new ListAdapter((LinkedList<BookCover>) mData, mContext);
        bookList.setAdapter(mAdapter);
        bookList.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        paraBook=mData.get(position).bookName;
        Log.e("BookName:",paraBook);
        Intent intent = new Intent(BookContent.this, BookDetail.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        bundle.putString("BookName",paraBook);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
