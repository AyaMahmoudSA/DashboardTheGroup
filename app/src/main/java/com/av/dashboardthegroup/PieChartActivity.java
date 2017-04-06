package com.av.dashboardthegroup;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.av.dashboardthegroup.Adapter.CompaniesAdapter;
import com.av.dashboardthegroup.Model.CompanyData;
import com.av.dashboardthegroup.Model.MarketChartData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.av.dashboardthegroup.MainActivity.company_symbols;

/**
 * Created by Aya on 3/15/2017.
 */

public class PieChartActivity extends Activity {
    LineChart mChart;
    private float[]  yData = { 20, 50, 15 };

    private float[] xData = { 50, 50, 50};
    static ArrayList<MarketChartData> addchartvalue;
    ArrayList<CompanyData> companyDataArrayList;
    CompaniesAdapter companiesAdapter;
    public static   ListView list_companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_compnay);

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        list_companies=(ListView) findViewById(R.id.list_company);

        AndroidNetworking.get("http://thegroupmw.azurewebsites.net/JSON/Companies.aspx")
                .setPriority(Priority.HIGH)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-type", "application/json")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            companyDataArrayList=new ArrayList<CompanyData>();

                            JSONArray get_categories=response.getJSONArray("categories");
                            for(int i =0;i<get_categories.length();i++){
                                JSONObject get_category=get_categories.getJSONObject(i);
                                JSONArray get_companies=get_category.getJSONArray("companies");
                                for(int j=0;j<get_companies.length();j++){
                                    JSONObject get_company=get_companies.getJSONObject(j);
                                    CompanyData cd=new CompanyData();
                                    cd.setNameEn(get_company.getString("NameEn"));
                                    cd.setNameAr(get_company.getString("NameAr"));
                                    cd.setSymbol(get_company.getString("Symbol"));
                                    cd.setLogo(get_company.getString("Logo"));
                                    companyDataArrayList.add(cd);

                                }

                            }
                           companiesAdapter=new CompaniesAdapter(PieChartActivity.this,companyDataArrayList);
                            list_companies.setAdapter(companiesAdapter);
                            list_companies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {












                                }
                            });




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

/*

        AndroidNetworking.get("https://testfirebase-d33a0.firebaseio.com/chart_Data/.json")
                .setPriority(Priority.HIGH)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-type", "application/json")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject jsonArray=response;
                            for(int k=0;k<jsonArray.length();k++){
                                String check=company_symbols.get(k);
                                String check2= jsonArray.names().getString(k);

                                //    for(int m=0;m<jsonArray.length();m++){
                                    if(check.trim()==check2.trim()){
                                        JSONArray n=jsonArray.getJSONArray(company_symbols.get(k));

                                        addchartvalue =new ArrayList<>();
                                        for(int j=0 ;j<n.length();j++){
                                            JSONObject object = n.getJSONObject(j);
                                            MarketChartData marketChartData=new MarketChartData();
                                            marketChartData.setDate(object.getString("Date"));
                                            marketChartData.setPrice(object.getString("Price"));
                                            addchartvalue.add(marketChartData);
                                        }


                                        mChart = (LineChart) findViewById(R.id.line_chart);

                                        List<Entry> yVals1 = new ArrayList<Entry>();
                                        for (int i = 0; i < addchartvalue.size(); i++) {
                                            MarketChartData h = addchartvalue.get(i);
                                            float P[] = {Float.parseFloat(h.getPrice())};

                                            String output = h.getDate().substring(10, 16);
                                            String output_hours = output.substring(1, 3);//09
                                            String output_seconds = output.substring(4, 6);//09
                                            String trim_hours;
                                            if (output_hours.startsWith("0")) {
                                                trim_hours = output_hours.substring(1, 2);
                                            } else {
                                                trim_hours = output_hours;
                                            }
                                            String Time = trim_hours + "." + output_seconds;
                                            float fTime = Float.parseFloat(Time);

                                            float D[] = {fTime};
                                            for (int j = 0; j < P.length; j++) {

                                                yVals1.add(new Entry(D[j], P[j]));

                                            }
                                            LineDataSet dataSet = new LineDataSet(yVals1, "MarketData Charts");

                                            //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                                            dataSet.setDrawCircles(false);
                                            dataSet.setDrawValues(false);
                                            dataSet.setLineWidth(2f);
                                            dataSet.setColor(getResources().getColor(R.color.colorPrimaryDark));
                                            // instantiate pie data object now
                                            LineData data = new LineData(dataSet);

                                            //    data.setValueFormatter(new PercentFormatter());

                                            // undo all highlights
                                            mChart.setData(data);
                                            mChart.animateX(2500);
                                            mChart.setDrawBorders(false);
                                            mChart.setDrawGridBackground(false);
                                            mChart.getDescription().setEnabled(false);
                                            mChart.setAutoScaleMinMaxEnabled(true);

                                            // remove axis
                                            YAxis leftAxis = mChart.getAxisLeft();
                                            leftAxis.setEnabled(true);

                                            YAxis rightAxis = mChart.getAxisRight();
                                            rightAxis.setEnabled(false);

                                            XAxis xAxis = mChart.getXAxis();
                                            xAxis.setEnabled(true);


                                            // Shiow legend
                                            Legend l = mChart.getLegend();
                                            // modify the legend ...
                                            l.setForm(Legend.LegendForm.LINE);
                                            l.setTextSize(11f);
                                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                                            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                                            l.setDrawInside(false);

                                        }


                                }

                                }


                        //    }


                            // create pie data set

                            // update pie chart
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
*/


       // mChart.setUsePercentValues(true);
      //  mChart.getDescription().setEnabled(false);


       // mChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        //   mChart.setCenterText(generateCenterSpannableText());

      //  mChart.setDrawHoleEnabled(true);
     //   mChart.setHoleColor(Color.WHITE);

     //   mChart.setTransparentCircleColor(Color.WHITE);
      //  mChart.setTransparentCircleAlpha(110);

      //  mChart.setHoleRadius(58f);
      //  mChart.setTransparentCircleRadius(61f);

     //   mChart.setDrawCenterText(true);

      //  mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
      //  mChart.setRotationEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener


        // mChart.spin(2000, 0, 360);




        // entry label styling


    }


    private void addData() {

    }

/*
        LineChart lineChart = (LineChart) findViewById(R.id.line_chart);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");



        LineData data = new LineData(dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        //dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        lineChart.setData(data);
        lineChart.animateY(5000);*/



       /* mChart = (PieChart) findViewById(R.id.pie_chart);

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
     //   mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener

        addData();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTextSize(9f);
        mChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        mChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);

    }


    private void addData() {
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new PieEntry( yData[i],xData[i]));


        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "Balance");

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        Random rnd = new Random();
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);


        // instantiate pie data object now
        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextColor(Color.BLACK);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);
        // undo all highlights
        mChart.highlightValues(null);
        mChart.setData(data);
        mChart.animateX(1000);
        // update pie chart
        mChart.invalidate();
    }*/




}