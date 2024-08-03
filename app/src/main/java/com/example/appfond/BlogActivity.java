package com.example.appfond;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BlogActivity extends AppCompatActivity {

    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;
    BlogPost blogpost;
    BlogRecyclerAdapter adapter;
    private ProgressBar progressBarHome;
    private Button lastPost;
    private Button ArcPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        blog_list = new ArrayList<>();
        blog_list_view = findViewById(R.id.blog_list_view);
        progressBarHome = findViewById(R.id.progressBarHome);

        adapter = new BlogRecyclerAdapter(BlogActivity.this, blog_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager(BlogActivity.this));
        blog_list_view.setAdapter(adapter);

        lastPost = findViewById(R.id.buttonLastCount);
        ArcPost = findViewById(R.id.buttonArcCount);

        ArcPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isShowAllPosts = 1;
                getViewPost();
            }
        });

        lastPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isShowAllPosts = 0;
                getViewPost();
            }
        });
        //if (!GlobalVariables.fromViewPost) {
            getViewPost();


    }

    private void getViewPost(){
        if (MainActivity.isShowAllPosts == 0) {
            getPosts();
        }else{
            getArchPosts();
        }
    }

    private void getPosts() {
//        Toast.makeText(HomeFragment.this, "getMessage", Toast.LENGTH_LONG).show();
        progressBarHome.setVisibility(View.VISIBLE);
        HTTPSBase Global = new HTTPSBase();
        String url = Global.URL_GETDEFPOSTS;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                blog_list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //String success = "0";
                    //success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("posts");
                    //Toast.makeText(MainActivity.this, success + "" + jsonArray.length(), Toast.LENGTH_LONG).show();
                    //if (success.equals("1")) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String id = object.getString("id");
                        String title = object.getString("title");
                        String date_post_txt = object.getString("date_post_txt");
                        String image = object.getString("image");
                        String image_url = Global.URL_ROOT + "/" + image;
                        String text = object.getString("text");
                        String date_post = object.getString("date_post_txt");

                        blogpost = new BlogPost(id, title, text, date_post_txt, image_url);
                        blog_list.add(blogpost);
                        adapter.notifyDataSetChanged();
                        progressBarHome.setVisibility(View.INVISIBLE);
                    }
                    //}

                } catch (Exception e) {
                    progressBarHome.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        Toast.makeText(HomeFragment.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(BlogActivity.this);
        requestQueue.add(request);
    }

    private void getArchPosts() {
//        Toast.makeText(HomeFragment.this, "getMessage", Toast.LENGTH_LONG).show();
        progressBarHome.setVisibility(View.VISIBLE);
        HTTPSBase Global = new HTTPSBase();
        String url = Global.URL_GETALLPOSTS;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                blog_list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //String success = "0";
                    //success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("posts");
                    //Toast.makeText(MainActivity.this, success + "" + jsonArray.length(), Toast.LENGTH_LONG).show();
                    //if (success.equals("1")) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String id = object.getString("id");
                        String title = object.getString("title");
                        String date_post_txt = object.getString("date_post_txt");
                        String image = object.getString("image");
                        String image_url = Global.URL_ROOT + "/" + image;
                        String text = object.getString("text");
                        String date_post = object.getString("date_post_txt");

                        blogpost = new BlogPost(id, title, text, date_post_txt, image_url);
                        blog_list.add(blogpost);
                        adapter.notifyDataSetChanged();
                        progressBarHome.setVisibility(View.INVISIBLE);
                    }
                    //}

                } catch (Exception e) {
                    progressBarHome.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        Toast.makeText(HomeFragment.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(BlogActivity.this);
        requestQueue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int position = data.getIntExtra("selected_position", 0);
            blog_list_view.scrollToPosition(position);
        }
    }
}