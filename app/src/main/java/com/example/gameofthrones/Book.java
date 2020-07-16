package com.example.gameofthrones;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Book {
    public static Map<String,Book> instance=new HashMap<>();

    public String bookName;
    public String bookID;
    public String oriName;
    public ArrayList<String> translator;
    public String summary;
    public String releaseTime;
    public String rating;
    public String author;
    public ArrayList<BookDetail.Item> povCharacters;
    public String position;
    public ArrayList<String>povUrl;
    public String translators;
    public int img;
    public boolean haveLoaded;
    public boolean povLoaded;

    private Book(String bookName){
        this.bookName=bookName;
        setBookStaticinfo(bookName);
        haveLoaded=false;
        povLoaded=false;
        this.translator=new ArrayList<String>();
        this.povCharacters=new ArrayList<BookDetail.Item>();
        this.povUrl=new ArrayList<String>();
    }

    public static Book getInstance(String bookName){
        if(instance.get(bookName)==null){
            instance.put(bookName,new Book(bookName));
        }
        return instance.get(bookName);
    }

    private void setBookStaticinfo(String bookName) {
        switch(bookName){
            case "权力的游戏":
                this.bookID="6964050";
                this.position="1";
                this.img= R.mipmap.cover1;
                break;
            case "列王的纷争":
                this.bookID="10488620";
                this.position="2";
                this.img= R.mipmap.cover2;
                break;
            case "冰雨的风暴":
                this.bookID="10608468";
                this.position="3";
                this.img= R.mipmap.cover3;
                break;
            case "群鸦的盛宴":
                this.bookID="11628196";
                this.position="5";
                this.img= R.mipmap.cover4;
                break;
            case "魔龙的狂舞":
                this.bookID="20381804";
                this.position="8";
                this.img= R.mipmap.cover5;
                break;
        }
    }

    public String bindTrans(){
        int num=this.translator.size();
        for (int i=0;i<num;++i){
            translators=translators+"/"+this.translator.get(i);
        }
        translators=translators.substring(5);
        return translators;
    }
}
