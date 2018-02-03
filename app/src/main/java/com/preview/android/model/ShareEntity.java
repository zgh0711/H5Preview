package com.preview.android.model;

/**
 * Created by jirh on 2017/6/23.
 */

public class ShareEntity {
    public String title;
    public String shoreUrl;
    public String intro;
    public String pic;

    public ShareEntity(String title, String shoreUrl, String intro, String pic) {
        this.title = title;
        this.shoreUrl = shoreUrl;
        this.intro = intro;
        this.pic = pic;
    }
}
