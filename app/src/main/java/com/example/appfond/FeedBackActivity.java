package com.example.appfond;

import static java.sql.DriverManager.println;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

public class FeedBackActivity extends AppCompatActivity {

    private Toolbar toolbarFeedBack;
    private ProgressBar progressBarFB;
    private EditText fieldEmailFB, fieldDescrFB;
    private TextView labelEmailFB, labelDescrFB;
    private Button btnSendFB;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed_back);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //toolbarFeedBack = findViewById(R.id.newCardToolbar);


    toolbarFeedBack = findViewById(R.id.feedBackToolbar);
        setSupportActionBar(toolbarFeedBack);
        getSupportActionBar().setTitle(R.string.textBack);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressBarFB = findViewById(R.id.progressBarFeedBack);
    fieldEmailFB = findViewById(R.id.fieldEmailFeedBack);
    fieldDescrFB = findViewById(R.id.fieldDescrFeedBack);
    btnSendFB = findViewById(R.id.buttonSendFB);
    btnSendFB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBarFB.setVisibility(View.VISIBLE);

            String tmp_user = MainActivity.User_id;
            String tmp_emailFB = fieldEmailFB.getText().toString();
            String tmp_descrFB = fieldDescrFB.getText().toString();

            if (!TextUtils.isEmpty(tmp_emailFB) && tmp_emailFB.length()>2 && !TextUtils.isEmpty(tmp_descrFB)) {

                sendFeedBack(tmp_user, tmp_emailFB, tmp_descrFB);

            } else {
                Toast.makeText(FeedBackActivity.this, getString(R.string.textErrorCheckDataExceptDescr) ,Toast.LENGTH_LONG).show();
            }
            progressBarFB.setVisibility(View.INVISIBLE);
        }
    });

    }

    public void sendFeedBack(String user_id, String email, String comm){

        progressBarFB.setVisibility(View.VISIBLE);
        mRequestQueue = Volley.newRequestQueue(FeedBackActivity.this);

        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_SEND_FEEDBACK;

        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    println("message=" + message);
                    if (message.equals("0")) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FeedBackActivity.this)
                                //set icon
                                .setIcon(R.drawable.epi_check_logo_sm)
                                //set title
                                .setTitle(R.string.textAttention)
                                //set message
                                .setMessage(R.string.textMessageSendOk)
                                //set positive button
                                .setPositiveButton(R.string.textOK, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        sendToMain();
                                    }
                                });
                        //set negative button
                        AlertDialog dialog = alertDialog.create();
                        dialog.show();

                        sendToMain();

                    }

                } catch (JSONException e) {
                    /*Toast.makeText(FeedBackActivity.this,getString(R.string.textErrorCheckData) + e.toString(),Toast.LENGTH_LONG).show();
                    println(e.toString());*/
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(FeedBackActivity.this)
                            //set icon
                            .setIcon(R.drawable.epi_check_logo_sm)
                            //set title
                            .setTitle(R.string.textAttention)
                            //set message
                            .setMessage(R.string.textMessageSendOk)
                            //set positive button
                            .setPositiveButton(R.string.textOK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    sendToMain();
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

                Toast.makeText(FeedBackActivity.this,getString(R.string.textErrorCheckData) +error.toString(),Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("email",email);
                params.put("comment",comm);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    private void sendToMain() {
        MainActivity.from_add = 4;
        Intent mainIntent = new Intent(FeedBackActivity.this, MainActivity.class);
        startActivity(mainIntent);
        //finish();
    }


}