package com.example.gameofthrones;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WikiView extends AppCompatActivity {
    private WebView webView;
    private long exitTime = 0;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        final String consultName = bundle.getString("WikiName");
        setContentView(R.layout.activity_wiki_view);
        webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if(url!=null) {
                    String fun="javascript:function getClass(parent,sClass) { var aEle=parent.getElementsByTagName('div'); var aResult=[]; var i=0; for(i<0;i<aEle.length;i++) { if(aEle[i].className==sClass) { aResult.push(aEle[i]); } }; return aResult; } ";
                    view.loadUrl(fun);
                    String fun2="javascript:function hideOther() {getClass(document,'navbar-container')[0].style.display='none'; \n" +
                            "getClass(document,'bottom footer')[0].style.display='none'; \n" +
                            "document.getElementById('content-actions').style.display='none';}";
                    view.loadUrl(fun2);
                    view.loadUrl("javascript:hideOther();");
                }
                super.onPageFinished(view, url);
                setContentView(webView);

            }

            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        webView.loadUrl("https://asoiaf.huijiwiki.com/wiki/"+consultName);          //调用loadUrl方法为WebView加入链接
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}
