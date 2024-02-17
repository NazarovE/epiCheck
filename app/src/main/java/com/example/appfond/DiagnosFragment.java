package com.example.appfond;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DiagnosFragment extends Fragment {

    private RecyclerView diag_list;
    private List<Cards> card_list;
    Cards card;
    CardRecyclerAdapter adapter;
    private ProgressBar progressBarHome;
    //private FloatingActionButton btnAddDiag;
    private Button btnAddDiag;

    public DiagnosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diagnos, container, false);

        card_list = new ArrayList<>();
        diag_list = view.findViewById(R.id.diag_list);
        progressBarHome = view.findViewById(R.id.progressBarDiag);
        btnAddDiag = view.findViewById(R.id.buttonNewDiag);
        btnAddDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newCardIntent = new Intent(getContext(), NewCardActivity.class);
                newCardIntent.setAction(Intent.ACTION_SEND);
                startActivity(newCardIntent);

            }
        });

        /*private void sendToNewDiag() {
            Intent newCardIntent = new Intent(getContext(), NewCardActivity.class);
            startActivity(newCardIntent);
            // finish();
        }*/


        adapter = new CardRecyclerAdapter(getActivity().getApplicationContext(), card_list);
        diag_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        diag_list.setAdapter(adapter);
        //getDiagValues();
        getDiagnosis();

        return view;
    }



    private void getDiagnosis() {
//        Toast.makeText(HomeFragment.this, "getMessage", Toast.LENGTH_LONG).show();
        progressBarHome.setVisibility(View.VISIBLE);
        HTTPSBase Global = new HTTPSBase();
        String url = Global.URL_GET_CARDS + "?id_user=" + MainActivity.User_id;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                card_list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //String success = "0";
                    //success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("cards");
                    //Toast.makeText(MainActivity.this, success + "" + jsonArray.length(), Toast.LENGTH_LONG).show();
                    //if (success.equals("1")) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String name_card = object.getString("name_card");
                        String date_create = object.getString("date_create");
                        String birthday = object.getString("birthday");
                        String name_diagnosis = object.getString("name_diagnosis");
                        String card_comment = object.getString("card_comment");
                        String id_card = object.getString("id_card");
                        String id_user = object.getString("id_user");
                        String id_diagnosis = object.getString("id_diagnosis");
                        String name_diag = object.getString("name_diag");
                        String count_episode_today = object.getString("count_episode_today");

                        card = new Cards(name_card, date_create, birthday, name_diagnosis, card_comment, id_card,
                                id_user, id_diagnosis, name_diag, count_episode_today);
                        card_list.add(card);
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

        progressBarHome.setVisibility(View.INVISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);
    }


}