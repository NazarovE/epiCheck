package com.example.appfond;



/*

Add new language:
1.
epiCheck -> app -> src -> main -> res : New -> Anroid Resource Directory:
Resource type: values
Available qualifiers: выберите Locale → нужный язык/регион
В созданной папке создайте файл strings.xml:
values/strings.xml (основной язык)

2. ProfileFragment.java 2 places
-switch (GlobalVariables.languageApp)
-String lang = "en";
    switch (position) {
3. Add new language to database (event_text or something)
4. Add new tags to all xml files with new Land, exmaple
<string name="textLanSweden">Ruotsi</string> (better throught open editor for all translation )
5. Add new tag to
epiCheck -> app -> src -> main -> res -> values - arrays.xml
example
<string name="textLanSweden">Ruotsi</string>
 */

//import static com.example.appfond.BuildConfig.VERSION_CODE;@
import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;
import static java.sql.DriverManager.println;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
//import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MainActivity extends AppCompatActivity implements OnActivityRefreshListener    {

    public static Integer isShowAllPosts = 0;

    public static Integer isShowRealTer = 0;
    public static String currentUser;
    public static String User_id = "0";
    public static String is_super;
    public static String fullname_user;
    public static String user_city;
    public static String image_link;
    public static String count_cards;
    public static String user_identifier_token;
    public static String URL_NEED_HELP = "";
    public static String URL_GET_FEEDBACK = "";
    public static String main_text_about = null;
    public static String main_text_contacts = null;
    public static Integer from_add = 0;

    public static File pdffile;

    public static Integer countMainPost = 0;

    public static Integer isCheckVersion = 1;


    private BottomNavigationView mainbottomNav;
    private HomeFragment homeFragment;
    //private AboutFragment aboutFragment;
    private ProfileFragment profileFragment;
    //private ContactsFragment contactsFragment;
    private DiagnosFragment diagnosFragment;

    private FixFragment fixFragment;

    private HistoryFragment historyFragment;

    private TeraphyFragment teraphyFragment;

    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;
    //private BottomSheetDialogFragment eventFragment;


    public static Integer is_login = 0;

    public static String nameSettings = "EpiCheckSettings";

    private List<GlobalSettings> global_settings;
    GlobalSettings globalSetting;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        println("я тут MA - onCreate");
        setContentView(R.layout.activity_main);

        handleSSLHandshake();

        // Загрузить сохраненный язык
        println("я тут MA - onCreate - getSavedLanguage");
        getSavedLanguage();

        try {
            // Получение PackageManager
            PackageManager packageManager = getPackageManager();

            // Получение имени пакета текущего приложения
            String packageName = getPackageName();

            // Получение информации о пакете
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);

            // Наименование приложения (label)
            //String appName = packageManager.getApplicationLabel(getApplicationInfo()).toString();

            // Версия приложения
            String versionName = packageInfo.versionName;

            // Версия кода приложения (integer)
            //int versionCode = packageInfo.versionCode;

            GlobalVariables.VERSION_NAME = versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        global_settings = new ArrayList<>();

        setSupportActionBar(mainToolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("EpiCheck");

        //mainToolbar.inflateMenu(R.menu.main_menu);
        //progressBarMainForm = findViewById(R.id.progressBarMain);

        //main menu
        mainbottomNav = findViewById(R.id.mainBottomNav);

        // 2. Получаем его меню
        Menu menu = mainbottomNav.getMenu();



// 3. Обновляем все элементы
        menu.findItem(R.id.bottom_action_diag).setTitle(getString(R.string.res_diag_menu));
        menu.findItem(R.id.bottom_action_history).setTitle(getString(R.string.res_menu_history));
        menu.findItem(R.id.bottom_action_fix).setTitle(getString(R.string.res_menu_fix));
        menu.findItem(R.id.bottom_action_teraphy).setTitle(getString(R.string.res_menu_teraphy));
        menu.findItem(R.id.bottom_action_profile).setTitle(getString(R.string.bottom_profile_text));

        //mainbottomNav.setItemIconTintList(ColorStateList.valueOf(R.drawable.epickek_round_sm));
        mainbottomNav.setItemIconTintList(null);

        //fragments
        homeFragment = new HomeFragment();
        //aboutFragment = new AboutFragment();
        profileFragment = new ProfileFragment();
        //contactsFragment = new ContactsFragment();
        diagnosFragment = new DiagnosFragment();
        historyFragment = new HistoryFragment();
        fixFragment = new FixFragment();
        teraphyFragment = new TeraphyFragment();


        mainbottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_action_teraphy:
                    replaceFragment(teraphyFragment);
                    return true;
                case R.id.bottom_action_fix:
                    replaceFragment(fixFragment);
                    return true;
                case R.id.bottom_action_history:
                    replaceFragment(historyFragment);
                    return true;
                case R.id.bottom_action_diag:
                    replaceFragment(diagnosFragment);
                    return true;
                case R.id.bottom_action_profile:
                    replaceFragment(profileFragment);
                    return true;

                default:
                    replaceFragment(homeFragment);
                    return true;
            }
        });

        ImageButton fabButton = findViewById(R.id.fab_button);
        fabButton.setOnClickListener(v -> {
            // Действие для центральной кнопки
            replaceFragment(fixFragment);
        });

        FloatingActionButton addPostBtn = findViewById(R.id.add_post_btn);
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPostIntent = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(newPostIntent);
            }
        });

        SharedPreferences sh = getSharedPreferences(nameSettings, Context.MODE_PRIVATE);
        User_id = sh.getString("userId", "0");
        //System.out.println("User_id=" + User_id);



        replaceFragment(diagnosFragment);

    }

    @SuppressLint("TrulyRandom")
    private void handleSSLHandshake() {
        try {
            @SuppressLint("CustomX509TrustManager") TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType)
                        throws CertificateException {
                    if (certs == null || certs.length == 0) {
                        throw new CertificateException("Нет сертификатов для проверки");
                    }
                    // Базовая проверка срока действия
                    for (X509Certificate cert : certs) {
                        cert.checkValidity();
                    }
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());

            HostnameVerifier defaultVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HttpsURLConnection.setDefaultHostnameVerifier(
                    (hostname, session) -> defaultVerifier.verify(hostname, session)
            );

        } catch (Exception ignored) {
        }
    }


    private void isSignedIn() {
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        // Progress
        String finaltype_request = "check_user";
        String emailforrequest = MainActivity.currentUser;


        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    GlobalVariables.wasLatestEvent = Integer.valueOf(jsonObject.getString("was_last_event"));
                    GlobalVariables.image_profile = jsonObject.getString("image");

                    // Создаем ScheduledExecutorService с одним потоком
                    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

                    // Задаем задачу, которую нужно выполнить по истечении 5 секунд
                    scheduler.schedule(() -> {
                        if (GlobalVariables.wasLatestEvent.equals(0)) {
                            EventDialogFragment eventDialogFragment = new EventDialogFragment();
                            eventDialogFragment.show(getSupportFragmentManager(), "EventDialog");
                        }
                    }, 5, TimeUnit.SECONDS);



                    // Закрываем планировщик через какое-то время, если больше нет задач
                    scheduler.shutdown();



                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, R.string.textErrorDescr, Toast.LENGTH_LONG).show();

                }

            }
        }, error -> Toast.makeText(MainActivity.this, R.string.textErrorDescr, Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);
                params.put("email", emailforrequest);
                params.put("os", GlobalVariables.strOsVer);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //println("я тут MA - onStart");
        if (from_add == 0) {
            //get global params
            //GetTextInfoDev();
            getGlobalParamsNew2();


            SharedPreferences sh = getSharedPreferences(nameSettings, Context.MODE_PRIVATE);
            //MyAppFondSettings
            // The value will be default as empty string because for
            // the very first time when the app is opened, there is nothing to show
            String isLogin = sh.getString("current_email", "");
            //int a = sh.getInt("age", 0);
            if (isLogin.isEmpty()) {
                //sendToLogin();
                is_login = 0;
            } else {
                is_login = 1;

                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                GlobalVariables.token = task.getResult();

                                // Log and toast
                                Log.d(TAG, GlobalVariables.token);
                               // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                            }
                        });

                currentUser = isLogin;
                user_city = sh.getString("city", "");
                count_cards = sh.getString("count_cards", "0");

                fullname_user = sh.getString("fullname", "");
                is_super = sh.getString("super", "0");
                image_link = sh.getString("image", "");
                User_id = sh.getString("userId", "0");
                GlobalVariables.globalCardId = Integer.parseInt(sh.getString("card_id","0"));

                isSignedIn();

            }
        } else if (from_add == 1) {
            //from_add = 0;
            replaceFragment(diagnosFragment);
            mainbottomNav.setSelectedItemId(R.id.bottom_action_diag);
        } else if (from_add == 2) {
            from_add = 0;
            replaceFragment(historyFragment);
            mainbottomNav.setSelectedItemId(R.id.bottom_action_history);
        } else if (from_add == 3) {
            from_add = 0;
            replaceFragment(teraphyFragment);
            mainbottomNav.setSelectedItemId(R.id.bottom_action_teraphy);
        } else if (from_add == 4) {
            from_add = 0;
            replaceFragment(profileFragment);
            mainbottomNav.setSelectedItemId(R.id.bottom_action_profile);
        } else if (from_add == 5) {
            from_add = 0;
            replaceFragment(homeFragment);
        } else if (from_add == 6) {
            from_add = 0;
            replaceFragment(fixFragment);
        }else {
            from_add = 0;
            replaceFragment(profileFragment);
            mainbottomNav.setSelectedItemId(R.id.bottom_action_profile);
        }

    }

    private void getGlobalParamsNew2() {

        //progressBarMainForm.setVisibility(View.VISIBLE);

        //progressBarMainForm.post(() -> {
            HTTPSBase Global = new HTTPSBase();
            String url = Global.URL_GET_PARAMS_NEW;
            //startRequest(); // ВЕСЬ Volley-код сюда
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                           // progressBarMainForm.setVisibility(View.INVISIBLE);
                            global_settings.clear();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("globalparams");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("name");
                                    String value = object.getString("value");

                                    switch (name) {
                                        case "default_days_post":
                                            countMainPost = Integer.parseInt(value);
                                            break;
                                        case "check_update_app":
                                            isCheckVersion = Integer.parseInt(value);
                                            break;
                                        case "versionID_EpiCheck_Android":
                                            GlobalVariables.lastVersion = Float.parseFloat(value);
                                            break;
                                        case "last_event":
                                            GlobalVariables.lastEventText = value;
                                            break;
                                        case "id_event":
                                            GlobalVariables.id_event = Integer.parseInt(value);
                                            break;
                                    }

                                    globalSetting = new GlobalSettings(name, value);
                                    global_settings.add(globalSetting);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Float vn = Float.valueOf(GlobalVariables.VERSION_NAME);

                            if ((GlobalVariables.lastVersion > vn) && (isCheckVersion == 1)) {
                                AlertDialog alertDialogDel = new AlertDialog.Builder(MainActivity.this)
                                        //set icon
                                        .setIcon(R.drawable.epickek_round_sm)
                                        //set title
                                        .setTitle(R.string.textAttention)
                                        //set message
                                        .setMessage(R.string.textUpdateApp)
                                        //set positive button
                                        .setPositiveButton(R.string.textYes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //set what would happen when positive button is clicked
                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                            }
                                        })
                                        //set negative button
                                        .setNegativeButton(R.string.textNo, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //set what should happen when negative button is clicked
                                                //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .show();
                            }


                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            //progressBarMainForm.setVisibility(View.INVISIBLE);

                            if (error instanceof TimeoutError) {
                                onRequestTimeout();
                            } else {
                                onRequestError();
                            }
                        }
                    }
            );

            // 🔹 Таймаут 8 секунд, без повторов
            request.setRetryPolicy(new DefaultRetryPolicy(
                    8000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(request);
        //});




    }

    private void onRequestTimeout() {
        //Toast.makeText(this, "Таймаут соединения", Toast.LENGTH_LONG).show();
        sendToNoConnect();
        // например: повтор запроса или оффлайн-режим
    }

    private void onRequestError() {
        //Toast.makeText(this, "Ошибка загрузки данных", Toast.LENGTH_LONG).show();
        sendToNoConnect();
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

      if (!MainActivity.User_id.equals("0")) {

          if(menu instanceof MenuBuilder){
              MenuBuilder m = (MenuBuilder) menu;
              m.setOptionalIconsVisible(true);
          }

            getMenuInflater().inflate(R.menu.main_menu, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout_btn:
                //logout code
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        //set icon
                        .setIcon(R.drawable.epickek_round_sm)
                        //set title
                        .setTitle(R.string.textAttention)
                        //set message
                        .setMessage(R.string.textOutAccount)
                        //set positive button
                        .setPositiveButton(R.string.textYes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                MainActivity.User_id = "0";
                                GlobalVariables.globalCardId = 0;
                                currentUser = null;
                                SaveSettings(true);
                                //sendToMain();
                                sendToReg();
                                //finish();
                            }
                        })
                        //set negative button
                        .setNegativeButton(R.string.textNo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                //Toast.makeText(getApplicationContext(), "Nothing Happened", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();


                return true;

            case R.id.action_cnange_pwd_btn:
                Intent profileIntent = new Intent(MainActivity.this, ChangePwdActivity.class);
                startActivity(profileIntent);
                //finish();
                return true;

            case R.id.action_blog:
                Intent blogIntent = new Intent(MainActivity.this, BlogActivity.class);
                startActivity(blogIntent);
                return true;

            case R.id.action_del_acc:
                AlertDialog alertDialogDel = new AlertDialog.Builder(this)
                        //set icon
                        .setIcon(R.drawable.warning)
                        //set title
                        .setTitle(R.string.textAttention)
                        //set message
                        .setMessage(R.string.textDelAccount)
                        //set positive button
                        .setPositiveButton(R.string.textYes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                postDeleteAccount();
                                //finish();
                            }
                        })
                        //set negative button
                        .setNegativeButton(R.string.textCancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();


                return true;

            case R.id.action_info:

                Intent fbIntent = new Intent(MainActivity.this, FeedBackActivity.class);
                startActivity(fbIntent);

                return true;

            default:
                return false;
        }

    }

    
    

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_blog);
        if (item != null) {
                item.setVisible(GlobalVariables.languageApp.equals("ru")); // Показывать только если HideItem = 0
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void sendToReg() {
        Intent regIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(regIntent);
        finish();
    }

    private void sendToMain() {
        replaceFragment(diagnosFragment);
    }

    private void postDeleteAccount() {

        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        // Progress
        String finaltype_request = "delete_account";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    println("message=" + message);
                    if (message.equals("0")) {
                        currentUser = null;
                        SaveSettings(true);
                        sendToLogin();
                    }

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);
                params.put("email", MainActivity.currentUser);
                params.put("fullname", MainActivity.fullname_user);
                params.put("city", MainActivity.user_city);
                params.put("userid", MainActivity.User_id);
                System.out.println("User_id=" + User_id);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void sendToNoConnect() {
        Intent loginIntent = new Intent(MainActivity.this, NoConnectActivity.class);
        startActivity(loginIntent);
        finish();
    }

    public void SaveSettings(Boolean isOut) {
        SharedPreferences sharedPreferences = getSharedPreferences(nameSettings, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        if (isOut == false) {
            myEdit.putString("current_email", MainActivity.currentUser);
        } else {
            myEdit.putString("current_email", null);
            myEdit.putString("super", null);
            myEdit.putString("fullname", null);
            myEdit.putString("image", null);
            myEdit.putString("city", null);
            myEdit.putString("count_cards", null);
            myEdit.putString("userId", null);
            myEdit.putString("email", null);
            myEdit.putString("userIdentifier", null);
        }
        myEdit.apply();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    public String getSystemLanguage() {
        // Получаем текущую локаль системы
        Locale systemLocale = Locale.getDefault();
        // Возвращаем код языка (например, "ru", "en", "fr")
        return systemLocale.getLanguage();
    }



    public void getSavedLanguage() {
        //SharedPreferences preferences = context.getSharedPreferences(nameSettings, Context.MODE_PRIVATE);

        SharedPreferences sharedPreferences = getSharedPreferences(nameSettings, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        String tmpLan = sharedPreferences.getString("Language", "1"); // Возвращает сохраненный язык или пустую строку, если язык не выбран


        if (tmpLan.equals("1")) {
            String language = getSystemLanguage();
            //Log.d("SystemLanguage", "Current system language: " + language);
            tmpLan = language;
            myEdit.putString("Language", tmpLan);
            myEdit.apply();

        }

        GlobalVariables.languageApp = tmpLan;
        GlobalVariables.strOsVer = "An_" + tmpLan;
        setAppLocale(this, tmpLan);

    }



    public void setAppLocale(Context context, String languageCode) {
        // Создаем объект Locale для нового языка
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        // Получаем ресурсы и конфигурацию
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();

        // Устанавливаем новую локаль
        config.setLocale(locale);

        // Обновляем конфигурацию
        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }

    @Override
    public void onRefresh() {
        // Код для обновления активности
        recreate();
    }

}