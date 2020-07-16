package com.example.gameofthrones;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;


public class FamilyItem extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ProgressDialog progressDialog;
    private GridView grid_photo;
    private BaseAdapter mAdapter=null;
    private InputStream detail;
    private InputStream picDetail;
    private Family family;
    public String paraName;
    private String[] menus = {SlideMenuUtil.ITEM_Stark,SlideMenuUtil.ITEM_Lannister,
            SlideMenuUtil.ITEM_Baratheon,SlideMenuUtil.ITEM_Targaryen,
            SlideMenuUtil.ITEM_Tully,SlideMenuUtil.ITEM_Arryn,
            SlideMenuUtil.ITEM_Tyrell,SlideMenuUtil.ITEM_Martell,
            SlideMenuUtil.ITEM_Greyjoy};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_family);
        setSlideMenu();
        family=Family.getInstance("史塔克");
        setFamilyView();
    }

    private void setFamilyView() {
        showProgress("页面加载中");//开始加载动画
        if (!family.haveLoaded) {
            family.haveLoaded = true;
            new Thread() {
                public void run() {
                    try {
                        detail = GetData.getXML("http://asoiaf.huijiwiki.com/api.php?action=query&format=xml&formatversion=2&list=categorymembers&cmtitle=Category:" + family.familyName + "家族&cmlimit=500");
                        DataAnalysis.GetTop10(detail, family);
                        for (int i = 0; i < 16; ++i) {
                            String name = family.top10Person.get(i).title;
                            picDetail = GetData.getXML("http://asoiaf.huijiwiki.com/api.php?action=query&format=xml&formatversion=2&prop=pageimages&titles=" + name + "&pithumbsize=500");
                            DataAnalysis.pageImgAnalysis(picDetail, name, family);
                        }
                        handler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        family.haveLoaded = false;
                    }
                };
            }.start();
        }
        else{
            handler.sendEmptyMessage(1);
        }
    }

    //用于刷新页面
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            removeProgress();//当加载结束时移除动画
            mAdapter = new MyAdapter(FamilyItem.this,family.pages);
            grid_photo=findViewById(R.id.grid_photo);
            grid_photo.setAdapter(mAdapter);
            grid_photo.setOnItemClickListener(FamilyItem.this);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        paraName=family.pages.get(position).getText();
        Log.e("webview名字：",paraName);
        Intent intent = new Intent(FamilyItem.this, WikiView.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        bundle.putString("WikiName",paraName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

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


        SlideMenuUtil.initFamily();
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
            SlideMenuUtil.familyMenu.add(tvMenu);
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
        SlideMenuUtil.familyMenu.get(0).setTextColor(Color.parseColor("#080808"));
    }

    // 点击时改内容
    private void slideMenuOnChange(String menuTag,int position){
        TextView clickTv=SlideMenuUtil.familyMenu.get(position);
        for (int i = 0; i < menus.length; i++) {
            SlideMenuUtil.familyMenu.get(i).setTextColor(Color.WHITE);
        }
        clickTv.setTextColor(Color.parseColor("#080808"));
        family=Family.getInstance(menuTag);
        setFamilyView();
    }

    private void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(FamilyItem.this, ProgressDialog.STYLE_SPINNER);
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
