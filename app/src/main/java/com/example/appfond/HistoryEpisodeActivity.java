package com.example.appfond;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class HistoryEpisodeActivity extends AppCompatActivity {

    private Toolbar toolbarHistory;
    private RecyclerView episode_list_view;
    private List<Episodes> episode_list;
    Episodes episodes;
    EpisodesAdapter adapter;
    private ProgressBar progressBarEpi;
    private Button btnUpdateData;
    private TextView countEpisodes;
    private EditText edDateBegin;
    private EditText edDateEnd;
    private DatePickerDialog datePickerDialogBegin;
    private DatePickerDialog datePickerDialogEnd;
    String tempCardId;
    BarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_episode);

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
                getEpisodesForChart();
                getEpisodes();
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

        barChart = findViewById(R.id.barChartEpi);

        getEpisodesForChart();
        getEpisodes();
        //barChart





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
                    //String success = "0";
                    //success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("episodes");
                    //Toast.makeText(MainActivity.this, success + "" + jsonArray.length(), Toast.LENGTH_LONG).show();
                    //if (success.equals("1")) {
                    countEpisodes.setText(String.valueOf(jsonArray.length()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String datetime = object.getString("date");
                        String comment = object.getString("comment");
                        String id_card = object.getString("id_card");
                        String id_episode = object.getString("id_episode");

                        episodes = new Episodes(datetime, comment, id_card, id_episode);
                        episode_list.add(episodes);
                        adapter.notifyDataSetChanged();
                        progressBarEpi.setVisibility(View.INVISIBLE);
                    }
                    //}

                } catch (Exception e) {
                    progressBarEpi.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                    Toast.makeText(HistoryEpisodeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        Toast.makeText(HomeFragment.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
                episode_list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("episodes_chart");

                    ArrayList<BarEntry> barEntries = new ArrayList<>();
                    ArrayList<String> xAxisName = new ArrayList<>();
                    Legend legend = barChart.getLegend();
                    List<LegendEntry> entries = new ArrayList<>();
                    String[] values = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String date_val = object.getString("date");
                        String count_val = object.getString("count");
                        BarEntry barEntry = new BarEntry(i, Float.parseFloat(count_val));
                        values[i] = date_val;
                        xAxisName.add(date_val);
                        //xAxis1.setTypeface(tf);
                        //YAxis leftAxis = barChart.getAxisLeft();
                        //leftAxis.setEnabled(false);

                        LegendEntry entry = new LegendEntry();
                        /*entry.formColor = colorList.get(i);
                        entry.label = titleList.get(i);*/
                        entries.add(entry);

                        barEntries.add(barEntry);
                        progressBarEpi.setVisibility(View.INVISIBLE);
                    }

                    BarDataSet barDataSet = new BarDataSet(barEntries, "Приступы");
                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    barChart.setData(new BarData(barDataSet));
                    barChart.animateY(3000);
                    barChart.getDescription().setText("");
                    legend.setEnabled(false);

                   /* XAxis xAxis1 = barChart.getXAxis();
                    xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis1.setTextSize(8);
                    //xAxis1.setGranularity(1f);
                    //MyAxisValueFormatter myAxisValueFormatter = new MyAxisValueFormatter(values);
                    xAxis1.setValueFormatter(new MyAxisValueFormatter(values));
                    //xAxis1.setValueFormatter(myAxisValueFormatter);
                    xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis1.setTextColor(Color.parseColor("#701112"));*/

                    barChart.getAxisRight().setDrawLabels(false);

                    // TO ADD THE VALUES IN X-AXIS


                    barchart(barChart,barEntries,xAxisName);

                    //barChart.invalidate();


                    //}

                } catch (Exception e) {
                    progressBarEpi.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                    Toast.makeText(HistoryEpisodeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        Toast.makeText(HomeFragment.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
        BarDataSet barDataSet = new BarDataSet(arrayList, "Values");

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        barData.setValueTextSize(0f);

        barChart.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
        barChart.setDrawGridBackground(false);

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

        barChart.setData(barData);

    }

}