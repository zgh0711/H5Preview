package com.preview.android.model;

/**
 * Created by ZGH on 2017/9/3.
 */

public class ApiEntity {

    /**
     * code : 0
     * data : {"adimg":null,"adurl":null,"hideGuide":true,"ioschecking":true,"ioscode":null,"iosurl":null,"vercode":null,"verurl":null}
     * time : 1504427973
     */

    private int code;
    private DataEntity data;
    private int        time;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static class DataEntity {
        private String  adimg;
        private String  adurl;
        private String  androidchecking;
        private String  androidcode;
        private String  androidurl;
        private String  baseurl;
        private String  find;
        private String  home;
        private String  ioschecking;
        private String  iosurl;
        private String  me;
        private String  quiz;
        private String  shop;

        public String getAdimg() {
            return adimg;
        }

        public void setAdimg(String adimg) {
            this.adimg = adimg;
        }

        public String getAdurl() {
            return adurl;
        }

        public void setAdurl(String adurl) {
            this.adurl = adurl;
        }

        public String getAndroidchecking() {
            return androidchecking;
        }

        public void setAndroidchecking(String androidchecking) {
            this.androidchecking = androidchecking;
        }

        public String getAndroidcode() {
            return androidcode;
        }

        public void setAndroidcode(String androidcode) {
            this.androidcode = androidcode;
        }

        public String getAndroidurl() {
            return androidurl;
        }

        public void setAndroidurl(String androidurl) {
            this.androidurl = androidurl;
        }

        public String getBaseurl() {
            return baseurl;
        }

        public void setBaseurl(String baseurl) {
            this.baseurl = baseurl;
        }

        public String getFind() {
            return find;
        }

        public void setFind(String find) {
            this.find = find;
        }

        public String getHome() {
            return home;
        }

        public void setHome(String home) {
            this.home = home;
        }

        public String getIoschecking() {
            return ioschecking;
        }

        public void setIoschecking(String ioschecking) {
            this.ioschecking = ioschecking;
        }

        public String getIosurl() {
            return iosurl;
        }

        public void setIosurl(String iosurl) {
            this.iosurl = iosurl;
        }

        public String getMe() {
            return me;
        }

        public void setMe(String me) {
            this.me = me;
        }

        public String getQuiz() {
            return quiz;
        }

        public void setQuiz(String quiz) {
            this.quiz = quiz;
        }

        public String getShop() {
            return shop;
        }

        public void setShop(String shop) {
            this.shop = shop;
        }
    }
}
