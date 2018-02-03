package com.preview.android.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.preview.android.R;
import com.preview.android.base.BaseFragment;
import com.preview.android.event.EventCenter;
import com.preview.android.view.ProgressWebView;
import com.preview.android.view.TitleView;

/**
 * A simple {@link Fragment} subclass.
 * @author ZGH
 */

public class WebViewFragment extends BaseFragment {
    private String title = "";
    private ProgressWebView webView;


    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance(String title) {
        Bundle args = new Bundle();
        if (title != null) {
            args.putString("title",title);
        }
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initContentView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    protected void getBundleExtras(Bundle arguments) {
        if (arguments != null) {
            title = arguments.getString("title");
        }
    }

    @Override
    protected void initView(View view) {
        TitleView titleView = view.findViewById(R.id.titleView_fragment);
        if (titleView != null && !TextUtils.isEmpty(title)) {
            titleView.setCenterText(title);
        }

        webView = view.findViewById(R.id.webView);
        webView.loadUrl("https://app.c-ampm.com/");

        webView.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                webView.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    protected void onEventComing(EventCenter event) {

    }

}
