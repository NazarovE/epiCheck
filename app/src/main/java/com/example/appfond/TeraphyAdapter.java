package com.example.appfond;

import static java.sql.DriverManager.println;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeraphyAdapter extends RecyclerView.Adapter<TeraphyAdapter.ViewHolder>{

    private Context context;
    public List<Teraphy> teraphy_list;
    public String[] Subject = {};


    public TeraphyAdapter(Context context, List<Teraphy> teraphy_list){
        this.context = context;
        this.teraphy_list = teraphy_list;
    }

    @NonNull
    @Override
    public TeraphyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ter_list_item, parent, false);

        return new TeraphyAdapter.ViewHolder(view);
    }

    @Override
    // public void onBindViewHolder(@NonNull BlogRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
    public void onBindViewHolder(@NonNull TeraphyAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String title_id = teraphy_list.get(position).getId_ter();
        String title_name = teraphy_list.get(position).getName_ter();
        String title_country = teraphy_list.get(position).getCountry_ter();
        String title_doz = teraphy_list.get(position).getDoz_ter();
        String title_date_beg = teraphy_list.get(position).getDate_begin();
        String title_date_end = teraphy_list.get(position).getDate_end();
        String card_id = teraphy_list.get(position).getId_card();


        holder.setNameTer(title_name);
        holder.setDozTer(title_doz);
        holder.setCountryTer(title_country);
        holder.setIdCard(card_id);
        holder.setIdTer(title_id);
        holder.setDateBeg(title_date_beg);
        holder.setDateEnd(title_date_end);


    }

    @Override
    public int getItemCount() {
        return teraphy_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView fieldIdCardFT, fieldIdTer;
        EditText fieldNameTer, fieldCountryTer, fieldDozTer, fieldDateBegTer, fieldDateEndTer;
        Button btnDelTer, btnEditTer;

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
            fieldIdCardFT = itemView.findViewById(R.id.fieldIdCardForTer);
            fieldIdTer = itemView.findViewById(R.id.fieldIdTer);
            fieldDateBegTer = itemView.findViewById(R.id.fieldDateBegTer);
            fieldDateEndTer = itemView.findViewById(R.id.fieldDateEndTer);
            fieldNameTer = itemView.findViewById(R.id.fieldNameTer);
            fieldCountryTer = itemView.findViewById(R.id.fieldCountryTer);
            fieldDozTer = itemView.findViewById(R.id.fieldDozTer);

            btnDelTer = itemView.findViewById(R.id.buttonDelTer);
            btnDelTer.setOnClickListener(new View.OnClickListener() {
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
                                    String tmp_id_ter = fieldIdTer.getText().toString();
                                    postDeleteTer(tmp_id_ter);
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


        public void setNameTer(String name_ter){
            fieldNameTer = mView.findViewById(R.id.fieldNameTer);
            fieldNameTer.setText(name_ter);

        }

        public void setDozTer(String doz_ter){
            fieldDozTer = mView.findViewById(R.id.fieldDozTer);
            fieldDozTer.setText(doz_ter);

        }

        public void setCountryTer(String country_ter){
            fieldCountryTer = mView.findViewById(R.id.fieldCountryTer);
            fieldCountryTer.setText(country_ter);

        }

        public void setDateBeg(String date_beg){
            fieldDateBegTer = mView.findViewById(R.id.fieldDateBegTer);
            fieldDateBegTer.setText(date_beg);
        }

        public void setDateEnd(String date_end){
            fieldDateEndTer = mView.findViewById(R.id.fieldDateEndTer);
            fieldDateEndTer.setText(date_end);
        }

        public void setIdCard(String idCard){
            fieldIdCardFT = mView.findViewById(R.id.fieldIdCardForTer);
            fieldIdCardFT.setText(idCard);
        }

        public void setIdTer(String idTer){
            fieldIdTer = mView.findViewById(R.id.fieldIdTer);
            fieldIdTer.setText(idTer);
        }




        public void postDeleteTer(String ter_id){

            mRequestQueue = Volley.newRequestQueue(mView.getContext());
            // Progress

            HTTPSBase Global = new HTTPSBase();
            String URL = Global.URL_GET_DELTERAPHY;

            mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String message = jsonObject.getString("message");

                        println("message=" + message);
                        if (message.equals("0")) {
                            // Reload current fragment
                            teraphy_list.remove(getAdapterPosition());
                            notifyItemRemoved(getPosition());
                        }

                    } catch (JSONException e) {
                        Toast.makeText(mView.getContext(),"Ошибка! Не удалось удалить: "+ e.toString(),Toast.LENGTH_LONG).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(mView.getContext(),"Ошибка! Не удалось удалить: "+error.toString(),Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("ter_id",ter_id);

                    return params;
                }
            };

            mStringRequest.setShouldCache(false);
            mRequestQueue.add(mStringRequest);
        }


    }




}
