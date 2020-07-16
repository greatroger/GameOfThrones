package com.example.gameofthrones;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class DataAnalysis {
    public static void GetTop10(InputStream xml, Family family) throws Exception {
        ArrayList<Characters> persons = null;
        Characters person = null;
        // 创建一个xml解析的工厂
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // 获得xml解析类的引用
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(xml, "UTF-8");
        // 获得事件的类型
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    persons = new ArrayList<Characters>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("cm".equals(parser.getName())) {
                        person = new Characters();
                        // 取出属性值
                        int pageid = Integer.parseInt(parser.getAttributeValue(0));
                        person.setId(pageid);
                        String title = parser.getAttributeValue(2);
                        person.setTitle(title);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("cm".equals(parser.getName())) {
                        persons.add(person);
                        person = null;
                    }
                    break;
            }
            eventType = parser.next();
        }

        Collections.sort(persons, new Comparator<Characters>() {
            @Override
            public int compare(Characters o1, Characters o2) {
                if (o1.pageid > o2.pageid)
                    return 1;
                else if (o1.pageid < o2.pageid)
                    return -1;
                return 0;
            }
        });

        for (int i = 0; i < persons.size(); ++i) {
            if (persons.get(i).title.equals(family.familyName + "家族")) {
                Characters iperson = persons.remove(i);
                persons.add(0, iperson);
                break;
            }

        }
        for (Iterator<Characters> it = persons.iterator(); it.hasNext();) {
            if (it.next().title.indexOf("消歧义")!= -1) {
                it.remove();
            }
        }
        for (int i = 0; i < 16; i++) {
            person = persons.get(i);
            Integer a = persons.get(i).pageid;
            Log.e("pageid：", a.toString());
            final String name = persons.get(i).title;
            Log.e("姓名：", name);
            family.top10Person.add(person);
        }
    }

    public static void pageImgAnalysis(InputStream picDetail, String name, Family family) throws Exception {
        ImageAndText page = null;
        // 创建一个xml解析的工厂
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // 获得xml解析类的引用
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(picDetail, "UTF-8");
        // 获得事件的类型
        Integer eventType = parser.getEventType();
        String imageName=null;
        String imgUrl=null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if("page".equals(parser.getName())) {
                        if(parser.getAttributeCount()==5)
                            imageName=parser.getAttributeValue(4);
                        else {
                            imageName=null;
                            imgUrl="https://huiji-public.huijistatic.com/asoiaf/uploads/4/4e/Image_placeholder2.jpg";
                        }
                    }
                    if ("thumbnail".equals(parser.getName())) {
                        imgUrl = parser.getAttributeValue(0);
                        String[] sss =imgUrl.split("/");
                        if(imageName!= null) {
                            int i = 0;
                            for (i = 0; i < sss.length; i++) {
                                if (sss[i].equals(imageName))
                                    break;
                            }
                            String base = "https://huiji-public.huijistatic.com/asoiaf/uploads/";
                            imgUrl = base + sss[i - 2] + "/" + sss[i - 1] + "/" + imageName;
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.next();
        }
        page = new ImageAndText();
        page.setText(name);
        Log.e("XML", imgUrl);
        Bitmap imgUrlBitmap = null;
        URL myFileUrl = new URL(imgUrl);
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            imgUrlBitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Bitmap imgUrlBitmap = BitmapFactory.decodeFile(imgUrl);
        page.setImageUrl(imgUrl);
        page.setBitmap(imgUrlBitmap);
        family.pages.add(page);
    }

    public static void BookInfo(String json, Book book) {
        JSONObject jsonBook = null;
        try {
            jsonBook = new JSONObject(json);
            JSONArray writer = jsonBook.getJSONArray("author");
            JSONObject rate = jsonBook.getJSONObject("rating");
            book.author = (String) writer.get(0);
            book.rating = rate.getString("average");
            book.summary = jsonBook.getString("summary");
            JSONArray translators = jsonBook.getJSONArray("translator");
            for (int i = 0; i < translators.length(); i++) {
                book.translator.add((String) translators.get(i));
            }
            book.translators=book.bindTrans();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void BookSupplement(String json, Book book) {
        JSONObject jsonBook = null;
        try {
            jsonBook = new JSONObject(json);
            book.oriName=jsonBook.getString("name");
            book.releaseTime=jsonBook.getString("released").substring(0,10);
            JSONArray povs = jsonBook.getJSONArray("povCharacters");
            for (int i=0;i<povs.length();++i){
                book.povUrl.add(povs.get(i).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void PovInfo(String json,Book book){
        JSONObject jsonBook = null;
        try{
            jsonBook = new JSONObject(json);
            book.povCharacters.add(new BookDetail.Item(jsonBook.getString("name")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void MusicListInfo(String json,Album album){
        Album.Music song=null;
        JSONObject jsonMusic = null;
        try {
            jsonMusic = new JSONObject(json);
            JSONArray songs = jsonMusic.getJSONArray("songs");
            for (int i = 0; i <songs.length() ; i++) {
                JSONObject songJson=songs.getJSONObject(i);
                song=new Album.Music();
                song.songName = songJson.getString("name");
                Log.e("songName",song.songName);
                song.songId=songJson.getString("id");
                JSONArray ar=songJson.getJSONArray("ar");
                JSONObject singer= ar.getJSONObject(0);
                song.singer=singer.getString("name");
                album.songList.add(song);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void GetSongUrl(String json,Album.Music song){
        JSONObject jsonMusic = null;
        try {
            jsonMusic = new JSONObject(json);
            JSONArray data = jsonMusic.getJSONArray("data");
            JSONObject songJson=data.getJSONObject(0);
            song.songUrl=songJson.getString("url");
            Log.e("url",song.songUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
