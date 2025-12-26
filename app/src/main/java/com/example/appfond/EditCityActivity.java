package com.example.appfond;

import static java.sql.DriverManager.println;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class EditCityActivity extends AppCompatActivity {

    private Toolbar toolbarEditCity;
    private TextView editCityField;
    private Button chnCityBtn;
    private ProgressBar prgBarCngCity;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;
    public static String nameSettings = "EpiCheckSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_city);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbarEditCity = findViewById(R.id.toolbarEditCity);
        setSupportActionBar(toolbarEditCity);
        getSupportActionBar().setTitle(R.string.textBack);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editCityField = findViewById(R.id.editNewCity);
        editCityField.setText(MainActivity.user_city);

        MainActivity.from_add = 4;

        chnCityBtn = findViewById(R.id.buttonChangeCity);
        prgBarCngCity = findViewById(R.id.progressBarCngCity);

        chnCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newCity = editCityField.getText().toString();

                if(!TextUtils.isEmpty(newCity) && newCity.length()>=2){

                    prgBarCngCity.setVisibility(View.VISIBLE);
                    postNewCity(newCity, MainActivity.User_id);
                    //prgBarCngName.setVisibility(View.INVISIBLE);

                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext())
                            //set icon
                            .setIcon(R.drawable.epi_check_logo_sm)
                            //set title
                            .setTitle(R.string.textInformation)
                            //set message
                            .setMessage(getString(R.string.textErrorMain))
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


    }

    private void sendToMain() {
        Intent mainIntent = new Intent(EditCityActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }


    public void SaveSettings(final String val) {
        SharedPreferences sharedPreferences = getSharedPreferences(nameSettings, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("city", val);
        myEdit.commit();
    }


    private void postNewCity(final String new_city, final String user_id){

        mRequestQueue = Volley.newRequestQueue(EditCityActivity.this);
        // Progress
        String finaltype_request = "updatecity";
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
                        //Toast.makeText(EditNameActivity.this, R.string.successChangePwd,Toast.LENGTH_SHORT).show();
                        SaveSettings(new_city);
                        MainActivity.user_city = new_city;
                        prgBarCngCity.setVisibility(View.INVISIBLE);
                        sendToMain();
                    }

                } catch (JSONException e) {
                    prgBarCngCity.setVisibility(View.INVISIBLE);
                    //Toast.makeText(ChangePwdActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditCityActivity.this)
                            .setIcon(R.drawable.epi_check_logo_sm)
                            .setTitle(R.string.textErrorMain)
                            .setMessage(e.toString())
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prgBarCngCity.setVisibility(View.INVISIBLE);
                Toast.makeText(EditCityActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditCityActivity.this)
                        .setIcon(R.drawable.epi_check_logo_sm)
                        .setTitle(R.string.textErrorMain)
                        .setMessage(error.toString())
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);
                params.put("newcity",new_city);
                params.put("user_id", MainActivity.User_id);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

}