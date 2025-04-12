package com.example.appfond;

//import static com.example.appfond.BuildConfig.VERSION_NAME;
import static com.example.appfond.GlobalVariables.VERSION_NAME;
import static java.sql.DriverManager.println;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;



public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    private Button loginRegBtn;
   // private String currentUser;
    private ProgressBar loginProgress;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;
    private Button btnHidePwd;
    private Button btnGoogleAuth;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInClient client; // Объявляем как поле класса
    private GoogleSignInClient googleSignInClient; // Объявляем клиент как поле класса

    private String text_name_card = "";
    private String text_diag_card = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Настройка Google Sign-In
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // Запрос email
                .requestIdToken("YOUR_CLIENT_ID") // Укажите ваш Client ID
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Настройка кнопки
        //SignInButton signInButton = findViewById(R.id.buttonGoogleAuth);
        btnGoogleAuth = findViewById(R.id.buttonGoogleAuth);
        //btnGoogleAuth.setSize(SignInButton.SIZE_STANDARD);
        btnGoogleAuth.setOnClickListener(this::onSignInButtonClicked);*/

        // Настройка параметров авторизации
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso); // Инициализируем клиент

        // 2. Проверка существующей авторизации
        //checkExistingSignIn();

        // Создание клиента для авторизации
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Кнопка входа
        Button signInButton = findViewById(R.id.buttonGoogleAuth);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Проверка авторизованного пользователя
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                if (account != null) {
                    updateUI(account); // Пользователь уже авторизован
                    //startSignInIntent();
                    //Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    //handleSignInResult(task);
                } else {
                    startSignInIntent(); // Показываем форму авторизации
                }

                //signIn();
            }
        });




        ImageView imageView = findViewById(R.id.SignUpPageLogo);

        // Установка изображения с закругленными углами с помощью Glide
        RequestOptions requestOptions = new RequestOptions()
                .transform(new RoundedCorners(50)); // Радиус закругления углов в пикселях

        Glide.with(this)
                .load(R.drawable.epicheck_logo) // Замените на ваш ресурс изображения
               // .transform(RoundedCornersTransformation(16, 0))  // 16px радиус, 0 - без обрезки
                .apply(requestOptions)
                .into(imageView);

        loginEmailText = (EditText) findViewById(R.id.field_signup_email);
        loginPassText = (EditText) findViewById(R.id.field_signup_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        loginRegBtn = (Button) findViewById(R.id.btn_to_create_acc);
        loginProgress = (ProgressBar) findViewById(R.id.signup_progress);
        btnHidePwd = findViewById(R.id.buttonHidePwd);



        btnHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalVariables.HIDE_PWD) {
                    loginPassText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    GlobalVariables.HIDE_PWD = true;
                } else {
                    loginPassText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    GlobalVariables.HIDE_PWD = false;
                }
            }

            /*@Override
            public void onClick(View v) {
                if (GlobalVariables.HIDE_PWD) {
                    // Показать пароль
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    GlobalVariables.HIDE_PWD = false;
                    btnHidePwd.setImageResource(R.drawable.ic_eye_open); // Иконка "глаз открыт"
                } else {
                    // Скрыть пароль
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    GlobalVariables.HIDE_PWD = true;
                    btnHidePwd.setImageResource(R.drawable.ic_eye_closed); // Иконка "глаз закрыт"
                }
                // Переместить курсор в конец текста
                editTextPassword.setSelection(editTextPassword.getText().length());
            }*/

        });




        //move to create account
        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToReg();
            }
        });

        //login app AND MOVE TO MAIN
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                String loginEmail = loginEmailText.getText().toString();
                String loginPass = loginPassText.getText().toString();


                if(!TextUtils.isEmpty(loginEmail) &&
                   !TextUtils.isEmpty(loginPass)
                  ){

                    //closeKeyboard();

                    loginProgress.setVisibility(View.VISIBLE);
                    //get login
                    //sendRequestLogin(loginEmail, loginPass);
                    //sendPostLogin(loginEmail, loginPass);
                    //MainActivity.currentUser = "some user";
                    LoginUser(loginEmail, loginPass);

                    //success login
                    loginProgress.setVisibility(View.INVISIBLE);
                   /* if(MainActivity.currentUser != null){
                        //sendToMain();
                    }else{
                        String errorMessage = "something wrong"; //get error message from json
                        Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }*/

                } else {
                    Toast.makeText(LoginActivity.this,R.string.textErrorCheckData,Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    /*private void checkExistingSignIn() {
        // Проверка авторизованного пользователя
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {

        } else {
            startSignInIntent(); // Показываем форму авторизации

        }
        updateUI(account); // Пользователь уже авторизован
    }*/

    private void startSignInIntent() {
        //System.out.println("call startSignInIntent");
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //if (account != null) {
           // updateUI(account); // Пользователь уже авторизован
        //}
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*private void onSignInButtonClicked(View view) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        //System.out.println("call updateUI");
        if (account != null) {
            String tmpName = account.getDisplayName();
            String tmpEmail = account.getEmail();
            String tmpToken = account.getId();
            MainActivity.user_identifier_token = tmpToken;
            String tmpCity = getString(R.string.enter_city);
            String tmpPwd = "signinwithgoogle";

            LoginUserWithGoogle(tmpEmail, tmpName, tmpCity, tmpPwd, tmpToken);
            Toast.makeText(this,  getString(R.string.enterGoogleSuccess)+": " + tmpName, Toast.LENGTH_SHORT).show();
            //sendToMain();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account); // Обновление UI после успешной авторизации
        } catch (ApiException e) {
            Log.w("GoogleSignIn", getString(R.string.textErrorMain) + ": " + e.getStatusCode());
            //updateUI(null);@
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);

    }

    private void sendToMain() {
        //System.out.println("call sendToMain");
        MainActivity.from_add = 1;
        Intent mainIntent = new Intent(LoginActivity.this, TempActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void sendToReg() {
        Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(regIntent);
        finish();
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


    private void LoginUser(final String email, final String password){

        mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
        // Progress
        String finaltype_request = "login";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    println("response=" + response);
                    String message = jsonObject.getString("message");
                    println("message=" + message);
                    if (message.equals("0")) {
                        MainActivity.currentUser = email;
                        SaveSettings("current_email", MainActivity.currentUser);
                       // Toast.makeText(LoginActivity.this,"Login success",Toast.LENGTH_SHORT).show();
                        //System.out.println("VERSION_NAME=" + VERSION_NAME);
                        CheckUser(email, VERSION_NAME,"Android");


                    }

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,R.string.textErrorCheckData + e.getMessage(),Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this,"Ошибка! Проверьте введенные данные" + error.getMessage(),Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);
                params.put("email",email);
                params.put("password",password);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    private void LoginUserWithGoogle(final String email, final String fullname, final String city, final String password,
                                     final String userIdentifier){

        // RequestQueue mRequestQueue = newRequestQueue(RegisterActivity.this);
        //System.out.println("call LoginUserWithGoogle");
        mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
        // Progress
        String finaltype_request = "register";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;

        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("response=" + response);
                Log.d("TAG", "response=" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    //System.out.println("message create user=" + message);
                    if (message.equals("0")) {

                        MainActivity.currentUser = email;
                        SaveSettings("current_email", MainActivity.currentUser);
                        //System.out.println("VERSION_NAME=" + VERSION_NAME);
                        CheckUser(email, VERSION_NAME,"Android");
                            /*if (MainActivity.count_cards.equals("0")){
                                createCard(MainActivity.User_id, getString(R.string.textNullPatientName),
                                        getString(R.string.textNotDetermDiag),
                                        "", "2000-01-01");
                            }*/





                        //Toast.makeText(LoginActivity.this, R.string.textSuccessReg, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(LoginActivity.this, R.string.textErrorCreateAcc, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                //System.out.println("error register = " + error);

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
                params.put("currentversion", VERSION_NAME);
                params.put("userIdentifier", userIdentifier);

                return params;
            }

            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // Принимаем любой статус как успешный
                //System.out.println("I'm here...");
                return Response.success(new String(response.data), HttpHeaderParser.parseCacheHeaders(response));
            }


        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }



    public void CheckUser(final String email, final String versionApp, final String os){
        //System.out.println("call CheckUser");
        mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
        // Progress
        String finaltype_request = "check_user";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("check user response=" + response);
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
                    Toast.makeText(LoginActivity.this,R.string.textErrorCheckData,Toast.LENGTH_LONG).show();
                    //System.out.println("err=" + e.toString());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this,R.string.textErrorCheckData,Toast.LENGTH_LONG).show();
                //System.out.println("err=" + error.toString());
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


    }


    public void createCard(String user_id, String name_card, String name_diagnosis, String comm, String birthday){

        //progressBarN.setVisibility(View.VISIBLE);
        mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
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

                    //System.out.println("message=" + message);
                    if (message.equals("0")) {

                        sendToMain();

                    }

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,R.string.textErrorCheckData + e.toString(),Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this,R.string.textErrorCheckData +error.toString(),Toast.LENGTH_LONG).show();

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