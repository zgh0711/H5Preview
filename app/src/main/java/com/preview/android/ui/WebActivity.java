package com.preview.android.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.Result;
import com.joker.annotation.PermissionsCustomRationale;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsNonRationale;
import com.joker.api.Permissions4M;
import com.preview.android.R;
import com.preview.android.base.BaseActivity;
import com.preview.android.constant.Urls;
import com.preview.android.event.EventCenter;
import com.preview.android.model.ShareEntity;
import com.preview.android.update.UpdataService;
import com.preview.android.util.ImageUtil;
import com.preview.android.util.MyUtils;
import com.preview.android.view.TitleView;
import com.preview.android.view.X5WebView;
import com.preview.android.zxing.DecodeImage;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZGH
 */
public class WebActivity extends BaseActivity {
    private final String      ALIPAY             = "com.eg.android.AlipayGphone";
    private final String      QQ                 = "com.tencent.mobileqq";
    private final String      JD                 = "com.jingdong.app.mall";
    public static final int   WRITE_STORAGE_CODE = 1;
    private static final long WAIT_TIME          = 2000L;
    private              long touchTime          = 0;
    private X5WebView webView;
    private TitleView titleView;
    private String shareTitle = "";
    private String shareIntro = "AM海购，让海购更嗨购";
    private String shareUrl   = "";

    private boolean isQR = false;
    private Bitmap  mBitmap = null;
    /**
     * 是二维码时，才弹出对话框
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (isQR) {
                    showDialog();
                }
            }
        }
    };


    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);
        setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), 0);
        checkUpdate();
    }

    @Override
    protected void initView() {
        initTitleView();

        //在请求页面时加入header，让服务器知道是来自哪种客户端
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("androidapp", "true");
        headers.put("App-Port", "camit.android");

        webView = findViewById(R.id.webView);
//        webView.loadUrl(Urls.BASE_URL,headers);
        webView.loadUrl("file:///android_asset/CreditCard/index.html");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //如果是支付宝开头的链接则判断是否安装了支付宝，有的话就打开支付宝支付
                if (url.startsWith("alipays")) {
                    isAppInstalled(view, url, ALIPAY);
                } else if (url.startsWith("mqq")) {
                    isAppInstalled(view, url, QQ);
                } else if (url.startsWith("openapp.jd")) {
                    isAppInstalled(view, url, JD);
                } else {
                    webView.loadUrl(url,headers);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                shareUrl = webView.getUrl();
                LogUtils.d("2",shareUrl);

                String title = webView.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    shareTitle = title;
                    titleView.setCenterText(title);
                }
            }
        });

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //获取所点击的内容
                final WebView.HitTestResult htr = webView.getHitTestResult();
                //判断被点击的类型为图片
                if (htr.getType() == WebView.HitTestResult.IMAGE_TYPE) {
                    requestPermission();
                    // 获取到图片地址后做相应的处理
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            isQR = decodeImage(htr.getExtra());
                            handler.sendEmptyMessage(0);
                        }
                    }).start();
                }
                return false;
            }
        });
    }

    /**
     * 请求读写存储卡权限
     */
    private void requestPermission() {
        Permissions4M.get(WebActivity.this)
                     .requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                     .requestCodes(WRITE_STORAGE_CODE)
                     .requestPageType(Permissions4M.PageType.ANDROID_SETTING_PAGE)
                     .request();
    }

    /**
     * 必加的二次权限申请回调,手动重新此方法以支持权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions, @NonNull int[] grantResults) {
        Permissions4M.onRequestPermissionsResult(WebActivity.this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 权限申请失败后的回调
     */
    @PermissionsDenied(WRITE_STORAGE_CODE)
    public void storageDenied() {
        ToastUtils.showShort("写入手机存储权限申请失败，无法保存图片");
    }

    /**
     * 二次权限申请前弹出的提示，此提示用于向客户解释为什么需要申请权限
     */
    @PermissionsCustomRationale(WRITE_STORAGE_CODE)
    public void customRationale() {
        new AlertDialog.Builder(WebActivity.this)
                .setMessage("您好，我们需要您开启\"读写手机存储\"权限才能保存图片，请点击确定并继续点击允许来开启权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Permissions4M.get(WebActivity.this)
                                     .requestOnRationale()
                                     .requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                     .requestCodes(WRITE_STORAGE_CODE)
                                     .request();
                    }
                })
                .show();
    }

    /**
     * 权限申请时如果用户点了拒绝，同时又点了不再提示后再次申请权限时引导用户去系统设置里面开启权限
     */
    @PermissionsNonRationale(WRITE_STORAGE_CODE)
    public void nonRationale(final Intent intent) {
        new AlertDialog.Builder(WebActivity.this)
                .setMessage("您好，我们需要您开启\"读写手机存储\"权限才能保存图片，请点击确定前往应用管理并开启\"读写手机存储\"权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.showShort("没有权限，无法保存图片");
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void isAppInstalled(WebView view, String url, String pkgName) {
        if (MyUtils.isAppInstalled(WebActivity.this, pkgName)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            view.goBack();//把跳转的中间页面关掉，对我们无用的页面
        } else if (ALIPAY.equals(pkgName)) {
            ToastUtils.showShort("请先安装手机支付宝");
            view.goBack();
        } else if (JD.equals(pkgName)) {
            ToastUtils.showShort("请先安装手机京东");
            view.goBack();
        } else {
            ToastUtils.showShort("请先安装手机QQ");
            view.goBack();
        }
    }

    /**
     * 判断是否为二维码,传入图片地址
     */
    private boolean decodeImage(String sUrl) {
        Bitmap bitmap;
        if (sUrl == null) {
            return false;
        } else if (sUrl.contains("://")) {
            bitmap = ImageUtil.getBitmap(sUrl);
        } else {
            bitmap = ImageUtil.base64ToBitmap(sUrl);
        }

        if (bitmap == null) {
            return false;
        } else {
            mBitmap = bitmap;
        }

        Result result = DecodeImage.handleQRCodeFormBitmap(bitmap);
        return result != null;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否保存图片");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mBitmap != null) {
                    String fileName = "QR_" + System.currentTimeMillis() + ".jpg";
                    String filePath = MyUtils.FOLDER + File.separator + fileName;
                    ImageUtil.saveMyBitmap(WebActivity.this, mBitmap, fileName);
                    if (FileUtils.isFileExists(filePath)) {
                        ToastUtils.showShort("保存成功,请到系统相册中查看该二维码");
                    }
                }
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    /**
     * 检查是否需要更新
     */
    private void checkUpdate() {
        int localVer = AppUtils.getAppVersionCode();
        int remoteVer = Urls.VER_Code;
        if (remoteVer > localVer) {
            update();
        }
    }

    private void update() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("发现新版本，是否更新");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(Urls.VER_URL)) {
                    UpdataService.startService(WebActivity.this, Urls.VER_URL);
                }
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void initTitleView() {
        titleView = findViewById(R.id.titleView);
        titleView.getCenter_tv().setSingleLine();
        titleView.getRight_tv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }

    private void share() {
        ShareEntity share;
        // TODO: 需要一个图片地址和文字介绍
        String imgUrl = "http://www.baidu.com";
        if (!TextUtils.isEmpty(shareTitle) && !TextUtils.isEmpty(shareUrl)) {
            share = new ShareEntity(shareTitle, shareUrl, shareIntro, imgUrl);
            MyUtils.showShare(WebActivity.this, share);
        } else {
            ToastUtils.showShort("内容不全，无法分享");
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void onEventComing(EventCenter event) {

    }

    @Override
    public void onBackPressedSupport() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if (System.currentTimeMillis() - touchTime < WAIT_TIME) {
                this.finish();
            } else {
                touchTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
