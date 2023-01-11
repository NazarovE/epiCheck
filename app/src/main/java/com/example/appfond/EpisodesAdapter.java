package com.example.appfond;

import static java.sql.DriverManager.println;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder>{

    private Context context;
    public List<Episodes> episodes_list;
    public String[] Subject = {};


    public EpisodesAdapter(Context context, List<Episodes> episodes_list){
        this.context = context;
        this.episodes_list = episodes_list;
    }

    @NonNull
    @Override
    public EpisodesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_list_item, parent, false);

        return new EpisodesAdapter.ViewHolder(view);
    }

    @Override
    // public void onBindViewHolder(@NonNull BlogRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
    public void onBindViewHolder(@NonNull EpisodesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String title_date = episodes_list.get(position).getDate();
        String title_comment = episodes_list.get(position).getComment();
        String card_id = episodes_list.get(position).getIdCard();
        String episode_id = episodes_list.get(position).getIdEpisode();

        holder.setDateTimeText(title_date);
        holder.setComment(title_comment);
        holder.setIdCard(card_id);
        holder.setIdEpisode(episode_id);

    }

    @Override
    public int getItemCount() {
        return episodes_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView fieldIdEpisode, fieldDateTimeEpisode, fieldIdCard, fieldCommentEpi;
        EditText fieldBirthday, fieldDesc;
        Spinner fieldDiag;
        Button fix_episod, EditCard, DelCard, btnHistory, btnDelEpi;

        //DatePicker datePicker2;
        DatePickerDialog datePickerDialog;





        private StringRequest mStringRequest;
        private RequestQueue mRequestQueue;


        private View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            mView = itemView;
            //--------------------------------------------------------------------------------------
            //System.out.println(String.valueOf("l="+String.valueOf(diag_values.size())));

            // System.out.println(String.valueOf("l="+String.valueOf(diag_values.size())));
            //System.out.println(String.valueOf("l="+String.valueOf(Subject.length)));
            //--------------------------------------------------------------------------------------
            fieldIdCard = itemView.findViewById(R.id.idCard);
            fieldIdEpisode = itemView.findViewById(R.id.idEpi);
            fieldDateTimeEpisode = itemView.findViewById(R.id.fieldDateTimeEpi);
            fieldCommentEpi = itemView.findViewById(R.id.textCommentEpi);
            btnDelEpi = itemView.findViewById(R.id.buttonDelEpi);
            btnDelEpi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialogDel = new AlertDialog.Builder(mView.getContext())
                            //set icon
                            .setIcon(R.drawable.warning)
                            //set title
                            .setTitle("Внимание")
                            //set message
                            .setMessage("Вы действительно хотите удалить данный приступ?")
                            //set positive button
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what would happen when positive button is clicked
                                    String tmp_id_episode = fieldIdEpisode.getText().toString();
                                    postDeleteEpi(tmp_id_episode);
                                    //finish();
                                }
                            })
                            //set negative button
                            .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what should happen when negative button is clicked
                                    //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                }
                            })
                            .show();
                }
            });

        }




        public void setDateTimeText(String datetime){
            fieldDateTimeEpisode = mView.findViewById(R.id.fieldDateTimeEpi);
            fieldDateTimeEpisode.setText(datetime);

        }

        public void setComment(String DiagTmptext){
            fieldCommentEpi = mView.findViewById(R.id.textCommentEpi);
            fieldCommentEpi.setText(DiagTmptext);

        }

        public void setIdCard(String idCard){
            fieldIdCard = mView.findViewById(R.id.idCard);
            fieldIdCard.setText(idCard);
        }

        public void setIdEpisode(String idEpisode){
            fieldIdCard = mView.findViewById(R.id.idEpi);
            fieldIdCard.setText(idEpisode);
        }




        public void postDeleteEpi(String episode_id){

            mRequestQueue = Volley.newRequestQueue(mView.getContext());
            // Progress

            HTTPSBase Global = new HTTPSBase();
            String URL = Global.URL_DEL_EPISODE;

            mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String message = jsonObject.getString("message");

                        println("message=" + message);
                        if (message.equals("0")) {
                            // Reload current fragment
                            episodes_list.remove(getAdapterPosition());
                            notifyItemRemoved(getPosition());
                        }

                    } catch (JSONException e) {
                        Toast.makeText(mView.getContext(),"Ошибка! Не удалось удалить приступ: "+ e.toString(),Toast.LENGTH_LONG).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(mView.getContext(),"Ошибка! Не удалось удалить приступ: "+error.toString(),Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("episode_id",episode_id);

                    return params;
                }
            };

            mStringRequest.setShouldCache(false);
            mRequestQueue.add(mStringRequest);
        }


    }




}
