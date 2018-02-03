package com.preview.android.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import com.blankj.utilcode.util.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author ZGH
 * @date 2017/5/4
 */

public class ImageUtil {

    public static File creatFile(String fileName) {
        String filePath = MyUtils.FOLDER + File.separator + fileName;
        //先创建文件夹
        if (FileUtils.createOrExistsDir(MyUtils.FOLDER)) {
            FileUtils.createOrExistsFile(filePath);
        }

        if (FileUtils.isFileExists(filePath)) {
            return FileUtils.getFileByPath(filePath);
        } else {
            return null;
        }
    }

    /**
     * 保存bitmap到file,并返回
     */
    public static File saveBitmapToFile(Bitmap bm, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 将图片文件添加至相册（便于浏览）
     * @param file
     * @param context
     */
    public static void addToGallery(File file, Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }


    /**
     * 根据地址获取网络图片
     * @param sUrl
     * @return
     */
    public static Bitmap getBitmap(String sUrl) {
        try {
            URL url = new URL(sUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        if (base64Data.startsWith("data:image/png;base64,")) {
            base64Data = base64Data.replace("data:image/png;base64,", "");
        }
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * bitmap 保存为jpg 图片
     *
     * @param mBitmap  图片源
     * @param fileName 图片名
     */
    public static void saveMyBitmap(Context context,Bitmap mBitmap, String fileName) {
        File file = ImageUtil.creatFile(fileName);
        FileOutputStream fOut = null;

        if (file != null) {
            try {
                fOut = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageUtil.addToGallery(file, context);
        }

    }
}
