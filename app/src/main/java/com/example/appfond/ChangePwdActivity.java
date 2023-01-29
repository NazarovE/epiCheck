package com.example.appfond;

import static com.example.appfond.BuildConfig.VERSION_NAME;
import static java.sql.DriverManager.println;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class ChangePwdActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar chnPwdToolBat;
    private TextView passOld, passNew, passRepeat;
    private Button chnPwd;
    private ProgressBar prgBarCngPwd;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        prgBarCngPwd = findViewById(R.id.progressBarCngPwd);

        chnPwdToolBat = findViewById(R.id.toolbarChgPwd);
        setSupportActionBar(chnPwdToolBat);
        getSupportActionBar().setTitle("Назад");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        passOld = findViewById(R.id.oldPasswordField);
        passNew = findViewById(R.id.newPassField);
        passRepeat = findViewById(R.id.repeatNewPassField);

        chnPwd = findViewById(R.id.buttonChangePwd);
        chnPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = passOld.getText().toString();
                String newPassword = passNew.getText().toString();
                String repeatPassword = passRepeat.getText().toString();


                if(!TextUtils.isEmpty(oldPassword) && !TextUtils.isEmpty(newPassword) &&
                   !TextUtils.isEmpty(repeatPassword) && newPassword.equals(repeatPassword) &&
                   newPassword.length()>=6
                ){

                   // closeKeyboard();

                    prgBarCngPwd.setVisibility(View.VISIBLE);

                    postNewPassword(oldPassword, newPassword);

                    //success change password
                    prgBarCngPwd.setVisibility(View.INVISIBLE);


                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext())
                            //set icon
                            .setIcon(R.drawable.logo)
                            //set title
                            .setTitle("Информация")
                            //set message
                            .setMessage("Проверьте введенные данные. Возможно не совпадают пароли или содержат иные символы, " +
                                    "отличные от латинских букв, цифр, нижнего подчеркивания. Пароль должен быть не менее 6 символов.")
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
    }


    private void sendToMain() {
        Intent mainIntent = new Intent(ChangePwdActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

   /* private void closeKeyboard()
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

    private void postNewPassword(final String old_pass, final String new_pass){

        mRequestQueue = Volley.newRequestQueue(ChangePwdActivity.this);
        // Progress
        String finaltype_request = "updatepassword";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_CHN_PWD;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    println("message=" + message);
                    if (message.equals("0")) {
                        Toast.makeText(ChangePwdActivity.this,"Пароль успешно изменен",Toast.LENGTH_SHORT).show();
                        prgBarCngPwd.setVisibility(View.INVISIBLE);
                        sendToMain();
                    }

                } catch (JSONException e) {
                    prgBarCngPwd.setVisibility(View.INVISIBLE);
                    //Toast.makeText(ChangePwdActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangePwdActivity.this)
                            .setIcon(R.drawable.logo)
                            .setTitle("Ошибка")
                            .setMessage(e.toString())
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prgBarCngPwd.setVisibility(View.INVISIBLE);
                Toast.makeText(ChangePwdActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangePwdActivity.this)
                        .setIcon(R.drawable.logo)
                        .setTitle("Ошибка")
                        .setMessage(error.toString())
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);
                params.put("oldpassword",old_pass);
                params.put("newpassword",new_pass);
                params.put("user_id", MainActivity.User_id);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }


}