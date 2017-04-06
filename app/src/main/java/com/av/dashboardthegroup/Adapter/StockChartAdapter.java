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
import com.av.dashboardthegroup.MainActivity;
import com.av.dashboardthegroup.Model.MarketChartData;
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
import com.google.firebase.database.DataSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
    List<Entry> yVals1=new ArrayList<>();
    public static ArrayList<StockChartData> chartresult;
    HashMap<String, ArrayList<StockChartData>> dataSnap;
    HashMap<String, ArrayList<StockChartData>> dataSnap2;
   // ArrayList<ArrayList<StockChartData>> addchartvalue_stock2;
    ArrayList<StockChartData> addchartvalue_stock3;
    List<Entry> yVals2;
    DataSnapshot dataSnapshot;
    ArrayList<ArrayList<StockChartData>> addchartvalue_stock2;
    ArrayList<StockChartData> addchartvalue_stock;


    public StockChartAdapter(Activity a,DataSnapshot data ){
        dataSnapshot=data;
        activity=a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return (int)dataSnapshot.getChildrenCount()-42;
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

        addchartvalue_stock=new ArrayList<>();
        addchartvalue_stock2=new ArrayList<>();
  //      for(int j=0;j<dataSnapshot.getChildren;j++)
        for (DataSnapshot child : dataSnapshot.getChildren()) {

          //  Symbols =child.getKey();
for(int i=0;i<child.getChildrenCount();i++){}
            for (DataSnapshot chil : child.getChildren()) {

                StockChartData m=new StockChartData();
                m.setDate(chil.child("Date").getValue().toString());
                m.setPrice(chil.child("Price").getValue().toString());
                addchartvalue_stock.add(m);

                        /*StockChartData m=child.child(String.valueOf(i));
                        m.setDate(child.child("Date").getValue().toString());
                        m.setPrice(child.child("Price").getValue().toString());
*/
            }
            addchartvalue_stock2.add(addchartvalue_stock);

            //  listHashMap.put(Symbols,addchartvalue_stock);
            // company_symbols.add(Symbols);

        }
        //yVals2=SendData(addchartvalue_stock2.get(position));


          yVals1=new ArrayList<>();
      for (int i = 0; i < addchartvalue_stock2.get(position).size(); i++) {
            StockChartData h = addchartvalue_stock2.get(position).get(i);
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

            LineDataSet dataSet = new LineDataSet(yVals1, "الرسوم البيانية للسوق");
            //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setDrawCircles(false);
            dataSet.setDrawValues(false);
            dataSet.setLineWidth(2f);
            dataSet.setColor(activity.getResources().getColor(R.color.chart_color));
            // instantiate pie data object now
            LineData data = new LineData(dataSet);

            //    data.setValueFormatter(new PercentFormatter());

            // undo all highlights
            holder.mChart.setData(data);
            holder.mChart.animateX(2500);
            holder. mChart.setDrawBorders(false);
            holder. mChart.setDrawGridBackground(false);
            holder.  mChart.getDescription().setEnabled(false);
            holder.  mChart.setAutoScaleMinMaxEnabled(true);

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





        return convertView;
    }


    static  class MyViewHoldwer{
        LineChart mChart;
        public  MyViewHoldwer(View v){
            mChart =  (LineChart) v.findViewById(R.id.line_chart);

            v.setTag(this);


        }


    }


  /*  public List<Entry> SendData(ArrayList<StockChartData> chart_data){
        for (int i = 0; i < chart_data.size(); i++) {
            StockChartData h = chart_data.get(i);
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
        return  yVals1;
    }*/
    }









