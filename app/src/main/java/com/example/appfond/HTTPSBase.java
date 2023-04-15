package com.example.appfond;

import android.app.Application;

public class HTTPSBase extends Application {

    //public String URL_ROOT = "https://vhost268072.cpsite.ru/appfond";
    public String URL_ROOT = "https://appfondsod.ru";
    public String URL_LOGIN_APP = URL_ROOT + "/api.php";
    public String URL_GETDEFPOSTS = URL_ROOT + "/getdefposts.php";
    public String URL_GETALLPOSTS = URL_ROOT + "/getallposts.php";
    public String URL_UPLOAD_IMG_PROFILE = URL_ROOT + "/upload_img_android.php";
    public String URL_GET_TEXT = URL_ROOT + "/gettext.php";
    public String URL_CHN_PWD = URL_ROOT + "/api_profile.php";
    public String URL_GET_PARAMS = URL_ROOT + "/getglobalparams.php";
    public String URL_GET_CARDS = URL_ROOT + "/cards/api_cards.php";
    public String URL_EDIT_CARD = URL_ROOT + "/cards/edit_card.php";
    public String URL_CREATE_CARD = URL_ROOT + "/cards/create_card.php";
    public String URL_DEL_CARD = URL_ROOT + "/cards/del_card.php";
    public String URL_CREATE_EPISODE = URL_ROOT + "/cards/create_episode.php";
    public String URL_GET_DIAGNOSIS = URL_ROOT + "/cards/get_diagnosis.php";
    public String URL_GET_HISTORY = URL_ROOT + "/cards/get_episodes.php";
    public String URL_GET_HISTORY_CHART = URL_ROOT + "/cards/get_episodes_chart.php";
    public String URL_DEL_EPISODE = URL_ROOT + "/cards/del_episode.php";
    public String URL_GET_COLL = URL_ROOT + "/posts/get_collect.php";

    public String URL_GET_DELTERAPHY = URL_ROOT + "/cards/del_teraphy.php";
    public String URL_GET_TERAPHY_REAL = URL_ROOT + "/cards/api_teraphy_real.php";
    public String URL_GET_TERAPHY_ARCH = URL_ROOT + "/cards/api_teraphy.php";
    public String URL_GET_EDTERAPHY = URL_ROOT + "/cards/edit_teraphy.php";
    public String URL_NEW_TERAPHY = URL_ROOT + "/cards/new_teraphy.php";

    public Integer User_id;

    public String get_URL_LOGIN_APP() {
        return URL_LOGIN_APP;
    }

    public String get_URL_CHN_PWD() {
        return URL_CHN_PWD;
    }
}
