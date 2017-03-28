package com.av.dashboardthegroup.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.av.dashboardthegroup.JSONParser;
import com.av.dashboardthegroup.MainActivity;
import com.av.dashboardthegroup.Model.MarketChartData;
import com.av.dashboardthegroup.Model.StockChartData;
import com.av.dashboardthegroup.R;
import com.av.dashboardthegroup.URL;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.av.dashboardthegroup.MainActivity.dataSnapshot_StocksData;
import static com.av.dashboardthegroup.MainActivity.gridView;

/**
 * Created by Aya on 3/14/2017.
 */

public class PerferredStockDataAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    static  ArrayList<StockChartData> addchartvalue;
    DataSnapshot dataSnap;
    private Activity activity;
    MyViewHoldwer holder = null;
    List<Entry> yVals1;
    public PerferredStockDataAdapter(Activity a,DataSnapshot get_data) {
        dataSnap=get_data;
        activity=a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }
    @Override
    public int getCount() {
        return (int)dataSnap.getChildrenCount()-36;
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
            convertView = inflater.inflate(R.layout.row_preferred_data_ar, null);
            holder = new MyViewHoldwer(convertView);
            convertView.setTag(holder);
            Log.d("row", "Creating row");

        } else {
            holder = (MyViewHoldwer) convertView.getTag();
            Log.d("row", "Recycling use");
        }

        DataSnapshot  child = dataSnap.child(String.valueOf(position));

        holder.Symbol.setText(child.child("NameAr").getValue().toString());

        String get_CurrentPrice = BigDecimal(child.child("CurrentPrice").getValue().toString());
        holder.CurrentPrice.setText(get_CurrentPrice);

        String get_ChangePercentage = BigDecimal(child.child("ChangePercentage").getValue().toString());
        holder.ChangePercentage.setText(get_ChangePercentage+"%");

        String get_ChangeValue = BigDecimal(child.child("ChangeValue").getValue().toString());
        holder.ChangeValue.setText(get_ChangeValue);

        if(child.child("ChangeSign").getValue().toString().equals("+")){

         holder.linearLayout.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shadow_green));

        }
        if(child.child("ChangeSign").getValue().toString().equals("-")){
            holder.linearLayout.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shadow_red));



        }
        if(child.child("ChangeSign").getValue().toString().equals("=")) {
            holder.linearLayout.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shadow_yellow));

        }

     /*   String []symbols={child.child("Symbol").getValue().toString()};

        for(int k=0;k<symbols.length;k++){
          //  Toast.makeText(activity,symbols[k],Toast.LENGTH_SHORT).show();

            String URL_Chart= URL.URL_StockChartData+"?type=2&symbol="+symbols[k];
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
                                addchartvalue = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    StockChartData stockChartData = new StockChartData();
                                    stockChartData.setDate(object.getString("Date"));
                                    stockChartData.setPrice(object.getString("Price"));
                                    addchartvalue.add(stockChartData);
                                }



                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

            yVals1 = new ArrayList<Entry>();
            for (int i = 0; i < addchartvalue.size(); i++) {
                StockChartData h = addchartvalue.get(i);
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

                LineDataSet dataSet = new LineDataSet(yVals1, "");
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
            }

            }
*/

/*
    try {
        JSONParser jParser = new JSONParser();
        String URL_Chart= URL.URL_StockChartData+"?type=2&symbol="+child.child("Symbol").getValue();
        JSONArray json = jParser.getJSONArrFromUrl(URL_Chart);
        addchartvalue=new ArrayList<>();
         for (int i = 0; i < json.length(); i++) {
                                JSONObject object = json.getJSONObject(i);
                                StockChartData stockChartData = new StockChartData();
                                stockChartData.setDate(object.getString("Date"));
                                stockChartData.setPrice(object.getString("Price"));
                                addchartvalue.add(stockChartData);
                            }

                            yVals1 = new ArrayList<Entry>();
                            for (int i = 0; i < addchartvalue.size(); i++) {
                                StockChartData h = addchartvalue.get(i);
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

                                LineDataSet dataSet = new LineDataSet(yVals1,"");
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


                            }



                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }





*/



        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        //    data.setValueFormatter(new PercentFormatter());

        // undo all highlights



        /* gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    dataSnap = dataSnapshot_StocksData.child(String.valueOf(position));



                  *//*  final CharSequence[] items = {"BUY", "SELL", "Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Make your selection");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Do something with the selection
                            if (items[item].equals("BUY")) {
                               //getCapturesProfilePicFromCamera();
                                Intent i=new Intent(MainActivity.this,orderactivity.class);
                                i.putExtra("Symbol",dataSnap.child("Symbol").getValue().toString());

                                startActivity(i);



                            } else if (items[item].equals("Choose from Library")) {
                                Toast.makeText(MainActivity.this,items[item], Toast.LENGTH_SHORT).show();
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

*//*



                }
            });*/

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataSnapshot  child = dataSnap.child(String.valueOf(position));




                }

        });


        return convertView;
    }


    static  class MyViewHoldwer {
        TextView Symbol, CurrentPrice, ChangePercentage, ChangeValue;
        LinearLayout linearLayout;
        LineChart mChart;


        public MyViewHoldwer(View v) {
            Symbol = (TextView) v.findViewById(R.id.txt_Symbol);
            CurrentPrice = (TextView) v.findViewById(R.id.txt_CurrentPrice);
            ChangePercentage = (TextView) v.findViewById(R.id.txt_ChangePercentage);
            ChangeValue = (TextView) v.findViewById(R.id.txt_ChangeValue);
            linearLayout =(LinearLayout)v.findViewById(R.id.linear_color);
            mChart=(LineChart) v.findViewById(R.id.line_chart);
            v.setTag(this);


        }
    }

    public  String BigDecimal(String NumberFormatString){
        BigDecimal bigDecimal_value = new BigDecimal(NumberFormatString);
        BigDecimal string_value = bigDecimal_value.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        String result = string_value.toString();

        return  result ;
    }

    public  void DrawChart(final String Symbol){




    }
}



