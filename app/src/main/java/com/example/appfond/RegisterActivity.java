package com.example.appfond;

import static com.android.volley.toolbox.Volley.newRequestQueue;
import static com.example.appfond.GlobalVariables.VERSION_NAME;
//import static com.example.appfond.BuildConfig.VERSION_NAME;
import static java.sql.DriverManager.println;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_email_field;
    private EditText reg_pass_field;
    private EditText reg_city_field;
    private EditText reg_name_field;
    private EditText reg_rep_pass_field;
    private Button reg_btn;
    private Button reg_login_btn;
    private Button canc_reg_but;
    private Button reg_with_goole;
    //private ImageButton hidePwrReg, hidePwdRep;
    private ProgressBar reg_progress;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInClient client; // Объявляем как поле класса
    private GoogleSignInClient googleSignInClient; // Объявляем клиент как поле класса

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView imageView = findViewById(R.id.SignUpPageLogo);

        // Установка изображения с закругленными углами с помощью Glide
        RequestOptions requestOptions = new RequestOptions()
                .transform(new RoundedCorners(50)); // Радиус закругления углов в пикселях

        Glide.with(this)
                .load(R.drawable.epicheck_logo) // Замените на ваш ресурс изображения
                .apply(requestOptions)
                .into(imageView);

        // Настройка параметров авторизации
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso); // Инициализируем клиент

        // 2. Проверка существующей авторизации
        //checkExistingSignIn();

        // Создание клиента для авторизации
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        reg_email_field = (EditText) findViewById(R.id.field_signup_email);
        reg_name_field = (EditText) findViewById(R.id.field_name);
        reg_pass_field = (EditText) findViewById(R.id.field_signup_password);
        reg_rep_pass_field = (EditText) findViewById(R.id.field_repeat_password);
        reg_city_field = (EditText) findViewById(R.id.field_city);
        reg_btn = (Button) findViewById(R.id.btn_create_acc);
        canc_reg_but = findViewById(R.id.buttonCancelCreate);
        reg_login_btn = (Button) findViewById(R.id.btn_back_login);
        reg_progress = (ProgressBar) findViewById(R.id.signup_progress);
        reg_with_goole = (Button) findViewById(R.id.buttonSignGoogleAuth);

        //hidePwrReg = (ImageButton) findViewById(R.id.buttonHideRegPwd);
        //hidePwdRep = (ImageButton) findViewById(R.id.buttonHideRepPwd);

        /*hidePwrReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalVariables.HIDE_PWD) {
                    reg_pass_field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    GlobalVariables.HIDE_PWD = true;
                } else {
                    reg_pass_field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    GlobalVariables.HIDE_PWD = false;
                }
            }
        });

        hidePwdRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalVariables.HIDE_PWD) {
                    reg_rep_pass_field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    GlobalVariables.HIDE_PWD = true;
                } else {
                    reg_rep_pass_field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    GlobalVariables.HIDE_PWD = false;
                }
            }
        });*/

        canc_reg_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //closeKeyboard();

                String email = reg_email_field.getText().toString();
                String pass = reg_pass_field.getText().toString();
                String pass_rep = reg_rep_pass_field.getText().toString();
                String city = reg_city_field.getText().toString();
                String name = reg_name_field.getText().toString();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)
                    && !TextUtils.isEmpty(pass_rep) && pass.equals(pass_rep) && email.contains("@") && email.contains(".")
                    && pass.length()>=6 && city.length()>=2 && name.length()>=2){
                    reg_progress.setVisibility(View.VISIBLE);
                    //create user
                    CreateUser(email, name, city, pass);
                    //LoginUserWithGoogle(email, name, city, pass, "0");
                    //CheckUser(email, VERSION_NAME,"Android");

                    //CheckUser(email);
                   // Toast.makeText(RegisterActivity.this, "user: " + MainActivity.currentUser.toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(RegisterActivity.this, "user: " + MainActivity.User_id.toString(), Toast.LENGTH_SHORT).show();
                    //success create user
                    reg_progress.setVisibility(View.INVISIBLE);
                    /*if(!TextUtils.isEmpty(MainActivity.User_id)){
                        sendToMain();
                    }else{
                        String errorMessage = "Ошибка регистрации"; //get error message from json
                        Toast.makeText(RegisterActivity.this, "Error: " + errorMessage + "user: " + MainActivity.currentUser.toString(), Toast.LENGTH_SHORT).show();
                    }*/
                    reg_progress.setVisibility(View.INVISIBLE);

                } else {
                    //error create acc
                    //Toast.makeText(RegisterActivity.this, "There are some errors", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext())
                            //set icon
                            .setIcon(R.drawable.epi_check_logo_sm)
                            //set title
                            .setTitle(R.string.textAttention)
                            //set message
                            .setMessage(R.string.textErrorCreateAccPwd)
                            //set positive button
                            .setPositiveButton(R.string.textOK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            //set negative button
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();

                }

            }
        });

        reg_with_goole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Проверка авторизованного пользователя
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(RegisterActivity.this);
                if (account != null) {
                    updateUI(account); // Пользователь уже авторизован
                } else {
                    startSignInIntent(); // Показываем форму авторизации
                }
            }
        });

        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

    }


    private void CreateUser(final String email, final String fullname, final String city, final String password){

        // RequestQueue mRequestQueue = newRequestQueue(RegisterActivity.this);
        mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
        // Progress
        String finaltype_request = "register";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;

        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    System.out.println("message create user=" + message);
                    if (message.equals("0")) {

                        MainActivity.currentUser = email;
                        SaveSettings("current_email", MainActivity.currentUser);
                        System.out.println("VERSION_NAME=" + VERSION_NAME);
                        CheckUser(email, VERSION_NAME,"Android");
                        Toast.makeText(RegisterActivity.this, getString(R.string.textSuccessReg), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(RegisterActivity.this, getString(R.string.textErrorCreateAcc), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);
                params.put("email", email);
                params.put("fullname", fullname);
                params.put("city", city);
                params.put("password", password);
                params.put("os","An_"+GlobalVariables.languageApp);
                params.put("lang",GlobalVariables.languageApp);
                params.put("currentversion", VERSION_NAME);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    /*private void CreateUser(final String email, final String versionApp, final String os) {
        mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
        // Progress
        String finaltype_request = "check_user";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("check user response=" + response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    //println("response=" + response);
                    String message = jsonObject.getString("message");

                    println("message=" + message);
                    if (message.equals("1")) {
                        MainActivity.User_id = jsonObject.getString("userId");
                        SaveSettings("userId", MainActivity.User_id.toString());
                        MainActivity.currentUser = email;
                        SaveSettings("email", MainActivity.currentUser.toString());
                        MainActivity.is_super = jsonObject.getString("super");
                        SaveSettings("super", MainActivity.is_super.toString());
                        MainActivity.fullname_user = jsonObject.getString("fullname");
                        SaveSettings("fullname", MainActivity.fullname_user);
                        MainActivity.image_link = jsonObject.getString("image");
                        SaveSettings("image", MainActivity.image_link);
                        MainActivity.user_city = jsonObject.getString("city");
                        SaveSettings("city",MainActivity.user_city);
                        MainActivity.count_cards = jsonObject.getString("count_cards");
                        SaveSettings("count_cards", MainActivity.count_cards.toString());

                        SaveSettings("userIdentifier", MainActivity.user_identifier_token.toString());

                        if (MainActivity.count_cards.equals("0")){
                            createCard(MainActivity.User_id, getString(R.string.textNullPatientName),
                                    getString(R.string.textNotDetermDiag),
                                    "", "2000-01-01");
                        }

                        sendToMain();
                    }

                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this,R.string.textErrorCheckData,Toast.LENGTH_LONG).show();
                    System.out.println("err=" + e.toString());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterActivity.this,R.string.textErrorCheckData,Toast.LENGTH_LONG).show();
                System.out.println("err=" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);
                params.put("email",email);
                params.put("currentversion",versionApp);
                params.put("os",os);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
        println(mStringRequest.toString());
    }*/


    public void CheckUser(final String email, final String versionApp, final String os){

        mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
        // Progress
        String finaltype_request = "check_user"; //login
        //String finaltype_request = "register"; //login
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("check user response=" + response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println("response=" + response);
                    String message = jsonObject.getString("message");

                    println("message=" + message);
                    if (message.equals("1")) {
                        MainActivity.User_id = jsonObject.getString("userId");
                        SaveSettings("userId", MainActivity.User_id.toString());
                        MainActivity.currentUser = email;
                        SaveSettings("email", MainActivity.currentUser.toString());
                        MainActivity.is_super = jsonObject.getString("super");
                        SaveSettings("super", MainActivity.is_super.toString());
                        MainActivity.fullname_user = jsonObject.getString("fullname");
                        SaveSettings("fullname", MainActivity.fullname_user);
                        MainActivity.image_link = jsonObject.getString("image");
                        SaveSettings("image", MainActivity.image_link);
                        MainActivity.user_city = jsonObject.getString("city");
                        SaveSettings("city",MainActivity.user_city);
                        MainActivity.count_cards = jsonObject.getString("count_cards");
                        SaveSettings("count_cards", MainActivity.count_cards.toString());

                        String value_identifier = Optional.ofNullable(MainActivity.user_identifier_token).orElse("0");

                        SaveSettings("userIdentifier", value_identifier);

                        if (MainActivity.count_cards.equals("0")){
                            createCard(MainActivity.User_id, getString(R.string.textNullPatientName),
                                    getString(R.string.textNotDetermDiag),
                                    "", "2000-01-01");
                        }else{
                            GlobalVariables.globalCardId = Integer.parseInt(jsonObject.getString("card_id"));
                        }

                        sendToMain();
                    }

                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this,getString(R.string.textErrorCheckData),Toast.LENGTH_LONG).show();
                    System.out.println("err=" + e.toString());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterActivity.this,getString(R.string.textErrorCheckData),Toast.LENGTH_LONG).show();
                System.out.println("err=" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);
                params.put("email",email);
                params.put("currentversion",versionApp);
                params.put("os","An_"+GlobalVariables.languageApp);
                params.put("lang",GlobalVariables.languageApp);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
        println(mStringRequest.toString());


    }

    /**
     * Enables https connections
     */



    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void sendToCreateCard() {
        Intent mainIntent = new Intent(RegisterActivity.this, NewCardActivity.class);
        startActivity(mainIntent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account); // Обновление UI после успешной авторизации
        } catch (ApiException e) {
            Log.w("GoogleSignIn", getString(R.string.textErrorMain) + ": " + e.getStatusCode());
            //updateUI(null);
        }
    }


    public void createCard(String user_id, String name_card, String name_diagnosis, String comm, String birthday){

        //progressBarN.setVisibility(View.VISIBLE);
        mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
        // Progress
        //String finaltype_request = "check_user";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_CREATE_CARD_NEW;
        //String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    println("message=" + message);
                    if (message.equals("0")) {

                        sendToMain();

                    }

                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this,getString(R.string.textErrorCheckData) + e.toString(),Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterActivity.this,getString(R.string.textErrorCheckData) +error.toString(),Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("name_card",name_card);
                params.put("birthday",birthday);
                params.put("diag",name_diagnosis);
                params.put("comment",comm);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    protected void onStart() {
        super.onStart();


    }

    /*private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }*/


    private void startSignInIntent() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //if (account != null) {
        // updateUI(account); // Пользователь уже авторизован
        //}
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            String tmpName = account.getDisplayName();
            String tmpEmail = account.getEmail();
            String tmpToken = account.getId();
            MainActivity.user_identifier_token = tmpToken;
            String tmpCity = getString(R.string.enter_city);
            String tmpPwd = "signinwithgoogle";

            LoginUserWithGoogle(tmpEmail, tmpName, tmpCity, tmpPwd, tmpToken);

            Toast.makeText(this,  getString(R.string.enterGoogleSuccess)+": " + tmpName, Toast.LENGTH_SHORT).show();
        }
    }

    private void LoginUserWithGoogle(final String email, final String fullname, final String city, final String password,
                                     final String userIdentifier){

        // RequestQueue mRequestQueue = newRequestQueue(RegisterActivity.this);
        mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
        // Progress
        String finaltype_request = "register";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;

        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("response=" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    System.out.println("message create user=" + message);
                    if (message.equals("0")) {

                        MainActivity.currentUser = email;
                        SaveSettings("current_email", MainActivity.currentUser);
                        System.out.println("VERSION_NAME=" + VERSION_NAME);
                        CheckUser(email, VERSION_NAME,"Android");
                            /*if (MainActivity.count_cards.equals("0")){
                                createCard(MainActivity.User_id, getString(R.string.textNullPatientName),
                                        getString(R.string.textNotDetermDiag),
                                        "", "2000-01-01");
                            }*/





                        //Toast.makeText(LoginActivity.this, R.string.textSuccessReg, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(RegisterActivity.this, getString(R.string.textErrorCreateAcc), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);
                params.put("email", email);
                params.put("fullname", fullname);
                params.put("city", city);
                params.put("password", password);
                params.put("os","An_"+GlobalVariables.languageApp);
                params.put("lang",GlobalVariables.languageApp);
                params.put("currentversion", VERSION_NAME);
                params.put("userIdentifier", userIdentifier);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    /*private void CreateUserNew(final String email, final String fullname, final String city, final String password,
                                     final String userIdentifier){

        // RequestQueue mRequestQueue = newRequestQueue(RegisterActivity.this);
        mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
        // Progress
        String finaltype_request = "register";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;

        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("response=" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    System.out.println("message create user=" + message);
                    if (message.equals("0")) {

                        MainActivity.currentUser = email;
                        SaveSettings("current_email", MainActivity.currentUser);
                        System.out.println("VERSION_NAME=" + VERSION_NAME);
                        CheckUser(email, VERSION_NAME,"Android");
                            if (MainActivity.count_cards.equals("0")){
                                createCard(MainActivity.User_id, getString(R.string.textNullPatientName),
                                        getString(R.string.textNotDetermDiag),
                                        "", "2000-01-01");
                            }





                        //Toast.makeText(LoginActivity.this, R.string.textSuccessReg, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(RegisterActivity.this, R.string.textErrorCreateAcc, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);
                params.put("email", email);
                params.put("fullname", fullname);
                params.put("city", city);
                params.put("password", password);
                params.put("os","Android");
                params.put("lang",GlobalVariables.languageApp);
                params.put("currentversion", VERSION_NAME);
                params.put("userIdentifier", userIdentifier);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }*/





    public void SaveSettings (String setting, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.nameSettings,Context.MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putString(setting, value);
        //myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
        myEdit.commit();
    }


}