package com.example.appfond;

import static java.sql.DriverManager.println;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.print.PDFPrint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeraphyAdapter extends RecyclerView.Adapter<TeraphyAdapter.ViewHolder>{

    private Context context;
    public List<Teraphy> teraphy_list;
    //public String[] Subject = {};


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
        Button btnDelTer, btnEditTer, btnClearBeg, btnClearEnd;


        Boolean isAllowEdit = false;
        Boolean isCancel = false;
        String beforeName = "";
        String beforeCountry = "";
        String beforeDoz = "";
        String beforeDateBeg = "";
        String beforeDateEnd = "";

        DatePickerDialog datePickerBeg;
        DatePickerDialog datePickerEnd;



        private StringRequest mStringRequest;
        private RequestQueue mRequestQueue;


        private View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            mView = itemView;

            fieldIdCardFT = itemView.findViewById(R.id.fieldIdCardForTer);
            fieldIdTer = itemView.findViewById(R.id.fieldIdTer);
            fieldDateBegTer = itemView.findViewById(R.id.fieldDateBegTer);
            fieldDateEndTer = itemView.findViewById(R.id.fieldDateEndTer);
            fieldNameTer = itemView.findViewById(R.id.fieldNameTer);
            fieldCountryTer = itemView.findViewById(R.id.fieldCountryTer);
            fieldDozTer = itemView.findViewById(R.id.fieldDozTer);
            btnClearBeg = itemView.findViewById(R.id.buttonClearBeg);
            btnClearEnd = itemView.findViewById(R.id.buttonClearEnd);


            btnClearBeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fieldDateBegTer.setText("");
                }
            });

            btnClearEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fieldDateEndTer.setText("");
                }
            });

            initDatePickerBeg();
            initDatePickerEnd();

            fieldDateBegTer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerBeg.show();
                }
            });

            fieldDateEndTer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerEnd.show();
                }
            });

            btnEditTer = itemView.findViewById(R.id.buttonEditTer);
            btnEditTer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAllowEdit) {
                        //режим редактирования
                        if (!TextUtils.isEmpty(fieldNameTer.getText().toString()) && fieldNameTer.length()>2
                                //&& !TextUtils.isEmpty(fieldDiag.getSelectedItem().toString())
                                && !TextUtils.isEmpty(fieldCountryTer.getText().toString()) && fieldCountryTer.length()>2
                                && !TextUtils.isEmpty(fieldDozTer.getText().toString()) && fieldDozTer.length()>2
                                && !TextUtils.isEmpty(fieldDateBegTer.getText().toString())
                        ) {
                            isAllowEdit = false;
                            isCancel = false;

                            String tmp_valueId = fieldIdTer.getText().toString();
                            String tmp_name = fieldNameTer.getText().toString();
                            //String tmp_diag = fieldDiag.getSelectedItem().toString();
                            String tmp_country = fieldCountryTer.getText().toString();
                            String tmp_doz = fieldDozTer.getText().toString();
                            String tmp_date_beg = fieldDateBegTer.getText().toString();
                            String tmp_date_end = fieldDateEndTer.getText().toString();

                            // Toast.makeText(mView.getContext(), "cardId=" + valueID.getText().toString(),Toast.LENGTH_LONG).show();
                            pushEditTer(tmp_valueId,
                                    tmp_name,
                                    tmp_country,//getText().toString(),
                                    tmp_doz,
                                    tmp_date_beg,
                                    tmp_date_end);

                        } else {
                            Toast.makeText(mView.getContext(),"Ошибка! Проверьте введенные данные!" ,Toast.LENGTH_LONG).show();
                        }

                        //
                    } else {
                        //режим просмотра
                        beforeName = fieldNameTer.getText().toString();
                        beforeCountry = fieldCountryTer.getText().toString();
                        beforeDoz = fieldDozTer.getText().toString(); //.getText().toString();
                        beforeDateBeg = fieldDateBegTer.getText().toString();
                        beforeDateEnd = fieldDateEndTer.getText().toString();

                        goEnableFields(1);
                        isAllowEdit = true;
                        isCancel = true;
                    }
                }
            });

            btnDelTer = itemView.findViewById(R.id.buttonDelTer);
            btnDelTer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isCancel) {
                        //отмена
                        fieldNameTer.setText(beforeName);
                        fieldCountryTer.setText(beforeCountry);
                        fieldDozTer.setText(beforeDoz);
                        fieldDateBegTer.setText(beforeDateBeg);
                        fieldDateEndTer.setText(beforeDateEnd);
                        // fieldDiag.setText(beforeDiagnosis);

                        goEnableFields(0);
                        isAllowEdit = false;
                        isCancel = false;
                    }else {
                        AlertDialog alertDialogDel = new AlertDialog.Builder(mView.getContext())
                                //set icon
                                .setIcon(R.drawable.warning)
                                //set title
                                .setTitle("Внимание")
                                //set message
                                .setMessage("Вы действительно хотите выполнить удаление?")
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
                }
            });

        }



        private void initDatePickerBeg() {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    fieldDateBegTer.setText(date);
                }
            } ;

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            //int style = AlertDialog.THEME_HOLO_LIGHT;
            datePickerBeg = new DatePickerDialog(mView.getContext(), android.app.AlertDialog.THEME_HOLO_LIGHT,dateSetListener, year, month, day);
        }

        private void initDatePickerEnd() {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    fieldDateEndTer.setText(date);
                }
            } ;

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            //int style = AlertDialog.THEME_HOLO_LIGHT;
            datePickerEnd = new DatePickerDialog(mView.getContext(), android.app.AlertDialog.THEME_HOLO_LIGHT,dateSetListener, year, month, day);
        }

        private String makeDateString(int day, int month, int year) {
            if (month<10 && day>=10) {
                return year + "-0" + month + "-" + day;
            } else if (month>=10 && day<10) {
                return year + "-" + month + "-0" + day;
            } else if (month<10 && day<10) {
                return year + "-0" + month + "-0" + day;
            } else {
                return year + "-" + month + "-" + day;
            }
        }


        public void goEnableFields(Integer pval) {
            if (pval == 0){
                //режим просмотра
                fieldNameTer.setEnabled(false);
                fieldCountryTer.setEnabled(false);
                fieldDozTer.setEnabled(false);
                fieldDateBegTer.setEnabled(false);
                fieldDateEndTer.setEnabled(false);
                btnClearBeg.setEnabled(false);
                btnClearBeg.setVisibility(View.INVISIBLE);
                btnClearEnd.setEnabled(false);
                btnClearEnd.setVisibility(View.INVISIBLE);
                //border style


                btnEditTer.setText("Редактировать");
                //btEdit.setImage(UIImage(systemName: "pencil"), for: .normal)
                btnDelTer.setText("Удалить");
                //btDelete.setImage(UIImage(systemName: "trash"), for: .normal)
            } else {
                //режим редактирования
                fieldNameTer.setEnabled(true);
                fieldCountryTer.setEnabled(true);
                fieldDozTer.setEnabled(true);
                fieldDateBegTer.setEnabled(true);
                fieldDateEndTer.setEnabled(true);
                btnClearBeg.setEnabled(true);
                btnClearBeg.setVisibility(View.VISIBLE);
                btnClearEnd.setEnabled(true);
                btnClearEnd.setVisibility(View.VISIBLE);


                btnEditTer.setText("Сохранить");
                //btEdit.setImage(UIImage(systemName: "doc.fill"), for: .normal)
                btnDelTer.setText("Отмена");
                //btDelete.setImage(UIImage(systemName: "arrowshape.turn.up.backward"), for: .normal)
            }
        }

        /*

        public func pushEditTer(ter_id: Int, name_ter: String, country_ter: String, doz_ter: String, date_beg_ter: String, date_end_ter: String, completion: @escaping (Bool) -> Void) {

    //print("ter_id=\(ter_id)")
    let param_value: Parameters = ["newname":name_ter,"ter_id":ter_id,"newcountry":country_ter,"newdoz":doz_ter,"newbegter":date_beg_ter,"newendter":date_end_ter]

public func pushCreateTer(card_id: String, name_ter: String, country_ter: String, doz_ter: String, date_beg_ter: String, date_end_ter: String, completion: @escaping (Bool) -> Void) {

    //print("ter_id=\(ter_id)")
    let param_value: Parameters = ["newname":name_ter,"card_id":card_id,"newcountry":country_ter,"newdoz":doz_ter,"newbegter":date_beg_ter,"newendter":date_end_ter]

    request(URL_NEW_TERAPHY, method: .post, parameters:


        */

        public void pushEditTer(String ter_id, String name_ter, String country_ter, String doz_ter, String date_beg_ter, String date_end_ter){

            mRequestQueue = Volley.newRequestQueue(mView.getContext());
            // Progress
            //String finaltype_request = "check_user";
            HTTPSBase Global = new HTTPSBase();
            String URL = Global.URL_GET_EDTERAPHY;
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
                    params.put("newname", name_ter);
                    params.put("ter_id",ter_id);
                    params.put("newcountry",country_ter);
                    params.put("newdoz",doz_ter);
                    params.put("newbegter",date_beg_ter);
                    params.put("newendter",date_end_ter);

                    return params;
                }
            };

            mStringRequest.setShouldCache(false);
            mRequestQueue.add(mStringRequest);
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
            if (date_end.equals("0000-00-00")) { date_end = null; }
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
