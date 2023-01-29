package com.example.appfond;

import static com.example.appfond.BuildConfig.VERSION_NAME;
import static java.sql.DriverManager.println;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    private Button loginRegBtn;
   // private String currentUser;
    private ProgressBar loginProgress;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmailText = (EditText) findViewById(R.id.field_signup_email);
        loginPassText = (EditText) findViewById(R.id.field_signup_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        loginRegBtn = (Button) findViewById(R.id.btn_to_create_acc);
        loginProgress = (ProgressBar) findViewById(R.id.signup_progress);

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
                    Toast.makeText(LoginActivity.this,"Необходимо Заполнить email и пароль!",Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void sendToMain() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
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

                    String message = jsonObject.getString("message");
                    println("message=" + message);
                    if (message.equals("0")) {
                        MainActivity.currentUser = email;
                        SaveSettings("current_email", MainActivity.currentUser);
                       // Toast.makeText(LoginActivity.this,"Login success",Toast.LENGTH_SHORT).show();
                        System.out.println("VERSION_NAME=" + VERSION_NAME);
                        CheckUser(email, VERSION_NAME,"Android");
                        sendToMain();
                    }

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,"Ошибка! Проверьте введенные данные",Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this,"Ошибка! Проверьте введенные данные",Toast.LENGTH_LONG).show();

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

    public void CheckUser(final String email, final String versionApp, final String os){

        mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
        // Progress
        String finaltype_request = "check_user";
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
                    }

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,"Ошибка! Проверьте введенные данные",Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this,"Ошибка! Проверьте введенные данные",Toast.LENGTH_LONG).show();

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
    }

    public void SaveSettings (String setting, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppFondSettings",Context.MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putString(setting, value);
        //myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
        myEdit.commit();
    }




}