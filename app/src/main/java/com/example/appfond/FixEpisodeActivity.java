package com.example.appfond;

import static java.sql.DriverManager.println;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
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

import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FixEpisodeActivity extends AppCompatActivity {

    private Toolbar fixEpitoolbar;
    private EditText fieldDateFix, fieldTimeFix, fieldDesc;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button push;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;
    String tempCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_episode);
        MainActivity.from_add = 1;
        //get tempCardId;
        tempCardId = getIntent().getSerializableExtra("tempCardId").toString();

        fixEpitoolbar = findViewById(R.id.toolbarFixEpi);
        fieldDateFix = findViewById(R.id.fieldDateFix);
        fieldTimeFix = findViewById(R.id.fieldFixTime);
        fieldDesc = findViewById(R.id.fieldEpiDesc);
        push = findViewById(R.id.buttonPushEpi);

        initDatePicker();
        initTimePicker();

        fieldDateFix.setText(getTodayDate());
        fieldDateFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        fieldTimeFix.setText(getTodayTime());
        fieldTimeFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        setSupportActionBar(fixEpitoolbar);
        getSupportActionBar().setTitle("Назад");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(fieldDateFix.getText().toString()) && !TextUtils.isEmpty(fieldTimeFix.getText().toString())) {
                    String tempUserId = MainActivity.User_id;
                    String tempDate = fieldDateFix.getText().toString();
                    String tempTime = fieldTimeFix.getText().toString();
                    String comm = fieldDesc.getText().toString();
                    pushEpi(tempUserId, tempCardId, tempDate, tempTime, comm);
                } else {
                    Toast.makeText(FixEpisodeActivity.this,"Ошибка! Проверьте введенные данные!" ,Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        return makeDateString(day, month, year);
    }

    private String getTodayTime() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        return makeTimeString(hour, min);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                fieldDateFix.setText(date);
            }
        } ;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(FixEpisodeActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT,dateSetListener, year, month, day);
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                //timePicker.is24HourView(true);
                String time = makeTimeString(hour, min);
                fieldTimeFix.setText(time);
            }
        } ;

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        //int style = AlertDialog.THEME_HOLO_LIGHT;
        timePickerDialog = new TimePickerDialog(FixEpisodeActivity.this, timeSetListener, hour, min, true);
    }

    private String makeDateString(int day, int month, int year) {
        String mDate;
        String dateStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        if ((month<10) && (day<10)) {
            mDate = year + "-0" + month + "-0" + day;
        } else if ((month<10) && (day>=10)) {
            mDate = year + "-0" + month + "-" + day;
        } else if ((month>=10) && (day<10)) {
            mDate = year + "-" + month + "-0" + day;
        } else {
            mDate = year + "-" + month + "-" + day;
        }
        return dateStamp;
    }

    private String makeTimeString(int hour, int min) {

        String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

        return timeStamp;
    }

    public void pushEpi(String user_id, String card_id, String date_val, String time_val, String comm){

        mRequestQueue = Volley.newRequestQueue(FixEpisodeActivity.this);
        // Progress
        //String finaltype_request = "check_user";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_CREATE_EPISODE;
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
                    Toast.makeText(FixEpisodeActivity.this,"Ошибка! Проверьте введенные данные: "+ e.toString(),Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(FixEpisodeActivity.this,"Ошибка! Проверьте введенные данные: "+error.toString(),Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("card_id", card_id);
                params.put("user_id",user_id);
                params.put("date_episode",date_val + " " + time_val);
                params.put("comment",comm);


                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    private void sendToMain() {
        MainActivity.from_add = 1;
        Intent mainIntent = new Intent(FixEpisodeActivity.this, MainActivity.class);
        startActivity(mainIntent);

        //finish();
    }
}