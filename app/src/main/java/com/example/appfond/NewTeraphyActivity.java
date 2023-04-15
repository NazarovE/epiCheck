package com.example.appfond;

import static java.sql.DriverManager.println;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewTeraphyActivity extends AppCompatActivity {

    private Toolbar newTerToolbar;
    private EditText fieldNameTer, fieldCountryTer, fieldDozTer, fieldDateBegTer, fieldDateEndTer;
    private ProgressBar progressBarTer;
    private Button btnNewTer;

    private DatePickerDialog datePickerBeg, datePickerEnd;

    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;
    String tempCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_teraphy);

        MainActivity.from_add = 1;
        newTerToolbar = findViewById(R.id.newTerToolbar);
        setSupportActionBar(newTerToolbar);
        getSupportActionBar().setTitle("Назад");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tempCardId = getIntent().getSerializableExtra("tempCardId").toString();

        progressBarTer = findViewById(R.id.progressBarNewTer);
        fieldNameTer = findViewById(R.id.fieldNameNewTer);
        fieldCountryTer = findViewById(R.id.fieldCountryNewTer);
        fieldDozTer = findViewById(R.id.fieldDozNewTer);
        fieldDateBegTer = findViewById(R.id.fieldNewDateBegTer);
        fieldDateEndTer = findViewById(R.id.fieldNewDateEndTer);

        fieldDateBegTer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerBeg.show();
            }
        });

        fieldDateEndTer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerEnd.show();
            }
        });

        initDatePickerBeg();
        initDatePickerEnd();

        btnNewTer = findViewById(R.id.buttonSaveNewTer);

        btnNewTer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //closeKeyboard();
                progressBarTer.setVisibility(View.VISIBLE);

                String tmp_user = MainActivity.User_id;
                String tmp_card_id = tempCardId;
                String tmp_name = fieldNameTer.getText().toString();
                String tmp_country = fieldCountryTer.getText().toString();
                String tmp_doz = fieldDozTer.getText().toString();
                String tmp_date_beg = fieldDateBegTer.getText().toString();
                String tmp_date_end = fieldDateEndTer.getText().toString();

                if (!TextUtils.isEmpty(tmp_name) && tmp_name.length()>2 && !TextUtils.isEmpty(tmp_country) && tmp_country.length()>2
                        && !TextUtils.isEmpty(tmp_doz) && tmp_doz.length()>2 && !TextUtils.isEmpty(tmp_date_beg) ) {

                    createTer(tmp_card_id,tmp_name,tmp_country,tmp_doz,tmp_date_beg,tmp_date_end);

                } else {
                    Toast.makeText(NewTeraphyActivity.this,"Ошибка! Проверьте введенные данные! Все поля кроме 'Дата вывода' не могут быть пустыми!" ,Toast.LENGTH_LONG).show();
                }
                progressBarTer.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void createTer(String card_id, String name_ter, String country_ter, String doz_ter, String date_beg, String date_end){

        progressBarTer.setVisibility(View.VISIBLE);
        mRequestQueue = Volley.newRequestQueue(NewTeraphyActivity.this);
        // Progress
        //String finaltype_request = "check_user";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_NEW_TERAPHY;
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
                    Toast.makeText(NewTeraphyActivity.this,"Ошибка! Проверьте введенные данные: "+ e.toString(),Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(NewTeraphyActivity.this,"Ошибка! Проверьте введенные данные: "+error.toString(),Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("newname", name_ter);
                params.put("card_id",card_id);
                params.put("newcountry",country_ter);
                params.put("newdoz",doz_ter);
                params.put("newbegter",date_beg);
                params.put("newendter",date_end);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    private void initDatePickerBeg() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                fieldDateBegTer.setText(date);
            }
        } ;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerBeg = new DatePickerDialog(NewTeraphyActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT,dateSetListener, year, month, day);
    }

    private void initDatePickerEnd() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                fieldDateEndTer.setText(date);
            }
        } ;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerEnd = new DatePickerDialog(NewTeraphyActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT,dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        if (month<10 && day>=10) {
            return year + "-0" + month + "-" + day;
        } else if (month>=10 && day<10) {
            return year + "-" + month + "-0" + day;
        } else if (month<10 && day<10) {
            return year + "-0" + month + "-0" + day;
        } else {
            return year + "-" + month + "-" + day;
        }
    }

    private void sendToMain() {
        MainActivity.from_add = 1;
        Intent mainIntent = new Intent(NewTeraphyActivity.this, MainActivity.class);
        startActivity(mainIntent);
        //finish();
    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
}