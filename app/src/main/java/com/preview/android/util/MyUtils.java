package com.preview.android.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.util.List;

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

}
