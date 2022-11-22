package com.example.appfond;

import static com.android.volley.toolbox.Volley.newRequestQueue;
import static java.sql.DriverManager.println;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
    private ProgressBar reg_progress;



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
        reg_login_btn = (Button) findViewById(R.id.btn_back_login);
        reg_progress = (ProgressBar) findViewById(R.id.signup_progress);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeKeyboard();

                String email = reg_email_field.getText().toString();
                String pass = reg_pass_field.getText().toString();
                String pass_rep = reg_rep_pass_field.getText().toString();
                String city = reg_city_field.getText().toString();
                String name = reg_name_field.getText().toString();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)
                    && !TextUtils.isEmpty(pass_rep) && pass.equals(pass_rep)){
                    reg_progress.setVisibility(View.VISIBLE);
                    //create user
                    CreateUser(email, name, city, pass, pass_rep);

                    //success create user
                    reg_progress.setVisibility(View.INVISIBLE);
                    if(MainActivity.currentUser != null){
                        sendToMain();
                    }else{
                        String errorMessage = "something wrong"; //get error message from json
                        Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                    MainActivity.currentUser = "some user";
                    reg_progress.setVisibility(View.INVISIBLE);
                } else {
                    //error create acc
                    Toast.makeText(RegisterActivity.this, "There are some errors", Toast.LENGTH_SHORT).show();
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

    private void closeKeyboard()
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
    }

    private void CreateUser(final String email, final String fullname, final String city, final String password, final String rep_password){

        RequestQueue mRequestQueue = newRequestQueue(RegisterActivity.this);
        // Progress
        String finaltype_request = "register";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    Integer temp_user_id = jsonObject.getInt("");
                    println("message=" + message);
                    if (message.equals("0")) {
                        MainActivity.currentUser = email;
                        SaveSettings();
                        MainActivity.User_id = 0;
                        Toast.makeText(RegisterActivity.this, "create user success", Toast.LENGTH_SHORT).show();
                        //go to SetupActivity
                        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                        startActivity(setupIntent);
                        finish();
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

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    public void SaveSettings () {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppFondSettings",Context.MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putString("current_email", MainActivity.currentUser);
        //myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
        myEdit.commit();
    }

}