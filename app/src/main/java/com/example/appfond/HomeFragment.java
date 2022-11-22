package com.example.appfond;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;
    BlogPost blogpost;
    BlogRecyclerAdapter adapter;
    private ProgressBar progressBarHome;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        blog_list = new ArrayList<>();
        blog_list_view = view.findViewById(R.id.blog_list_view);
        progressBarHome = view.findViewById(R.id.progressBarHome);

        adapter = new BlogRecyclerAdapter(getActivity().getApplicationContext(), blog_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_list_view.setAdapter(adapter);


        getPosts();
        // Inflate the layout for this fragment
        return view;
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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);
    }
}