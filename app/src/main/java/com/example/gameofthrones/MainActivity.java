package com.example.gameofthrones;

import android.content.Intent;
import android.graphics.Color;


import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private DrawerLayout drawerLayout;
    private ListView list_left_drawer;
    private ArrayList<NaviItem> menuLists;
    private PrimAdapter<NaviItem> myAdapter = null;
    private Button button;
    private ActionBarDrawerToggle drawerbar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initLayout();
        initToolBar();
        initEvent();
    }

    public void initLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置菜单内容之外其他区域的背景色
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        //左边抽屉
        list_left_drawer = (ListView) findViewById(R.id.list_left_drawer);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    //设置开关监听
    private void initEvent() {
        drawerbar = new ActionBarDrawerToggle(this, drawerLayout,toolbar , R.string.open, R.string.close) {
            //菜单打开
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            // 菜单关闭
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerbar.setDrawerIndicatorEnabled(true);
        drawerbar.syncState();
    }

    private void init(){
        list_left_drawer = (ListView) findViewById(R.id.list_left_drawer);

        menuLists = new ArrayList<NaviItem>();
        menuLists.add(new NaviItem(R.mipmap.family,"家族"));
        menuLists.add(new NaviItem(R.mipmap.book,"书籍"));
        menuLists.add(new NaviItem(R.mipmap.music,"音乐"));
        myAdapter = new PrimAdapter<NaviItem>(menuLists,R.layout.navibar_item) {
            @Override
            public void bindView(ViewHolder holder, NaviItem obj) {
                holder.setImageResource(R.id.img_icon,obj.getIconId());
                holder.setText(R.id.txt_content, obj.getIconName());
            }
        };
        list_left_drawer.setAdapter(myAdapter);
        list_left_drawer.setOnItemClickListener(this);
    }

    //左边菜单开关事件
    public void openLeftLayout() {
        if (drawerLayout.isDrawerOpen(list_left_drawer)) {
            drawerLayout.closeDrawer(list_left_drawer);
        } else {
            drawerLayout.openDrawer(list_left_drawer);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch(position){
            case 0:
                intent=new Intent(this,FamilyItem.class);
                startActivity(intent);
                break;
            case 1:
                intent=new Intent(this,BookContent.class);
                startActivity(intent);
                break;
            case 2:
                intent=new Intent(this,MusicContent.class);
                startActivity(intent);
                break;
        }
    }
}
