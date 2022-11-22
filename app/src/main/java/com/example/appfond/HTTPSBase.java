package com.example.appfond;

import android.app.Application;

public class HTTPSBase extends Application {
    public String URL_ROOT = "https://vhost268072.cpsite.ru/appfond";
    public String URL_LOGIN_APP = URL_ROOT + "/api.php";
    public String URL_GETDEFPOSTS = URL_ROOT + "/getdefposts.php";
    public Integer User_id;

    public String get_URL_LOGIN_APP() {
        return URL_LOGIN_APP;
    }
}
