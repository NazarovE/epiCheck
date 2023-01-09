package com.example.appfond;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;

public class FixEpisodeActivity extends AppCompatActivity {

    private Toolbar fixEpitoolbar;
    private EditText fieldDateFix, fieldTimeFix;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_episode);

        fixEpitoolbar = findViewById(R.id.toolbarFixEpi);
        fieldDateFix = findViewById(R.id.fieldDateFix);
        fieldTimeFix = findViewById(R.id.fieldFixTime);

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
        if ((month<10) && (day<10)) {
            mDate = year + "-0" + month + "-0" + day;
        } else if ((month<10) && (day>=10)) {
            mDate = year + "-0" + month + "-" + day;
        } else if ((month>=10) && (day<10)) {
            mDate = year + "-" + month + "-0" + day;
        } else {
            mDate = year + "-" + month + "-" + day;
        }
        return mDate;
    }

    private String makeTimeString(int hour, int min) {
        String mTime;
        if ((hour<10) && (min<10)){
            mTime = "0" + hour + ":0" + min;
        } else if ((hour<10) && (min>=10)){
            mTime = "0" + hour + ":" + min;
        } else if ((hour>=10) && (min<10)){
            mTime = hour + ":0" + min;
        } else {
            mTime = hour + ":" + min;
        }
        return mTime;
    }
}