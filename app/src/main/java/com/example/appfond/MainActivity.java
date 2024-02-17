package com.example.appfond;

import static com.example.appfond.BuildConfig.VERSION_CODE;
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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public static Integer isShowAllPosts = 0;

    public static Integer isShowRealTer = 0;
    public static String currentUser;
    public static String User_id = "0";
    public static String is_super;
    public static String fullname_user;
    public static String user_city;
    public static String image_link;
    public static String count_cards;
    public static String URL_NEED_HELP = "";
    public static String URL_GET_FEEDBACK = "";
    public static String URL_APPSTORE = "";
    public static String URL_GET_ROOT_TMP = "";
    public static String main_text_about = null;
    public static String main_text_contacts = null;
    public static Integer from_add = 0;

    public static File pdffile;

    public static Integer countMainPost = 0;
    public static Integer showPayWall = 0;
    public static Integer isCheckVersion = 1;
    public static Float lastVersion;
    public static String[] DiagVal = null;
    public static List<Diagnos> diag_values;
    public static  String val1 = null;

    private Toolbar mainToolbar;
    private FloatingActionButton addPostBtn;
    private BottomNavigationView mainbottomNav;
    private HomeFragment homeFragment;
    private AboutFragment aboutFragment;
    private ProfileFragment profileFragment;
    private ContactsFragment contactsFragment;
    private DiagnosFragment diagnosFragment;

    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;
    private String infodev = "";

    public static Integer is_login = 0;

    public static String nameSettings = "EpiCheckSettings";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = findViewById(R.id.main_toolbar);


        setSupportActionBar(mainToolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("EpiCheck");

        //mainToolbar.inflateMenu(R.menu.main_menu);


        //main menu
        mainbottomNav = findViewById(R.id.mainBottomNav);

        //fragments
        homeFragment = new HomeFragment();
        aboutFragment = new AboutFragment();
        profileFragment = new ProfileFragment();
        contactsFragment = new ContactsFragment();
        diagnosFragment = new DiagnosFragment();



        mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_action_home:
                        replaceFragment(homeFragment);
                        return true;
                    /*case R.id.bottom_action_contacts:
                        replaceFragment(contactsFragment);
                        return true;
                    case R.id.bottom_action_about:
                        replaceFragment(aboutFragment);
                        return true;*/
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
            }
        });

        addPostBtn = findViewById(R.id.add_post_btn);
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPostIntent = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(newPostIntent);
            }
        });

        replaceFragment(homeFragment);

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (from_add == 0) {
            //get global params
            getGlobalParams();

            SharedPreferences sh = getSharedPreferences(nameSettings, Context.MODE_PRIVATE);
            //MyAppFondSettings
            // The value will be default as empty string because for
            // the very first time when the app is opened, there is nothing to show
            String isLogin = sh.getString("current_email", "");
            //int a = sh.getInt("age", 0);
            if (isLogin.equals("")) {
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
                                String token = task.getResult();

                                // Log and toast
                                Log.d(TAG, token);
                               // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                            }
                        });

                currentUser = isLogin;
                user_city = sh.getString("city", "");
                count_cards = sh.getString("count_cards", "0");

                fullname_user = sh.getString("fullname", "");
                is_super = sh.getString("super", "0");
                image_link = sh.getString("image", "");
                User_id = sh.getString("userId", null);

            }
        } else {
            from_add = 0;
            replaceFragment(diagnosFragment);
            mainbottomNav.setSelectedItemId(R.id.bottom_action_diag);
        }

    }

    private void getGlobalParams() {
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        // Progress
        String finaltype_request = "get_param";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_GET_PARAMS;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    //MainActivity.URL_GET_ROOT_TMP = jsonObject.getString("url_root");
                    countMainPost = Integer.parseInt(jsonObject.getString("default_days_post"));
                    //showPayWall = Integer.parseInt(jsonObject.getString("show_paywall"));
                    isCheckVersion = Integer.parseInt(jsonObject.getString("check_update_app"));
                    //MainActivity.URL_GET_FEEDBACK = jsonObject.getString("link_question");
                    //Toast.makeText(MainActivity.this, MainActivity.URL_GET_FEEDBACK.toString(),Toast.LENGTH_LONG).show();
                    //MainActivity.URL_NEED_HELP = jsonObject.getString("link_pay");
                    //MainActivity.URL_APPSTORE = jsonObject.getString("link_appstore");
                    lastVersion = Float.valueOf(jsonObject.getString("versionID_EpiCheck_Android"));


                    Float vn = Float.valueOf(BuildConfig.VERSION_NAME);

                    if ((lastVersion > vn) && (isCheckVersion == 1)) {
                        AlertDialog alertDialogDel = new AlertDialog.Builder(MainActivity.this)
                                //set icon
                                .setIcon(R.drawable.epicheck_logo)
                                //set title
                                .setTitle("Внимание")
                                //set message
                                .setMessage("Опубликована новая версия приложения! Обновимся прямо сейчас?")
                                //set positive button
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //set what would happen when positive button is clicked
                                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        }
                                    }
                                })
                                //set negative button
                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //set what should happen when negative button is clicked
                                        //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                    }
                                })
                                .show();
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


                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    //@SuppressLint("RestrictedApi")
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      if (!MainActivity.User_id.equals("0")) {

          if(menu instanceof MenuBuilder){
              MenuBuilder m = (MenuBuilder) menu;
              m.setOptionalIconsVisible(true);
          }

            getMenuInflater().inflate(R.menu.main_menu, menu);

            /*MenuItem item = (MenuItem) menu.findItem(R.id.action_cnange_pwd_btn);
            item.setIcon(R.drawable.key);
            GetTextInfoDev();*/
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
                        .setIcon(R.drawable.epicheck_logo)
                        //set title
                        .setTitle("Информация")
                        //set message
                        .setMessage("Вы действительно хотите выйти из приложения?")
                        //set positive button
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                MainActivity.User_id = "0";
                                currentUser = null;
                                SaveSettings(true);
                                sendToMain();
                                //finish();
                            }
                        })
                        //set negative button
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
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

            case R.id.action_del_acc:
                AlertDialog alertDialogDel = new AlertDialog.Builder(this)
                        //set icon
                        .setIcon(R.drawable.warning)
                        //set title
                        .setTitle("Информация")
                        //set message
                        .setMessage("Вы действительно хотите удалить учетную запись? Это действие необратимо!")
                        //set positive button
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                postDeleteAccount();
                                //finish();
                            }
                        })
                        //set negative button
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();


                return true;

            case R.id.action_info:
                if (TextUtils.isEmpty(infodev)) {
                    GetTextInfoDev();
                }
                AlertDialog alertDialogInfo = new AlertDialog.Builder(this)
                        //set icon
                        .setIcon(R.drawable.epicheck_logo)
                        //set title
                        .setTitle("Информация")
                        //set message
                        .setMessage(infodev)
                        //set positive button
                        .setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                //finish();
                            }
                        })
                        //set negative button
                        /*.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                            }
                        })*/
                        .show();


                return true;

            default:
                return false;
        }

    }

    private void sendToMain() {
        replaceFragment(homeFragment);
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
        }
        myEdit.commit();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    private void GetTextInfoDev() {

        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        // Progress
        String finaltype_request = "infodev";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_GET_TEXT;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String value = jsonObject.getString("value");
                    infodev = value;

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Ошибка при получении данных :(", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Ошибка при получении данных :(", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);

    }



}