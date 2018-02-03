package com.preview.android.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.preview.android.R;
import com.preview.android.constant.Urls;
import com.preview.android.model.ShareEntity;

import java.io.File;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 *
 * @author ZGH
 * @date 2017/12/11
 */

public class MyUtils {
    public static final String FOLDER =
            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
            "AMhaigou" + File.separator + "picture";

    /**
     * 判断手机上是否安装了某个应用
     */
    public static boolean isAppInstalled(Context context,String pkgName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(pkgName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 一键分享功能
     */
    public static void showShare(final Context context, final ShareEntity entity) {
        final OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        /**
         * 下面的这些参数必须要写，某些不写会导致某些平台分享失败
         */
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(entity.title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(entity.shoreUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(entity.intro);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数,
        // 使用 imagePath 必须保证SDcard下面存在此张图片
        //imagePath,imageUrl 必须保留一个，否则微信不能分享，或者分享过去的图片都是应用的 logo
        oks.setImageUrl(entity.pic);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(entity.shoreUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(entity.shoreUrl);
        oks.setImageArray(new String[]{Urls.BASE_URL + entity.pic});

        /**
         * 真正分享出去的内容实际上是由下面的这些参数决定的，根据平台不同分别配置
         */
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if (Wechat.NAME.equals(platform.getName()) ||
                    WechatMoments.NAME.equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    paramsToShare.setUrl(entity.shoreUrl);
                    paramsToShare.setText(entity.intro);
                    paramsToShare.setTitle(entity.title);
                    paramsToShare.setImageUrl(entity.pic);
                }
                if (QQ.NAME.equals(platform.getName())) {
                    paramsToShare.setTitle(entity.title);
                    paramsToShare.setTitleUrl(entity.shoreUrl);
                    paramsToShare.setText(entity.intro);
                    paramsToShare.setUrl(entity.shoreUrl);
                    paramsToShare.setImageUrl(entity.pic);
                }
            }
        });

        //启动分享GUI
        oks.show(context);
    }
}
