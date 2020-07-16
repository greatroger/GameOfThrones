package com.example.gameofthrones;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicListAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Album.Music> data;
    private static LayoutInflater inflater=null;

    public MusicListAdapter(Activity a, ArrayList<Album.Music> d){
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.listitem_music, null);
        }
        TextView title = (TextView)vi.findViewById(R.id.songName); // 歌名
        TextView singer = (TextView)vi.findViewById(R.id.singer); // 歌手名
        ImageView icon=(ImageView)vi.findViewById(R.id.icon_play) ;
        Album.Music music=new Album.Music();
        music = data.get(position);
        title.setText(music.songName);
        singer.setText(music.singer);
        icon.setImageResource(R.mipmap.play1);
        return vi;
    }
}
