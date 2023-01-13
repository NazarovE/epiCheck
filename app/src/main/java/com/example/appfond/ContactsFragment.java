package com.example.appfond;

import static java.sql.DriverManager.println;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ContactsFragment extends Fragment {

    private ProgressBar progressBar;
    private Button btnSendToHelp;
    private Button btnSendToQuestion;
    private TextView textViewContacts;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;
    HTTPSBase Global = new HTTPSBase();

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        progressBar = view.findViewById(R.id.progressBarContacts);
        btnSendToHelp = view.findViewById(R.id.buttonToHelp);
        btnSendToQuestion = view.findViewById(R.id.buttonToQuestion);
        textViewContacts = view.findViewById(R.id.textViewContacts);

        if (MainActivity.main_text_contacts == null) {
            GetTextContacts();
        } else {
            textViewContacts.setText(MainActivity.main_text_contacts);
        }

        textViewContacts.setMovementMethod(new ScrollingMovementMethod());

        btnSendToQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*HTTPSBase Global = new HTTPSBase();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Global.URL_NEED_HELP));
                startActivity(browserIntent);*/

                String url = MainActivity.URL_GET_FEEDBACK;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        btnSendToHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*HTTPSBase Global = new HTTPSBase();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Global.URL_GET_FEEDBACK));
                startActivity(browserIntent);*/

                String url = MainActivity.URL_NEED_HELP;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        return view;
    }

    private void GetTextContacts(){
        progressBar.setVisibility(View.VISIBLE);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        // Progress
        String finaltype_request = "contacts";
        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_GET_TEXT;
        String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String value = jsonObject.getString("value");
                    MainActivity.main_text_contacts = value;
                    textViewContacts.setText(value);


                } catch (JSONException e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();

                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
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

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

}