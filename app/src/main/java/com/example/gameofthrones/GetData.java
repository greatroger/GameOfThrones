package com.example.gameofthrones;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetData {
    //以httpUrlConnection的方式获取api数据，并返回流（适用于XML）
    public static InputStream getXML(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        //conn.setRequestMethod("GET");
        int code=conn.getResponseCode();
        if (code == 200) {
            InputStream in = conn.getInputStream();
            return in;
        }
        else{
            Log.d("DisplayXML","error!!! :");
        }
        return null;
    }
    //以httpUrlConnection的方式获取api数据，并返回字符（适用于json）
    public static String getJson(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        //conn.setRequestMethod("GET");
        int code=conn.getResponseCode();
        if (code == 200) {
            InputStream in = conn.getInputStream();
            byte[] data = read(in);
            String apiData = new String(data, "UTF-8");
            return apiData;
        }
        else{
            InputStream in = conn.getInputStream();
            byte[] data = read(in);
            String json = new String(data, "UTF-8");
            Log.d("Json","error!!! :"+json);
        }
        return null;
    }

    //从流中读取数据
    public static byte[] read(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer,0,len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
