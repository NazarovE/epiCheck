package com.example.appfond;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.print.PDFPrint;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

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
//import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TeraphyActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;
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
        tempCardName = getIntent().getSerializableExtra("tempCardName").toString();
        tempCardBD = getIntent().getSerializableExtra("tempCardBD").toString();

        btnNewTer = findViewById(R.id.buttonNewTeraphy);
        btnNewTer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newTerIntent = new Intent(TeraphyActivity.this, NewTeraphyActivity.class);
                newTerIntent.setAction(Intent.ACTION_SEND);
                newTerIntent.putExtra("tempCardId", tempCardId);
                startActivity(newTerIntent);
            }
        });

        swtTer = findViewById(R.id.switchActive);
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

        btnPDFTer = findViewById(R.id.buttonPDFTer);
        btnPDFTer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermission()) {
                    // Toast.makeText(HistoryEpisodeActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    requestPermission();
                }

                if (checkPermission()) {

                    //clear path
                    FileManager.getInstance().cleanTempFolder(getApplicationContext());

                    final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
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
                    PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile, tmpHtml , new PDFPrint.OnPDFPrintListener() {
                        @Override
                        public void onSuccess(File file) {

                            Intent intentPdfViewer = new Intent(TeraphyActivity.this, PDFViewActivity.class);
                            //intentPdfViewer.putExtra(PDFViewActivity.PDF_FILE_URI, String.valueOf(savedPDFFile));
                            MainActivity.pdffile = savedPDFFile;

                            try {
                                createLogPDF(tempCardId,"teraphy_activity");
                                startActivity(intentPdfViewer);
                            }
                            catch (ActivityNotFoundException e) {
                                Toast.makeText(TeraphyActivity.this,
                                        "No Application available to viewPDF",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onError(Exception exception) {

                            exception.printStackTrace();
                        }
                    });
                }
            }
        });


        getTeraphy();


    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
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
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
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

    public void createLogPDF(String card_id, String type_log){

        progressBarTer.setVisibility(View.VISIBLE);
        mRequestQueue = Volley.newRequestQueue(TeraphyActivity.this);

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
}