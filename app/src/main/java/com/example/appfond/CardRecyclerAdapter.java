package com.example.appfond;

import static java.security.AccessController.getContext;
import static java.sql.DriverManager.println;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassifierEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>{

    private Context context;
    public List<Cards> diag_list;
    public String[] Subject = {};

    public CardRecyclerAdapter(Context context, List<Cards> diag_list){
        this.context = context;
        this.diag_list = diag_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diag_list_item, parent, false);

        return new CardRecyclerAdapter.ViewHolder(view);
    }

    @Override
   // public void onBindViewHolder(@NonNull BlogRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
    public void onBindViewHolder(@NonNull CardRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String title_name = diag_list.get(position).name_card;
        String title_birthday = diag_list.get(position).birthday;
        String title_diag = diag_list.get(position).name_diag;
        String title_desc = diag_list.get(position).card_comment;
        String card_id = diag_list.get(position).id_card;



        holder.setNameText(title_name);
        holder.setBirthdayText(title_birthday);
        holder.setDiagText(title_diag);
        holder.setDescText(title_desc);
        holder.setIdCard(card_id);
        holder.setDiagTextTmp(title_diag);
    }

    @Override
    public int getItemCount() {
        return diag_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView fieldName, valueID, fieldTmpDiag, fieldIdCard;
        EditText fieldBirthday, fieldDesc;
        Spinner fieldDiag;
        Button fix_episod, EditCard, DelCard;
        Boolean isAllowEdit = false;
        Boolean isCancel = false;
        String beforeName = "";
        String beforeAge = "";
        String beforeDiagnosis = "";
        String beforeComment = "";
        //DatePicker datePicker2;
        DatePickerDialog datePickerDialog;
       // Diagnos diagval;
       //public List<Diagnos> diag_values;
       // public List<Diagnos> myDiag;
        ///Integer idCard = 0;




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
            valueID = itemView.findViewById(R.id.fieldIdCard);
            fieldName = itemView.findViewById(R.id.fieldCardPersonName);
            fieldBirthday = itemView.findViewById(R.id.fieldCardBirthday);
            fieldDiag = itemView.findViewById(R.id.fieldCardDiag);
            fieldDesc = itemView.findViewById(R.id.fieldCardDesc);
            fieldTmpDiag = mView.findViewById(R.id.textDiagTmp);
            fieldIdCard = mView.findViewById(R.id.fieldIdCard);

            if (Subject.length == 0) {
                getDiagValues();
            }


            fix_episod = itemView.findViewById(R.id.buttonFixEpi);
            EditCard = itemView.findViewById(R.id.buttonEditCard);
            DelCard = itemView.findViewById(R.id.buttonDelCard);

            initDatePicker();

            fieldBirthday.setText(getTodayDate());
            fieldBirthday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDialog.show();
                }
            });



            DelCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCancel) {
                        //отмена
                        fieldName.setText(beforeName);
                        fieldBirthday.setText(beforeAge);
                        fieldDesc.setText(beforeComment);
                       // fieldDiag.setText(beforeDiagnosis);

                        goEnableFields(0);
                        isAllowEdit = false;
                        isCancel = false;
                    }else{
                        //удаление

                        AlertDialog alertDialogDel = new AlertDialog.Builder(mView.getContext())
                                //set icon
                                .setIcon(R.drawable.warning)
                                //set title
                                .setTitle("Внимание")
                                //set message
                                .setMessage("Вы действительно хотите удалить диагноз? Это действие необратимо!")
                                //set positive button
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //set what would happen when positive button is clicked
                                        String tmp_card_id = fieldIdCard.getText().toString();
                                        postDeleteCard(tmp_card_id);
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
                }
            });

            EditCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAllowEdit) {
                        //режим редактирования
                        if (!TextUtils.isEmpty(fieldName.getText().toString()) && fieldName.length()>2 && !TextUtils.isEmpty(fieldDiag.getSelectedItem().toString())) {
                            isAllowEdit = false;
                            isCancel = false;
                            Toast.makeText(mView.getContext(), "cardId=" + valueID.getText().toString(),Toast.LENGTH_LONG).show();
                            pushEditCard(valueID.getText().toString(), fieldName.getText().toString(),
                                    fieldDiag.getSelectedItem().toString(),//getText().toString(),
                                    fieldDesc.getText().toString(),
                                    fieldBirthday.getText().toString());
                        } else {
                            Toast.makeText(mView.getContext(),"Ошибка! Проверьте введенные данные!" ,Toast.LENGTH_LONG).show();
                        }

                        //
                    } else {
                        //режим просмотра
                        beforeName = fieldName.getText().toString();
                        beforeAge = fieldBirthday.getText().toString();
                       // beforeDiagnosis = fieldDiag.getSelectedItem().toString(); //.getText().toString();
                        beforeComment = fieldDesc.getText().toString();

                        goEnableFields(1);
                        isAllowEdit = true;
                        isCancel = true;
                    }
                }
            });

        }

        private String getTodayDate() {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            month = month + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return makeDateString(day, month, year);
        }

        private void initDatePicker() {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    fieldBirthday.setText(date);
                }
            } ;

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            //int style = AlertDialog.THEME_HOLO_LIGHT;
            datePickerDialog = new DatePickerDialog(mView.getContext(), android.app.AlertDialog.THEME_HOLO_LIGHT,dateSetListener, year, month, day);
        }

        private String makeDateString(int day, int month, int year) {
            return year + "-" + month + "-" + day;
        }



        public void goEnableFields(Integer pval) {
            if (pval == 0){
                //режим просмотра
                fieldName.setEnabled(false);
                fieldBirthday.setEnabled(false);
                fieldDiag.setEnabled(false);
                fieldDesc.setEnabled(false);
                //border style


                if (TextUtils.isEmpty(fieldDesc.getText().toString())) {
                    fieldDesc.setText("Здесь вы можете добавить несколько слов о Вашем ребенке...");
                }

                EditCard.setText("Редактировать");
                //btEdit.setImage(UIImage(systemName: "pencil"), for: .normal)
                DelCard.setText("Удалить");
                //btDelete.setImage(UIImage(systemName: "trash"), for: .normal)
            } else {
                //режим редактирования
                fieldName.setEnabled(true);
                fieldBirthday.setEnabled(true);
                fieldDiag.setEnabled(true);
                fieldDesc.setEnabled(true);


                if (fieldDesc.getText().toString() == "Здесь вы можете добавить несколько слов о Вашем ребенке...") {
                    fieldDesc.setText("");
                }

                EditCard.setText("Сохранить");
                //btEdit.setImage(UIImage(systemName: "doc.fill"), for: .normal)
                DelCard.setText("Отмена");
                //btDelete.setImage(UIImage(systemName: "arrowshape.turn.up.backward"), for: .normal)
            }
        }

        public void setNameText(String Titletext){
            fieldName = mView.findViewById(R.id.fieldCardPersonName);
            fieldName.setText(Titletext);
        }

        public void setBirthdayText(String Datetext){
            fieldBirthday = mView.findViewById(R.id.fieldCardBirthday);
            fieldBirthday.setText(Datetext);
        }

        public void setDiagText(String Diagtext){
            fieldDiag = mView.findViewById(R.id.fieldCardDiag);
            fieldTmpDiag = mView.findViewById(R.id.textDiagTmp);
            for (int i=0; i< Subject.length ;i++) {
                if (Subject[i] == fieldTmpDiag.getText().toString()) {
                    fieldDiag.setSelection(i);
                }
            }

        }

        public void setDiagTextTmp(String DiagTmptext){
            fieldTmpDiag = mView.findViewById(R.id.textDiagTmp);
            fieldTmpDiag.setText(DiagTmptext);


        }

        public void setDescText(String Desctext){
            fieldDesc = mView.findViewById(R.id.fieldCardDesc);
            fieldDesc.setText(Desctext);
        }

        public void setIdCard(String idCard){
            valueID = mView.findViewById(R.id.fieldIdCard);
            valueID.setText(idCard);
        }

        public void pushEditCard(String card_id, String name_card, String name_diagnosis, String comm, String birthday){

            mRequestQueue = Volley.newRequestQueue(mView.getContext());
            // Progress
            //String finaltype_request = "check_user";
            HTTPSBase Global = new HTTPSBase();
            String URL = Global.URL_EDIT_CARD;
            //String finalType_request = finaltype_request;
            mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String message = jsonObject.getString("message");

                        println("message=" + message);
                        if (message.equals("0")) {
                            goEnableFields(0);
                            isAllowEdit = false;
                            isCancel = false;
                        }

                    } catch (JSONException e) {
                        Toast.makeText(mView.getContext(),"Ошибка! Проверьте введенные данные: "+ e.toString(),Toast.LENGTH_LONG).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(mView.getContext(),"Ошибка! Проверьте введенные данные: "+error.toString(),Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("newname", name_card);
                    params.put("card_id",card_id);
                    params.put("newdiag",name_diagnosis);
                    params.put("newcomm",comm);
                    params.put("newage",birthday);

                    return params;
                }
            };

            mStringRequest.setShouldCache(false);
            mRequestQueue.add(mStringRequest);
        }

        public void postDeleteCard(String card_id){

            mRequestQueue = Volley.newRequestQueue(mView.getContext());
            // Progress
            //String finaltype_request = "check_user";
            HTTPSBase Global = new HTTPSBase();
            String URL = Global.URL_DEL_CARD;
            //String finalType_request = finaltype_request;
            mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String message = jsonObject.getString("message");

                        println("message=" + message);
                        if (message.equals("0")) {
                            // Reload current fragment
                            diag_list.remove(getAdapterPosition());
                            notifyItemRemoved(getPosition());

                        }

                    } catch (JSONException e) {
                        Toast.makeText(mView.getContext(),"Ошибка! Не удалось удалить диагноз: "+ e.toString(),Toast.LENGTH_LONG).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(mView.getContext(),"Ошибка! Не удалось удалить диагноз: "+error.toString(),Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("card_id",card_id);

                    return params;
                }
            };

            mStringRequest.setShouldCache(false);
            mRequestQueue.add(mStringRequest);
        }




        private void getDiagValues() {
//        Toast.makeText(HomeFragment.this, "getMessage", Toast.LENGTH_LONG).show();
            //progressBarHome.setVisibility(View.VISIBLE);
            HTTPSBase Global = new HTTPSBase();
            String url = Global.URL_GET_DIAGNOSIS;
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //MainActivity.diag_values.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        //String success = "0";
                        //success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("diagnosis");
                        //Toast.makeText(MainActivity.this, success + "" + jsonArray.length(), Toast.LENGTH_LONG).show();
                        //if (success.equals("1")) {
                        String[] temp = new String[jsonArray.length()];
                        Integer tempDiag;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String name_card = object.getString("name");
                            temp[i] = name_card;

                        }
                        //}

                        Subject = temp;

                        ArrayAdapter<String> adapter = new ArrayAdapter(mView.getContext(), android.R.layout.simple_spinner_item, Subject);
                        // ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.simple_spinner_item,Subject );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        fieldDiag.setAdapter(adapter);

                        fieldTmpDiag = mView.findViewById(R.id.textDiagTmp);
                        String tmlVal = fieldTmpDiag.getText().toString();
                        for (int i=0; i< Subject.length ;i++) {
                            if (Subject[i].equals(tmlVal)) {
                                fieldDiag.setSelection(i);
                            }
                        }

                        fieldDiag.setEnabled(false);


                    } catch (Exception e) {
                        //progressBarHome.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                        Toast.makeText(mView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mView.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(mView.getContext());
            requestQueue.add(request);
        }

    }




}
