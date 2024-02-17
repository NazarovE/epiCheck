package com.example.appfond;

import static java.sql.DriverManager.println;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
//import com.synnapps.carouselview.CarouselView;
//import com.synnapps.carouselview.ImageListener;

public class ViewPost extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private String[] urls;


    private static int SLIDE_NUMBER = 10;

    private ArrayList<String> imageUrls ;

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
    private ProgressBar pgBar;
    //CarouselView carouselView;

    SliderView sliderView;
    ImageView imageNoSLider;


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
        pgBar = findViewById(R.id.progressBarViewPost);
        //carouselView = (CarouselView) findViewById(R.id.carouselView);

        btnLikeImg = findViewById(R.id.imageLike);
        sliderView = findViewById(R.id.sliderView);
        //imageNoSLider = findViewById(R.id.image_view_crop);



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
        field_post_text.setMovementMethod(LinkMovementMethod.getInstance());


        field_date_post.setText(postDateValue);
        field_post_desc.setText(postDescValue);

        //Glide.with(this).setDefaultRequestOptions(placeholderRequest).load(imagePost).into(setupImage);

       // Glide.with(this).setDefaultRequestOptions(placeholderRequest).load(imagePost).into(imageNoSLider);
        postImageUri = Uri.parse(imagePost);




        pgBar.setVisibility(View.INVISIBLE);
        //set likes
        getLikesPost(idPost, imagePost);


    }



    public void getLikesPost(String post_id, String imaDef){
        pgBar.setVisibility(View.VISIBLE);
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
                    String count_col = jsonObject.getString("count_col");

                    countLike.setText(count_all);

                    if (MainActivity.User_id.equals("0")){
                        btnLikeImg.setBackgroundResource(R.drawable.heart_border_gray);
                        countLike.setTextColor(Color.GRAY);
                        btnLikeImg.setEnabled(false);
                    } else {
                        btnLikeImg.setEnabled(true);
                        countLike.setTextColor(Color.RED);
                        if (count_user.equals("0")) {
                            btnLikeImg.setBackgroundResource(R.drawable.like_border_red);
                        }else{
                            btnLikeImg.setBackgroundResource(R.drawable.heart_full);
                        }
                    }



                    if (count_col.equals("0")) {
                        String[] imageUrlsTmp = new String[1];
                        for (int i = 0; i < imageUrlsTmp.length; i++) {
                            String image_url = imaDef;
                            imageUrlsTmp[i] = (image_url);
                        }
                        urls = imageUrlsTmp;

                        SliderAdapter sliderAdapter = new SliderAdapter(urls);
                        sliderView.setSliderAdapter(sliderAdapter);
                        sliderView.stopAutoCycle();
                        /*sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP);
                        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                        sliderView.startAutoCycle();*/

                    }else{
                        getImageCollection();
                    }

                    pgBar.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    //Toast.makeText(ViewPost.this,e.toString(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(ChangePwdActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                    pgBar.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewPost.this)
                            .setIcon(R.drawable.epicheck_logo)
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
                pgBar.setVisibility(View.INVISIBLE);
                //Toast.makeText(ViewPost.this,error.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(ChangePwdActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewPost.this)
                        .setIcon(R.drawable.epicheck_logo)
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


    private void getImageCollection() {
//        Toast.makeText(HomeFragment.this, "getMessage", Toast.LENGTH_LONG).show();
        pgBar.setVisibility(View.VISIBLE);
        HTTPSBase Global = new HTTPSBase();
        String url = Global.URL_GET_COLL + "?id_post=" + idPost;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    //String success = "0";
                    //success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("imagesforcol");
                    String[] imageUrlsTmpC = new String[jsonArray.length()];
                    //Toast.makeText(MainActivity.this, success + "" + jsonArray.length(), Toast.LENGTH_LONG).show();
                    //if (success.equals("1")) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String image = object.getString("img_path");
                        String image_url = Global.URL_ROOT + "/" + image;
                        imageUrlsTmpC[i] = (image_url);


                    }
                    println("count img col = " + imageUrlsTmpC.length + "PostId=" + idPost);
                    urls = imageUrlsTmpC;

                   /* carouselView.setImageListener(imageListener);
                    carouselView.setPageCount(urls.length);*/

                    SliderAdapter sliderAdapterC = new SliderAdapter(urls);

                    sliderView.setSliderAdapter(sliderAdapterC);
                    //sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    //sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                    sliderView.startAutoCycle();


                    pgBar.setVisibility(View.INVISIBLE);
                    //}

                } catch (Exception e) {
                    pgBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        Toast.makeText(HomeFragment.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ViewPost.this);
        requestQueue.add(request);
    }


    //post like
    public void postLike(String post_id){
        pgBar.setVisibility(View.VISIBLE);
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
                    pgBar.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    //Toast.makeText(ViewPost.this,e.toString(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(ChangePwdActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                    pgBar.setVisibility(View.INVISIBLE);
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
                pgBar.setVisibility(View.INVISIBLE);
                //Toast.make        Text(ViewPost.this,error.toString(),Toast.LENGTH_LONG).show();
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