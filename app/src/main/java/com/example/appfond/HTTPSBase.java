package com.example.appfond;

import android.app.Application;

public class HTTPSBase extends Application {

    //public String URL_ROOT = "https://vhost268072.cpsite.ru/appfond";
    public String URL_ROOT = "https://appfondsod.ru";
    public String URL_LOGIN_APP = URL_ROOT + "/api.php";
    public String URL_GETDEFPOSTS = URL_ROOT + "/getdefposts.php";
    public String URL_UPLOAD_IMG_PROFILE = URL_ROOT + "/upload_img_android.php";
    public String URL_GET_TEXT = URL_ROOT + "/gettext.php";

    public String URL_GET_PARAMS = URL_ROOT + "/getglobalparams.php";

    public Integer User_id;

    public String get_URL_LOGIN_APP() {
        return URL_LOGIN_APP;
    }
}
