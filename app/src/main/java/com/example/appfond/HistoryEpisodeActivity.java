package com.example.appfond;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class HistoryEpisodeActivity extends AppCompatActivity {

    private Toolbar toolbarHistory;
    private RecyclerView episode_list_view;
    private List<Episodes> episode_list;
    Episodes episodes;
    EpisodesAdapter adapter;
    private ProgressBar progressBarEpi;
    String tempCardId;
    String dateBegin = "2023-01-06";
    String dateEnd = "2023-01-10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_episode);

        toolbarHistory = findViewById(R.id.toolbarHistory);
        setSupportActionBar(toolbarHistory);
        getSupportActionBar().setTitle("Назад");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        episode_list = new ArrayList<>();
        episode_list_view = findViewById(R.id.episode_list_view);
        progressBarEpi = findViewById(R.id.progressBarHistEp);

        adapter = new EpisodesAdapter(getApplicationContext(), episode_list);
        episode_list_view.setLayoutManager(new LinearLayoutManager(HistoryEpisodeActivity.this));
        episode_list_view.setAdapter(adapter);

        //get tempCardId;
        tempCardId = getIntent().getSerializableExtra("tempCardId").toString();

        getEpisodes();
    }


    private void getEpisodes() {
        progressBarEpi.setVisibility(View.VISIBLE);
        HTTPSBase Global = new HTTPSBase();
        String url = Global.URL_GET_HISTORY + "?id_card=" + tempCardId + "&datebeg=" + dateBegin + "&dateend=" + dateEnd;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                episode_list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //String success = "0";
                    //success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("episodes");
                    //Toast.makeText(MainActivity.this, success + "" + jsonArray.length(), Toast.LENGTH_LONG).show();
                    //if (success.equals("1")) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String datetime = object.getString("date");
                        String comment = object.getString("comment");
                        String id_card = object.getString("id_card");
                        String id_episode = object.getString("id_episode");

                        episodes = new Episodes(datetime, comment, id_card, id_episode);
                        episode_list.add(episodes);
                        adapter.notifyDataSetChanged();
                        progressBarEpi.setVisibility(View.INVISIBLE);
                    }
                    //}

                } catch (Exception e) {
                    progressBarEpi.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                    Toast.makeText(HistoryEpisodeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        Toast.makeText(HomeFragment.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(HistoryEpisodeActivity.this);
        requestQueue.add(request);
    }
}