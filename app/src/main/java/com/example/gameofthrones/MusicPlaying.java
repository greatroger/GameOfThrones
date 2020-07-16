package com.example.gameofthrones;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MusicPlaying extends AppCompatActivity implements View.OnClickListener {
    private ImageView discsmap;
    private ImageView pauseIv;
    private MediaPlayer mPlayer;
    private ObjectAnimator animator;
    private TextView currentTv;
    private TextView totalTv;
    private Integer temTime;  //用getDuration函数得到的音乐时长
    private String totalTime;  //经过format后可读性强的音乐时长
    private SeekBar jindutiaoSb;
    private TextView songName;
    private boolean isStop;
    private boolean isPause;
    private Album.Music song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String songJson = getIntent().getStringExtra("songOb");
        song = new Gson().fromJson(songJson, Album.Music.class);
        setContentView(R.layout.activity_music_playing);
        isStop = false;
        Link();
        play();
        rotatePlayer();
        otherOperation();
        jindutiaoSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void play() {

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(song.songUrl);
            mPlayer.prepare();
            temTime = mPlayer.getDuration();
            totalTime = formatTime(temTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.start();
    }

    private void Link() {
        discsmap = findViewById(R.id.listen_changpian_img);
        pauseIv = findViewById(R.id.listen_pause1_img);
        currentTv = findViewById(R.id.listen_current_tv);
        totalTv = findViewById(R.id.listen_length_tv);
        jindutiaoSb = findViewById(R.id.listen_jindutiao_sb);
        songName=findViewById(R.id.musicName);
        songName.setText(song.songName);

        pauseIv.setOnClickListener(this);
    }

    private void otherOperation() {
        totalTv.setText(totalTime);
        new Thread(new SeekBarThread()).start();
        jindutiaoSb.setMax(temTime);
    }

    private void rotatePlayer() {
        ///////////////////////////////////唱片打碟/////////////////////////////////////////////
        animator = ObjectAnimator.ofFloat(discsmap, "rotation", 0f, 360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//设置动画重复次数（-1代表一直转）
        animator.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
        animator.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listen_pause1_img:
                if (isPause == false) {
                    isPause=true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        animator.pause();
                    }
                    mPlayer.pause();
                    pauseIv.setImageResource(R.mipmap.play1);
                } else {
                    isPause=false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        animator.resume();
                    }
                    mPlayer.start();
                    pauseIv.setImageResource(R.mipmap.pause);
                }
                break;
        }
    }

    class SeekBarThread implements Runnable {

        @Override
        public void run() {
            while (mPlayer != null && isStop == false) {
                // 将SeekBar位置设置到当前播放位置
                handler.sendEmptyMessage(mPlayer.getCurrentPosition());
                try {
                    // 每100毫秒更新一次位置
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    //接受多线程信息
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            jindutiaoSb.setProgress(msg.what);
            currentTv.setText(formatTime(msg.what));

        }
    };

    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String TotalTime = simpleDateFormat.format(date);
        return TotalTime;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPlayer.reset();
        mPlayer.release();
        isStop=true;
        return;
    }
}
