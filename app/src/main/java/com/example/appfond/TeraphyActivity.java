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

public class TeraphyActivity extends AppCompatActivity {

    private Toolbar toolbarTer;
    private RecyclerView teraphy_list_view;
    private List<Teraphy> teraphy_list;
    Teraphy teraphy;
    TeraphyAdapter adapter;
    private ProgressBar progressBarTer;
    String tempCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teraphy);
        MainActivity.from_add = 1;
        toolbarTer = findViewById(R.id.toolbarTer);
        setSupportActionBar(toolbarTer);
        getSupportActionBar().setTitle("Назад");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        teraphy_list = new ArrayList<>();
        teraphy_list_view = findViewById(R.id.ter_list);
        progressBarTer = findViewById(R.id.progressBarTer);

        adapter = new TeraphyAdapter(getApplicationContext(), teraphy_list);
        teraphy_list_view.setLayoutManager(new LinearLayoutManager(TeraphyActivity.this));
        teraphy_list_view.setAdapter(adapter);

        //get tempCardId;
        tempCardId = getIntent().getSerializableExtra("tempCardId").toString();

        getTeraphy();
    }

    private void getTeraphy() {
        progressBarTer.setVisibility(View.VISIBLE);
        HTTPSBase Global = new HTTPSBase();
        String url = Global.URL_GET_TERAPHY_ARCH + "?id_card=" + tempCardId;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                teraphy_list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("terapies");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String id_card = object.getString("id_card");
                        String id_ter = object.getString("id_ter");
                        String name_ter = object.getString("name_ter");
                        String country_ter = object.getString("country_ter");
                        String doz_ter = object.getString("doz_ter");
                        String date_begin = object.getString("date_begin");
                        String date_end = object.getString("date_end");
                        String active_ter = object.getString("active_ter");

                        teraphy = new Teraphy(id_ter, name_ter, country_ter, doz_ter, date_begin, date_end, active_ter, id_card);
                        teraphy_list.add(teraphy);
                    }
                    adapter.notifyDataSetChanged();
                    progressBarTer.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    adapter.notifyDataSetChanged();
                    progressBarTer.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                    Toast.makeText(TeraphyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                adapter.notifyDataSetChanged();
                Toast.makeText(TeraphyActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(TeraphyActivity.this);
        requestQueue.add(request);
    }
}