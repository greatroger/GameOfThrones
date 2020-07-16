package com.example.gameofthrones;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;


public class MusicContent extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ProgressDialog progressDialog;
    private TextView title;
    private TextView text;
    private ImageView cover;
    private TextView time;
    private ListView song_list;
    private BaseAdapter mAdapter=null;
    private String detail;
    private Album album;
    private Album.Music song;
    private String musicId;
    private String[] menus = {SlideMenuUtil.ITEM_One,SlideMenuUtil.ITEM_Two,
            SlideMenuUtil.ITEM_Three,SlideMenuUtil.ITEM_Four,
            SlideMenuUtil.ITEM_Five,SlideMenuUtil.ITEM_Six,
            SlideMenuUtil.ITEM_Seven,SlideMenuUtil.ITEM_Eight};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_content);
        setSlideMenu();
        album=Album.getInstance("第一季");
        setAlbumView();
    }

    private void setLayout(){
        title=(TextView)findViewById(R.id.musicTitle);
        title.setText(album.albumName);
        text=(TextView)findViewById(R.id.text4);
        text.setText("发行日期：");
        cover=(ImageView)findViewById(R.id.musicCover);
        cover.setImageResource(album.albumCover);
        time=(TextView)findViewById(R.id.musicTime);
        time.setText(album.releaseTime);
    }

    private void setAlbumView(){
        Log.e("season",album.albumName);
        showProgress("页面加载中");//开始加载动画
        if (!album.haveLoaded) {
            album.haveLoaded=true;
            new Thread() {
                public void run() {
                    try {
                        detail = GetData.getJson("https://api.imjad.cn/cloudmusic/?type=album&id=" + album.albumId);
                        DataAnalysis.MusicListInfo(detail, album);
                        handler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        album.haveLoaded=false;
                    }
                }

                ;
            }.start();
        }
        else
        {
            handler.sendEmptyMessage(1);
        }
    }

    //用于刷新页面
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            removeProgress();//当加载结束时移除动画


            setLayout();
            mAdapter = new MusicListAdapter(MusicContent.this,album.songList);
            song_list=(ListView)findViewById(R.id.list_music);
            song_list.setAdapter(mAdapter);
            song_list.setOnItemClickListener(MusicContent.this);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        song=album.songList.get(position);
            musicId = song.songId;
            new Thread() {
                public void run() {
                    try {
                        detail = GetData.getJson("https://api.imjad.cn/cloudmusic/?type=song&id=" + musicId);
                        DataAnalysis.GetSongUrl(detail, song);
                        songHandler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                ;
            }.start();
    }

    //用于刷新页面
    //@SuppressLint("HandlerLeak")
    private final Handler songHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(MusicContent.this, MusicPlaying.class);
            intent.putExtra("songOb",new Gson().toJson(song));
            startActivity(intent);
        }
    };

    private void setSlideMenu() {
// 包含TextView的LinearLayout
        LinearLayout menuLinerLayout = (LinearLayout) findViewById(R.id.linearLayoutMenu);
        menuLinerLayout.setOrientation(LinearLayout.HORIZONTAL);
        // 参数设置
        LinearLayout.LayoutParams menuLinerLayoutParames = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1);
        menuLinerLayoutParames.gravity = Gravity.CENTER_HORIZONTAL;

        SlideMenuUtil.initMusic();
        // 添加TextView控件
        for (int i = 0; i < menus.length; i++) {
            TextView tvMenu = new TextView(this);
            tvMenu.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
            tvMenu.setPadding(30, 14, 30, 10);
            tvMenu.setText(menus[i]);
            tvMenu.setTextColor(Color.WHITE);
            tvMenu.setClickable(true);
            tvMenu.setGravity(Gravity.CENTER_HORIZONTAL);
            tvMenu.setTag(menus[i]);
            SlideMenuUtil.albumMenu.add(tvMenu);
            menuLinerLayout.addView(tvMenu, menuLinerLayoutParames);
            final int finalI = i;
            tvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String menuTag = v.getTag().toString();
                    Log.e("menuTag: ",menuTag);
                    if (v.isClickable()) {
                        // 点击菜单时改变内容
                        slideMenuOnChange(menuTag, finalI);
                    }
                }
            });
        }
        SlideMenuUtil.albumMenu.get(0).setTextColor(Color.parseColor("#080808"));
    }


    // 点击时改内容
    private void slideMenuOnChange(String menuTag,int position){
        TextView clickTv=SlideMenuUtil.albumMenu.get(position);
        for (int i = 0; i < menus.length; i++) {
            SlideMenuUtil.albumMenu.get(i).setTextColor(Color.WHITE);
        }
        clickTv.setTextColor(Color.parseColor("#080808"));
        album=Album.getInstance(menuTag);
        setAlbumView();
    }

    private void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MusicContent.this, ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);//设置点击不消失
        }
        if (progressDialog.isShowing()) {
            progressDialog.setMessage(message);
        } else {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }
    //------取消ProgressDialog
    private void removeProgress(){
        if (progressDialog==null){
            return;
        }
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
