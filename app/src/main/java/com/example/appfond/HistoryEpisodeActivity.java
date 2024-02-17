package com.example.appfond;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;

import static com.example.appfond.MainActivity.pdffile;

import static java.sql.DriverManager.println;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.print.PDFPrint;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;
import com.tejpratapsingh.pdfcreator.views.PDFTableView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFTextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class HistoryEpisodeActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;

    private Toolbar toolbarHistory;
    private RecyclerView episode_list_view;
    private List<Episodes> episode_list;
    Episodes episodes;
    EpisodesAdapter adapter;
    private ProgressBar progressBarEpi;
    private Button btnUpdateData;
    private Button btntoPDF;
    private TextView countEpisodes;
    private EditText edDateBegin;
    private EditText edDateEnd;
    private DatePickerDialog datePickerDialogBegin;
    private DatePickerDialog datePickerDialogEnd;
    String tempCardId;
    String tempCardName;
    String tempCardBD;
    BarChart barChart;

    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;

    Integer currentNightMode;

    /*@Override
    public void onBackPressed()
    {
        Intent intent = new Intent(HistoryEpisodeActivity.this,DiagnosFragment.class);
        startActivity(intent);
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_episode);

        MainActivity.from_add = 1;
        toolbarHistory = findViewById(R.id.toolbarHistory);
        setSupportActionBar(toolbarHistory);
        getSupportActionBar().setTitle("Назад");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        episode_list = new ArrayList<>();
        episode_list_view = findViewById(R.id.episode_list_view);
        progressBarEpi = findViewById(R.id.progressBarHistEp);

        btnUpdateData = findViewById(R.id.buttonUpdateData);
        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEpisodes();
                getEpisodesForChart();
            }
        });

        btntoPDF = findViewById(R.id.buttonToPDF);
        btntoPDF.setOnClickListener(new View.OnClickListener() {
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
                            "<h1>Дневник приступов</h1>\n" +
                            "<p>Имя: " + tempCardName + "</p>\n" +
                            "<p>Дата рождения: " + tempCardBD + "</p>\n" +
                            "\n" +
                            "<table border=\"1\"><tr>" +
                            "<th>Дата</th><th>Описание</th>" +
                            "</tr>";
                    for (int i=0;i<episode_list.size();i++) {
                        tmpHtml = tmpHtml + "<tr><td>"+episode_list.get(i).date+"</td><td>"+episode_list.get(i).comment+"</td></tr>";
                    }
                    tmpHtml = tmpHtml + "</table>" +
                            "</body>\n" +
                            "</html> ";
                    PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile, tmpHtml , new PDFPrint.OnPDFPrintListener() {
                        @Override
                        public void onSuccess(File file) {

                            createLogPDF(tempCardId,"history_activity");

                            Intent intentPdfViewer = new Intent(HistoryEpisodeActivity.this, PDFViewActivity.class);
                            //intentPdfViewer.putExtra(PDFViewActivity.PDF_FILE_URI, String.valueOf(savedPDFFile));
                            MainActivity.pdffile = savedPDFFile;

                            try {
                                startActivity(intentPdfViewer);
                            }
                            catch (ActivityNotFoundException e) {
                                Toast.makeText(HistoryEpisodeActivity.this,
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

        countEpisodes = findViewById(R.id.labelCountPeriod);
        edDateBegin = findViewById(R.id.fieldDatePerBeg);
        edDateEnd = findViewById(R.id.fieldDatePerEnd);

        initDatePickerBegin();
        initDatePickerEnd();

        edDateBegin.setText(getYesterDay());
        edDateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogBegin.show();
            }
        });
        edDateEnd.setText(getTodayDate());
        edDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogEnd.show();
            }
        });


        adapter = new EpisodesAdapter(getApplicationContext(), episode_list);
        episode_list_view.setLayoutManager(new LinearLayoutManager(HistoryEpisodeActivity.this));
        episode_list_view.setAdapter(adapter);

        //get tempCardId;
        tempCardId = getIntent().getSerializableExtra("tempCardId").toString();
        tempCardName = getIntent().getSerializableExtra("tempCardName").toString();
        tempCardBD = getIntent().getSerializableExtra("tempCardBD").toString();


        barChart = findViewById(R.id.barChartEpi);
        barChart.setNoDataText("Отсутствуют данные");
        barChart.setNoDataTextColor(R.color.purple_light);


        getEpisodes();
        getEpisodesForChart();


    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    private String getYesterDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH)-1;

        return makeDateString(day, month, year);
    }

    private void initDatePickerBegin() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                edDateBegin.setText(date);
            }
        } ;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialogBegin = new DatePickerDialog(HistoryEpisodeActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT,dateSetListener, year, month, day);

    }

    private void initDatePickerEnd() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                edDateEnd.setText(date);
            }
        } ;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialogEnd = new DatePickerDialog(HistoryEpisodeActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT,dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        String mDate;

            if ((month<10) && (day<10)) {
                mDate = year + "-0" + month + "-0" + day;
            } else if ((month<10) && (day>=10)) {
                mDate = year + "-0" + month + "-" + day;
            } else if ((month>=10) && (day<10)) {
                mDate = year + "-" + month + "-0" + day;
            } else {
                mDate = year + "-" + month + "-" + day;
            }

        return mDate;
    }

    private void getEpisodes() {
        progressBarEpi.setVisibility(View.VISIBLE);
        HTTPSBase Global = new HTTPSBase();
        String url = Global.URL_GET_HISTORY + "?id_card=" + tempCardId + "&datebeg=" + edDateBegin.getText().toString() + "&dateend=" + edDateEnd.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                episode_list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("episodes");

                    countEpisodes.setText(String.valueOf(jsonArray.length()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String datetime = object.getString("date");
                        String comment = object.getString("comment");
                        String id_card = object.getString("id_card");
                        String id_episode = object.getString("id_episode");

                        episodes = new Episodes(datetime, comment, id_card, id_episode);
                        episode_list.add(episodes);
                    }
                    adapter.notifyDataSetChanged();
                    progressBarEpi.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    adapter.notifyDataSetChanged();
                    progressBarEpi.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                    Toast.makeText(HistoryEpisodeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                adapter.notifyDataSetChanged();
                Toast.makeText(HistoryEpisodeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(HistoryEpisodeActivity.this);
        requestQueue.add(request);
    }

    private void getEpisodesForChart() {
        progressBarEpi.setVisibility(View.VISIBLE);
        HTTPSBase Global = new HTTPSBase();
        String url = Global.URL_GET_HISTORY_CHART + "?id_card=" + tempCardId + "&datebeg=" + edDateBegin.getText().toString() + "&dateend=" + edDateEnd.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // episode_list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("episodes_chart");

                    ArrayList<BarEntry> barEntries = new ArrayList<>();
                    ArrayList<String> xAxisName = new ArrayList<>();

                    String[] values = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String date_val = object.getString("date");
                        String count_val = object.getString("count");
                        BarEntry barEntry = new BarEntry(i, Float.parseFloat(count_val));
                        values[i] = date_val;
                        xAxisName.add(date_val);
                        barEntries.add(barEntry);
                    }

                    //----


                    // TO ADD THE VALUES IN X-AXIS


                    barchart(barChart,barEntries,xAxisName);
                    progressBarEpi.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    progressBarEpi.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                    Toast.makeText(HistoryEpisodeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HistoryEpisodeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(HistoryEpisodeActivity.this);
        requestQueue.add(request);
    }

    public static void barchart(BarChart barChart, ArrayList<BarEntry> arrayList, final ArrayList<String> xAxisValues) {
        barChart.setDrawBarShadow(false);
        barChart.setFitBars(true);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(25);
        barChart.setPinchZoom(true);

        barChart.setDrawGridBackground(true);

        BarDataSet barDataSet = new BarDataSet(arrayList, "Приступы");
        //barDataSet.setColors(new int[] {R.color.purple_light, R.color.purple_hard});
        barDataSet.setColor(R.color.fiol);
        // barChart.setData(new BarData(barDataSet));
        barChart.animateY(3000);
        barChart.getDescription().setText("");

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);


        barChart.getAxisRight().setDrawLabels(false);
       // BarDataSet barDataSet = new BarDataSet(arrayList, "Values");



        //barDataSet.setColors(new int[] {R.color.purple_light, R.color.purple_hard}, barChart.getContext());

        barChart.setData(new BarData(barDataSet));
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        barData.setValueTextSize(0f);



        //barChart.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
        barChart.setDrawGridBackground(true);

        Legend l = barChart.getLegend(); // Customize the ledgends
        l.setTextSize(10f);
        l.setFormSize(10f);
        //To set components of x axis
        XAxis xAxis = barChart.getXAxis();
        YAxis yAxis = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        xAxis.mEntryCount = arrayList.size();
        xAxis.setLabelCount(arrayList.size());

        xAxis.setTextSize(8f);

       // yAxis.setAxisMinimum(1.0F);
        yAxis.setGranularityEnabled(true);
        yAxis.setGranularity(1.0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        //xAxis.setDrawGridLines(false);
        yAxis.setDrawGridLines(true);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLinesBehindData(false);
        yAxis.setDrawZeroLine(true);
        yAxis.setDrawLimitLinesBehindData(false);

        yAxis2.setDrawGridLines(false);
        yAxis2.setDrawGridLines(false);

        //barDataSet.setColor(R.color.purple_hard);
        barDataSet.setColors(new int[] {R.color.fiol_bleed, R.color.fiol}, barChart.getContext());
        //if Configuration.UI_MODE_NIGHT_YES



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

    public void createLogPDF(String card_id, String type_log){

        progressBarEpi.setVisibility(View.VISIBLE);
        mRequestQueue = Volley.newRequestQueue(HistoryEpisodeActivity.this);

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