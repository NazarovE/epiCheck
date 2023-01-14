package com.example.appfond;

import static java.sql.DriverManager.println;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class NewCardActivity extends AppCompatActivity {

    private Toolbar newDiagToolbar;
    private EditText fieldBirthdayN, fieldDescN, fieldNameN;
    private Spinner fieldDiagN;
    private ProgressBar progressBarN;
    private Button btnSaveDiagN;
    private DatePickerDialog datePickerDialogN;
    public String[] Subject = {};
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        MainActivity.from_add = 1;
        newDiagToolbar = findViewById(R.id.newCardToolbar);
        setSupportActionBar(newDiagToolbar);
        getSupportActionBar().setTitle("Назад");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBarN = findViewById(R.id.progressBarNewDiagN);
        fieldBirthdayN = findViewById(R.id.fieldCardBirthdayN);
        fieldBirthdayN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogN.show();
            }
        });

        fieldDescN = findViewById(R.id.fieldCardPersonNameN);
        fieldNameN = findViewById(R.id.fieldCardPersonNameN);
        fieldDiagN = findViewById(R.id.fieldCardDiagN);

        initDatePicker();

        btnSaveDiagN = findViewById(R.id.buttonSaveDiag);

        if (Subject.length == 0) {
            getDiagValues();
        }

        btnSaveDiagN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeKeyboard();
                progressBarN.setVisibility(View.VISIBLE);

                String tmp_user = MainActivity.User_id;
                String tmp_name = fieldNameN.getText().toString();
                String tmp_diag = fieldDiagN.getSelectedItem().toString();
                String tmp_birthday = fieldBirthdayN.getText().toString();
                String tmp_desc = fieldDescN.getText().toString();

                if (!TextUtils.isEmpty(tmp_name) && tmp_name.length()>2 && !TextUtils.isEmpty(tmp_diag) && !TextUtils.isEmpty(tmp_birthday)) {

                    createCard(tmp_user,tmp_name,tmp_diag,tmp_desc,tmp_birthday);

                } else {
                    Toast.makeText(NewCardActivity.this,"Ошибка! Проверьте введенные данные! Все поля кроме описания не могут быть пустыми!" ,Toast.LENGTH_LONG).show();
                }
                progressBarN.setVisibility(View.INVISIBLE);
            }
        });


    }

    public void createCard(String user_id, String name_card, String name_diagnosis, String comm, String birthday){

        progressBarN.setVisibility(View.VISIBLE);
        mRequestQueue = Volley.newRequestQueue(NewCardActivity.this);
        // Progress
        //String finaltype_request = "check_user";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_CREATE_CARD;
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
                    Toast.makeText(NewCardActivity.this,"Ошибка! Проверьте введенные данные: "+ e.toString(),Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(NewCardActivity.this,"Ошибка! Проверьте введенные данные: "+error.toString(),Toast.LENGTH_LONG).show();

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

    private void sendToMain() {
        MainActivity.from_add = 1;
        Intent mainIntent = new Intent(NewCardActivity.this, MainActivity.class);
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

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                fieldBirthdayN.setText(date);
            }
        } ;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialogN = new DatePickerDialog(NewCardActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT,dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return year + "-" + month + "-" + day;
    }

    private void getDiagValues() {
        //Toast.makeText(HomeFragment.this, "getMessage", Toast.LENGTH_LONG).show();
        //progressBarHome.setVisibility(View.VISIBLE);
        HTTPSBase Global = new HTTPSBase();
        String url = Global.URL_GET_DIAGNOSIS;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //MainActivity.diag_values.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //String success = "0";
                    //success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("diagnosis");
                    //Toast.makeText(MainActivity.this, success + "" + jsonArray.length(), Toast.LENGTH_LONG).show();
                    //if (success.equals("1")) {
                    String[] temp = new String[jsonArray.length()];
                    Integer tempDiag;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String name_card = object.getString("name");
                        temp[i] = name_card;

                    }
                    //}

                    Subject = temp;

                    ArrayAdapter<String> adapter = new ArrayAdapter(NewCardActivity.this, android.R.layout.simple_spinner_item, Subject);
                    // ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.simple_spinner_item,Subject );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fieldDiagN.setAdapter(adapter);



                } catch (Exception e) {
                    //progressBarHome.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                    Toast.makeText(NewCardActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewCardActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(NewCardActivity.this);
        requestQueue.add(request);
    }
}