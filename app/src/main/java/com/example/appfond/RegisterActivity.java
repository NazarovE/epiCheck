package com.example.appfond;

import static com.android.volley.toolbox.Volley.newRequestQueue;
import static com.example.appfond.BuildConfig.VERSION_NAME;
import static java.sql.DriverManager.println;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_email_field;
    private EditText reg_pass_field;
    private EditText reg_city_field;
    private EditText reg_name_field;
    private EditText reg_rep_pass_field;
    private Button reg_btn;
    private Button reg_login_btn;
    private Button canc_reg_but;
    private ProgressBar reg_progress;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_email_field = (EditText) findViewById(R.id.field_signup_email);
        reg_name_field = (EditText) findViewById(R.id.field_name);
        reg_pass_field = (EditText) findViewById(R.id.field_signup_password);
        reg_rep_pass_field = (EditText) findViewById(R.id.field_repeat_password);
        reg_city_field = (EditText) findViewById(R.id.field_city);
        reg_btn = (Button) findViewById(R.id.btn_create_acc);
        canc_reg_but = findViewById(R.id.buttonCancelCreate);
        reg_login_btn = (Button) findViewById(R.id.btn_back_login);
        reg_progress = (ProgressBar) findViewById(R.id.signup_progress);

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
                            .setIcon(R.drawable.logo)
                            //set title
                            .setTitle("Информация")
                            //set message
                            .setMessage("Что то пошло не так при регистрации. Проверьте введенные данные. Возможно не совпадают пароли или содержат иные символы, " +
                                    "отличные от латинских букв, цифр, нижнего подчеркивания. Пароль должен быть не менее 6 символов. Email должен содержать знак @ и точки.")
                            //set positive button
                            .setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
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

        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

    }





    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
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
                        Toast.makeText(RegisterActivity.this, "create user success", Toast.LENGTH_SHORT).show();
                        sendToMain();
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
                params.put("currentversion", VERSION_NAME);

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

    public void CheckUser(final String email, final String versionApp, final String os){

        mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
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
                    Toast.makeText(RegisterActivity.this,e.toString(),Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterActivity.this,error.toString(),Toast.LENGTH_LONG).show();

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

}