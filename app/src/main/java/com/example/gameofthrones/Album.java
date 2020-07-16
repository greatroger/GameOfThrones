package com.example.gameofthrones;

import android.graphics.Bitmap;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Album {
    static class Music{
        public String songName;
        public String songId;
        public String singer;
        public String songUrl;

    }
    public static Map<String,Album> instance=new HashMap<>();
    public String albumName;
    public String albumId;
    public int albumCover;
    public String releaseTime;
    public ArrayList<Music> songList;
    public boolean haveLoaded;

    private Album(String season){
        setAlbumStaticInfo(season);
        songList=new ArrayList<Music>();
        haveLoaded=false;
    }

    public static Album getInstance(String season){
        if(instance.get(season)==null){
            instance.put(season,new Album(season));
        }
        return instance.get(season);
    }

    private void setAlbumStaticInfo(String season) {
        switch(season){
            case "第一季":
                this.albumId="2543301";
                this.albumCover=R.mipmap.album1;
                this.releaseTime="2011-06-28";
                this.albumName="Game Of Thrones (Music from the HBO Series) ";
                break;
            case "第二季":
                this.albumId="189848";
                this.albumCover=R.mipmap.album2;
                this.releaseTime="2012-06-19";
                this.albumName="Game Of Thrones 2(Music from the HBO Series) ";
                break;
            case "第三季":
                this.albumId="2508189";
                this.albumCover=R.mipmap.album3;
                this.releaseTime="2013-06-04";
                this.albumName="Game Of Thrones 3(Music from the HBO Series) ";
                break;
            case "第四季":
                this.albumId="2866148";
                this.albumCover=R.mipmap.album4;
                this.releaseTime="2014-06-10";
                this.albumName="Game Of Thrones 4(Music from the HBO Series) ";
                break;
            case "第五季":
                this.albumId="3162540";
                this.albumCover=R.mipmap.album5;
                this.releaseTime="2015-06-09";
                this.albumName="Game Of Thrones 5(Music from the HBO Series) ";
                break;
            case "第六季":
                this.albumId="34746190";
                this.albumCover=R.mipmap.album6;
                this.releaseTime="2016-06-24";
                this.albumName="Game Of Thrones 6(Music from the HBO Series) ";
                break;
            case "第七季":
                this.albumId="36031331";
                this.albumCover=R.mipmap.album7;
                this.releaseTime="2017-08-25";
                this.albumName="Game Of Thrones 7(Music from the HBO Series) ";
                break;
            case "第八季":
                this.albumId="79260545";
                this.albumCover=R.mipmap.album8;
                this.releaseTime="2019-05-19";
                this.albumName="Game Of Thrones 8(Music from the HBO Series) ";
                break;
        }
    }
}
