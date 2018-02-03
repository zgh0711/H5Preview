package com.preview.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.preview.android.R;
import com.preview.android.view.X5WebView;

/**
 * @author ZGH
 */
public class ADActivity extends AppCompatActivity {

    private X5WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        mWebView = findViewById(R.id.webView_ad);
        //WebView的状态恢复
        if (savedInstanceState != null){
            mWebView.restoreState(savedInstanceState);
        }else {
            mWebView.loadUrl(url);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //WebView的状态保存
        mWebView.saveState(outState);
    }

    //改写物理返回键的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mWebView.canGoBack()){
                mWebView.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
