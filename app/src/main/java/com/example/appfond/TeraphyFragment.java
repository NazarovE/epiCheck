package com.example.appfond;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.print.PDFPrint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeraphyFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Toolbar toolbarTer;
    private RecyclerView teraphy_list_view;
    private List<Teraphy> teraphy_list;
    Teraphy teraphy;
    TeraphyAdapter adapter;
    private ProgressBar progressBarTer;
    String tempCardId;
    String tempCardName;
    String tempCardBD;

    private Switch swtTer;

    private Button btnNewTer, btnPDFTer;

    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teraphy, container, false);

        MainActivity.from_add = 1;

        Boolean getForm;

        if ((MainActivity.User_id.equals("0")) && (GlobalVariables.globalCardId == 0)) {
            Intent mainIntent = new Intent(getActivity().getApplicationContext(), UnLoginProfileViewActivity.class);
            startActivity(mainIntent);
            getForm = false;
        } else if (!(MainActivity.User_id.equals("0")) && (GlobalVariables.globalCardId == 0)){
            Intent cardIntent = new Intent(getActivity().getApplicationContext(), NoCardActivity.class);
            startActivity(cardIntent);
            getForm = false;
        } else {

            getForm = true;
            teraphy_list = new ArrayList<>();
            teraphy_list_view = view.findViewById(R.id.ter_listF);
            progressBarTer = view.findViewById(R.id.progressBarTerF);

            adapter = new TeraphyAdapter(getActivity().getApplicationContext(), teraphy_list);
            teraphy_list_view.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            teraphy_list_view.setAdapter(adapter);

            //get tempCardId;
            tempCardId = String.valueOf(GlobalVariables.globalCardId);
            tempCardName = GlobalVariables.globalCardName;
            tempCardBD = GlobalVariables.globalCardBD;

            btnNewTer = view.findViewById(R.id.buttonNewTeraphyF);
            btnNewTer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newTerIntent = new Intent(getActivity().getApplicationContext(), NewTeraphyActivity.class);
                    newTerIntent.setAction(Intent.ACTION_SEND);
                    newTerIntent.putExtra("tempCardId", tempCardId);
                    startActivity(newTerIntent);
                }
            });

            swtTer = view.findViewById(R.id.switchActiveF);
            if (MainActivity.isShowRealTer == 0) {
                swtTer.setChecked(false);
            }else{
                swtTer.setChecked(true);
            }
            swtTer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (swtTer.isChecked()) {
                        MainActivity.isShowRealTer = 1;
                    } else {
                        MainActivity.isShowRealTer = 0;
                    }
                    getTeraphy();
                }
            });

            btnPDFTer = view.findViewById(R.id.buttonPDFTerF);
            btnPDFTer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*if (!checkPermission()) {
                        requestPermission();
                    }*/

                    if (checkStoragePermissions()) {
                        Toast.makeText(requireContext(), "Разрешения уже предоставлены!", Toast.LENGTH_SHORT).show();
                    } else {
                        requestStoragePermissions();
                    }

                    if (/*checkPermission()*/true) {

                        //clear path
                        FileManager.getInstance().cleanTempFolder(getActivity().getApplicationContext());

                        final File savedPDFFile = FileManager.getInstance().createTempFile(getActivity().getApplicationContext(), "pdf", false);
                        // Generate Pdf From Html

                        String tmpHtml = " <!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<body>\n" +
                                "\n" +
                                "<h1>Тепатия</h1>\n" +
                                "<p>Имя: " + tempCardName + "</p>\n" +
                                "<p>Дата рождения: " + tempCardBD + "</p>\n" +
                                "\n" +
                                "<table border=\"1\"><tr>" +
                                "<th>Название</th><th>Производитель</th><th>Дозировка</th><th>Дата ввода</th><th>Дата вывода</th>" +
                                "</tr>";
                        for (int i=0;i<teraphy_list.size();i++) {
                            String tmp_date_end = teraphy_list.get(i).date_end;
                            if (tmp_date_end.equals("0000-00-00")) {
                                tmp_date_end = "";
                            }
                            tmpHtml = tmpHtml + "<tr>" +
                                    "<td>"+teraphy_list.get(i).name_ter+"</td>" +
                                    "<td>"+teraphy_list.get(i).country_ter+"</td>" +
                                    "<td>"+teraphy_list.get(i).doz_ter+"</td>" +
                                    "<td>"+teraphy_list.get(i).date_begin+"</td>" +
                                    "<td>"+tmp_date_end+"</td></tr>";
                        }
                        tmpHtml = tmpHtml + "</table>" +
                                "</body>\n" +
                                "</html> ";
                        PDFUtil.generatePDFFromHTML(getActivity().getApplicationContext(), savedPDFFile, tmpHtml , new PDFPrint.OnPDFPrintListener() {
                            @Override
                            public void onSuccess(File file) {

                                Intent intentPdfViewer = new Intent(getActivity().getApplicationContext(), PDFViewActivity.class);
                                //intentPdfViewer.putExtra(PDFViewActivity.PDF_FILE_URI, String.valueOf(savedPDFFile));
                                MainActivity.pdffile = savedPDFFile;

                                try {
                                    createLogPDF(tempCardId,"teraphy_activity");
                                    startActivity(intentPdfViewer);
                                }
                                catch (ActivityNotFoundException e) {
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            "No Application available to viewPDF",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(Exception exception) {

                                exception.printStackTrace();
                            }
                        });
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Не разрешено использование файловой системы :(",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });


            getTeraphy();

        }
        if (getForm) {
            return view;
        } else {
            return null;
        }
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(requireActivity(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(requireActivity(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission2() {
        // requesting permissions if not provided.
        if (getContext().getApplicationContext() != null) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

        }
    }

    private void requestPermission() {
        Activity activity = getActivity();
        if (activity != null && activity instanceof Activity) {
            ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Фрагмент не прикреплён к активности или активность неправильная.", Toast.LENGTH_LONG).show();
            //Log.e("Permissions", );
        }
    }




    private void getTeraphy() {
        progressBarTer.setVisibility(View.VISIBLE);
        HTTPSBase Global = new HTTPSBase();
        String url;
        if (MainActivity.isShowRealTer == 0) {
            url = Global.URL_GET_TERAPHY_ARCH + "?id_card=" + tempCardId;
        } else {
            url = Global.URL_GET_TERAPHY_REAL + "?id_card=" + tempCardId;
        }
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
                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);
    }

    public void createLogPDF(String card_id, String type_log){

        progressBarTer.setVisibility(View.VISIBLE);
        mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        HTTPSBase Global = new HTTPSBase();
        String URL = Global.URL_PDF_LOG;

        mStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("type", type_log);
                params.put("card_id",card_id);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }

    // Проверка разрешений
    private boolean checkStoragePermissions() {
        int readPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED;
    }

    // Запрос разрешений
    private void requestStoragePermissions() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Покажите объяснение пользователю (опционально)
            Toast.makeText(getContext(), "Нужно разрешение для работы с хранилищем.", Toast.LENGTH_LONG).show();
        }
        requestPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE
        );
    }

    // Действие после получения разрешений
    private void accessStorage() {
        Toast.makeText(getContext(), "Доступ к хранилищу открыт!", Toast.LENGTH_SHORT).show();
        // Здесь вы можете выполнить действия с хранилищем
    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(getActivity().getApplicationContext(), "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешения предоставлены
                Toast.makeText(getContext(), "Доступ к хранилищу предоставлен!", Toast.LENGTH_SHORT).show();
            } else {
                // Разрешения отклонены
                Toast.makeText(getContext(), "Разрешения отклонены. Невозможно получить доступ к хранилищу.", Toast.LENGTH_SHORT).show();
            }
        }
    }





}
