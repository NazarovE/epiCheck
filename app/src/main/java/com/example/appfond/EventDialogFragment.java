package com.example.appfond;
import static java.sql.DriverManager.println;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EventDialogFragment extends BottomSheetDialogFragment {

    private ProgressBar progressBarEvent;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private TextView eventText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_event_dialog, container, false);

        progressBarEvent = view.findViewById(R.id.progressBarEvent);

        eventText = view.findViewById(R.id.textEvent);
        eventText.setText(GlobalVariables.lastEventText);

        // Предположим, у вас есть кнопка закрытия в вашем фрагменте
        Button closeButton = view.findViewById(R.id.buttonEventOk);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("id_event (event)= " + GlobalVariables.id_event);
                System.out.println("id_user (event) = " + MainActivity.User_id);
                //progressBarEvent.setVisibility(View.VISIBLE);
                createReadEvent(Integer.valueOf(MainActivity.User_id), GlobalVariables.id_event);
                progressBarEvent.setVisibility(View.INVISIBLE);
                // Закрытие диалога
                dismiss();
                //onDestroy();
                //dismissAllowingStateLoss();

            }
        });

        return view;
    }

    public void createReadEvent(Integer user_id, Integer event_id){

        progressBarEvent.setVisibility(View.VISIBLE);
        mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        // Progress

        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_READ_EVENT;
        //String finalType_request = finaltype_request;
        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    println("message=" + message);
                    if (message.equals("0")) {
                        GlobalVariables.wasLatestEvent = 1;
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(),"Ошибка! Проверьте введенные данные: "+ e.toString(),Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity().getApplicationContext(),"Ошибка! Проверьте введенные данные: "+error.toString(),Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();

                params.put("user_id", String.valueOf(user_id));
                params.put("event_id", String.valueOf(event_id));

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

}
