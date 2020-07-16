package com.example.gameofthrones;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.sip.SipSession;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookDetail extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private Book book;
    private TextView summary;
    private ImageView cover;
    private TextView overview;
    private String text_result;
    private TextView title;
    private String detail;
    private String suppleDetail;
    private String povDetail;

    private ArrayList<Group> gData = null;
    public ArrayList<ArrayList<Item>> iData = null;
    private Context mContext;
    private ExpandableListView exlist_lol;
    private ExlistAdapter myAdapter = null;

    class Group{
        private String gName;
        public Group() {
        }
        public Group(String gName) {
            this.gName = gName;
        }
        public String getgName() {
            return gName;
        }
        public void setgName(String gName) {
            this.gName = gName;
        }
    }

    static class Item{
        private String iName;
        public Item() {
        }
        public Item(String iName) {
            this.iName = iName;
        }
        public String getiName() {
            return iName;
        }
        public void setiName(String iName) {
            this.iName = iName;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        final String consultName = bundle.getString("BookName");
        book=Book.getInstance(consultName);
        Log.e("bookID:",book.bookID);
        showProgress("页面加载中");//开始加载动画
        if(! book.haveLoaded) {
            new Thread() {
                public void run() {
                    book.haveLoaded = true;
                    try {
                        detail = GetData.getJson("https://api.douban.com/v2/book/" + book.bookID + "?apikey=0df993c66c0c636e29ecbb5344252a4a");
                        suppleDetail=GetData.getJson("https://anapioficeandfire.com/api/books/"+book.position);
                        DataAnalysis.BookInfo(detail,book);
                        DataAnalysis.BookSupplement(suppleDetail,book);
//                        for (int i=0;i<book.povUrl.size();++i){
//                            povDetail=GetData.getJson(book.povUrl.get(i));
//                            DataAnalysis.PovInfo(povDetail,book);
//                        }
                        handler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        book.haveLoaded = false;
                    }
                };
            }.start();
        }else {
            handler.sendEmptyMessage(1);
        }

    }

    //用于刷新页面
    private Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            removeProgress();//当加载结束时移除动画
            setContentView(R.layout.activity_book_detail);
            setLayout();
            setExlist_lol();
            //PovGetDetail();
        }
    };

    private void PovGetDetail(){
        if (!book.povLoaded){
            new Thread(){
                public void run(){
                    book.povLoaded=true;
                    try{
                        for (int i=0;i<book.povUrl.size();++i){
                            povDetail=GetData.getJson(book.povUrl.get(i));
                            DataAnalysis.PovInfo(povDetail,book);
                        }
                        povHandler.sendEmptyMessage(1);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        book.povLoaded=false;
                    }
                };
            }.start();
        }
        else{
            povHandler.sendEmptyMessage(1);
        }
    }

    private Handler povHandler=new Handler(){
        public void handleMessage(Message msg) {
            iData.clear();
            iData.add(book.povCharacters);
            myAdapter = new ExlistAdapter(gData,iData,mContext);
            exlist_lol.setAdapter(myAdapter);
        }
    };


    private void setLayout(){
        text_result="作者：" + book.author + "\n原作名：" + book.oriName + "\n译者："+book.translators+"\n发行日期："+book.releaseTime+"\n豆瓣评分："+book.rating;
        title=(TextView)findViewById(R.id.title);
        title.setText(book.bookName);
        cover=(ImageView)findViewById(R.id.img_book);
        cover.setImageResource(book.img);
        overview=(TextView)findViewById(R.id.over_view);
        overview.setText(text_result);
        summary=(TextView)findViewById(R.id.summary);
        summary.setText(book.summary);

    }

    private void setExlist_lol(){
        exlist_lol=(ExpandableListView)findViewById(R.id.exlist_lol);
        mContext = BookDetail.this;
        gData = new ArrayList<Group>();
        gData.add(new Group("POV（视角）人物"));
        iData = new ArrayList<ArrayList<Item>>();
        iData.add(new ArrayList<Item>());
        PovGetDetail();
        myAdapter = new ExlistAdapter(gData,iData,mContext);
        exlist_lol.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(exlist_lol.isGroupExpanded(0)) {
                    ViewGroup.LayoutParams params = exlist_lol.getLayoutParams();
                    params.height = 173;
                    exlist_lol.setLayoutParams(params);
                }
                else{
                    ViewGroup.LayoutParams params = exlist_lol.getLayoutParams();
                    params.height = 130 * book.povCharacters.size() + 173;
                    exlist_lol.setLayoutParams(params);
                }
                return false;
            }
        });
        exlist_lol.setAdapter(myAdapter);
    }

    private void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(BookDetail.this, ProgressDialog.STYLE_SPINNER);
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
