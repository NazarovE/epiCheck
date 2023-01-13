package com.example.appfond;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class AboutFragment extends Fragment {

    private TextView textViewAbout;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;
    private ProgressBar progressBarAbout;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for tis fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        textViewAbout = view.findViewById(R.id.textViewAbout);
        progressBarAbout = view.findViewById(R.id.pgBarAbout);

        if (MainActivity.main_text_about == null) {
            GetTextAbout();
        } else {
            textViewAbout.setText(MainActivity.main_text_about);
        }

        textViewAbout.setMovementMethod(new ScrollingMovementMethod());

        return view;
    }

    private void GetTextAbout() {
        progressBarAbout.setVisibility(View.VISIBLE);
        progressBarAbout.setVisibility(getView().VISIBLE);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        // Progress
        String finaltype_request = "about_fond";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_GET_TEXT;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String value = jsonObject.getString("value");
                    MainActivity.main_text_about = value;
                    textViewAbout.setText(value);
                    progressBarAbout.setVisibility(getView().INVISIBLE);


                } catch (JSONException e) {
                    progressBarAbout.setVisibility(getView().INVISIBLE);
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();

                }
                progressBarAbout.setVisibility(View.INVISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarAbout.setVisibility(getView().INVISIBLE);
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("request", finalType_request);

                return params;
            }
        };
        progressBarAbout.setVisibility(getView().INVISIBLE);
        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }
}