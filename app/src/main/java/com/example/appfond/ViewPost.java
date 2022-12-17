package com.example.appfond;

import static com.example.appfond.BuildConfig.VERSION_NAME;
import static java.sql.DriverManager.println;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewPost extends AppCompatActivity {

   // Bundle bundle = getIntent().getExtras();
   //Intent intent = getIntent();
   // String value = getIntent().getStringExtra("textPost");;


    private TextView field_post_text;
    private TextView field_date_post;
    private TextView field_post_desc;
    private Uri postImageUri;
    private ImageView setupImage;
    private String idPost;
    private Toolbar postToolbar;
    private View viewMain;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;
    private ImageButton btnLikeImg;
    private TextView countLike;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);


        postToolbar = findViewById(R.id.viewPostToolbar);
        setSupportActionBar(postToolbar);
        getSupportActionBar().setTitle("Назад");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        field_post_text = findViewById(R.id.fieldtextPost);
        setupImage = findViewById(R.id.imagePostObj);
        field_date_post = findViewById(R.id.fieldDatePost);
        field_post_desc = findViewById(R.id.fieldDescText);
        countLike = findViewById(R.id.textCountLike);

        btnLikeImg = findViewById(R.id.imageLike);

        btnLikeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLike(idPost);
            }
        });



        Intent intent = this.getIntent();



        //get value from clild
        RequestOptions placeholderRequest = new RequestOptions();
        placeholderRequest.placeholder(R.drawable.default_profile);

        String fText = intent.getStringExtra("textPost");
        String imagePost = intent.getStringExtra("imagePost");
        String postDateValue = intent.getStringExtra("postDate");
        String postDescValue = intent.getStringExtra("postDesc");
        idPost = intent.getStringExtra("postId");

        //set value

        field_post_text.setText(fText);
        field_date_post.setText(postDateValue);
        field_post_desc.setText(postDescValue);

        Glide.with(this).setDefaultRequestOptions(placeholderRequest).load(imagePost).into(setupImage);
        postImageUri = Uri.parse(imagePost);

        //set likes
        getLikesPost(idPost);


        field_post_text.setMovementMethod(new ScrollingMovementMethod());

    }

    public void getLikesPost(String post_id){

        mRequestQueue = Volley.newRequestQueue(ViewPost.this);
        // Progress
        String finaltype_request = "get_hearts";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String count_user = jsonObject.getString("count_user");
                    String count_all = jsonObject.getString("count_hearts");

                    countLike.setText(count_all);


                    if (count_user.equals("0")) {
                         btnLikeImg.setBackgroundResource(R.drawable.like_border_red);
                    }else{
                          btnLikeImg.setBackgroundResource(R.drawable.heart_full);
                    }

                } catch (JSONException e) {
                    //Toast.makeText(ViewPost.this,e.toString(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(ChangePwdActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewPost.this)
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

                //Toast.makeText(ViewPost.this,error.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(ChangePwdActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewPost.this)
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
                params.put("id",post_id);
                params.put("id_user",MainActivity.User_id);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    //post like
    public void postLike(String post_id){

        mRequestQueue = Volley.newRequestQueue(ViewPost.this);
        // Progress
        String finaltype_request = "update_hearts";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_LOGIN_APP;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String count_user = jsonObject.getString("count_user");
                    String count_all = jsonObject.getString("count_all");

                    countLike.setText(count_all);

                    if (!count_user.equals("0")) {
                       btnLikeImg.setBackgroundResource(R.drawable.like_border_red);
                    }else{
                        btnLikeImg.setBackgroundResource(R.drawable.heart_full);
                    }

                } catch (JSONException e) {
                    //Toast.makeText(ViewPost.this,e.toString(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(ChangePwdActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewPost.this)
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

                //Toast.makeText(ViewPost.this,error.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(ChangePwdActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewPost.this)
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
                params.put("id_post",post_id);
                params.put("id_user",MainActivity.User_id);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

}