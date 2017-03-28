package com.av.dashboardthegroup.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.av.dashboardthegroup.Model.Portfolios;
import com.av.dashboardthegroup.Model.StockChartData;
import com.av.dashboardthegroup.R;
import com.av.dashboardthegroup.URL;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.av.dashboardthegroup.MainActivity.listview_stockchart;

/**
 * Created by Maiada on 3/28/2017.
 */

public class StockChartAdapter  extends BaseAdapter{


    private static LayoutInflater inflater = null;
    private Activity activity;
    StockChartAdapter.MyViewHoldwer holder = null;
    List<String> getcompany_symbols;
    List<String> getcompany_symbolsdata;
    List<Entry> yVals1;
    public static ArrayList<StockChartData> chartresult;


    public StockChartAdapter(Activity a, List<String> data ){
        getcompany_symbols=data;
        activity=a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return getcompany_symbols.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)

        {
            convertView = inflater.inflate(R.layout.row_prefferred_chart, null);
            holder = new StockChartAdapter.MyViewHoldwer(convertView);
            convertView.setTag(holder);
            Log.d("row", "Creating row");

        } else {
            holder = (StockChartAdapter.MyViewHoldwer) convertView.getTag();
            Log.d("row", "Recycling use");
        }
/*
        listview_stockchart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {*/

        String URL_Chart = URL.URL_StockChartData + "?type=2&symbol=" + getcompany_symbols.get(position);
               // Toast.makeText(activity,getcompany_symbols.get(position),Toast.LENGTH_SHORT).show();
                AndroidNetworking.get(URL_Chart)
                        .setPriority(Priority.HIGH)
                        .addHeaders("Accept", "application/json")
                        .addHeaders("Content-type", "application/json")
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    JSONArray jsonArray = response;
                                     ArrayList<StockChartData> addchartvalue=new ArrayList<>();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        StockChartData stockChartData = new StockChartData();
                                        stockChartData.setDate(object.getString("Date"));
                                        stockChartData.setPrice(object.getString("Price"));
                                        addchartvalue.add(stockChartData);
                                    }


                               // sendData(addchartvalue);
                                    LineDataSet dataSet = new LineDataSet(sendData(addchartvalue), "");
                                    //    Toast.makeText(activity,yVals1.toString(),Toast.LENGTH_SHORT).show();
                                    dataSet.setDrawCircles(false);
                                    dataSet.setDrawValues(false);
                                    dataSet.setLineWidth(2f);
                                    dataSet.setColor(activity.getResources().getColor(R.color.chart_color));
                                    // instantiate pie data object now
                                    LineData data = new LineData(dataSet);
                                    holder.mChart.setData(data);
                                    holder.mChart.animateX(2500);
                                    holder.mChart.setDrawBorders(false);
                                    holder.mChart.setDrawGridBackground(false);
                                    holder.mChart.getDescription().setEnabled(false);
                                    holder.mChart.setAutoScaleMinMaxEnabled(true);

                                    // remove axis
                                    YAxis leftAxis = holder.mChart.getAxisLeft();
                                    leftAxis.setEnabled(true);

                                    YAxis rightAxis = holder.mChart.getAxisRight();
                                    rightAxis.setEnabled(false);

                                    XAxis xAxis = holder.mChart.getXAxis();
                                    xAxis.setEnabled(true);


                                    // Shiow legend
                                    Legend l = holder.mChart.getLegend();
                                    // modify the legend ...
                                    l.setForm(Legend.LegendForm.LINE);
                                    l.setTextSize(12f);
                                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                                    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                                    l.setDrawInside(false);



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });




        return convertView;
    }
    static  class MyViewHoldwer{
        LineChart mChart;
        public  MyViewHoldwer(View v){
            mChart =  (LineChart) v.findViewById(R.id.line_chart);

            v.setTag(this);


        }


    }

public List<Entry> sendData(ArrayList<StockChartData> chartresult){
    yVals1 = new ArrayList<Entry>();
    for (int i = 0; i < chartresult.size(); i++)
    {
        StockChartData h = chartresult.get(i);
        // Toast.makeText(activity,h.toString(),Toast.LENGTH_SHORT).show();
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





    }

    return yVals1 ;
}}


