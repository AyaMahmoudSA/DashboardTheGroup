package com.av.dashboardthegroup.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.av.dashboardthegroup.APP;
import com.av.dashboardthegroup.MainActivity;
import com.av.dashboardthegroup.Model.Portfolios;
import com.av.dashboardthegroup.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Aya on 3/14/2017.
 */

public class PortfoliosAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    MyViewHoldwer holder = null;
    ArrayList<Portfolios> get_data;
    JSONObject get_chartdata;
    public float[]  yData ;
    private String[] xData ;

    public PortfoliosAdapter(Activity a, ArrayList<Portfolios> data ){
        get_data=data;
        activity=a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return get_data.size();
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
            convertView = inflater.inflate(R.layout.row_portfolio_data, null);
            holder = new MyViewHoldwer(convertView);
            convertView.setTag(holder);
            Log.d("row", "Creating row");

        } else {
            holder = (MyViewHoldwer) convertView.getTag();
            Log.d("row", "Recycling use");
        }
        //Toast.makeText(MainActivity.this,get_respone.size()+"",Toast.LENGTH_SHORT).show();
        Portfolios portfolioStock = new Portfolios();
        portfolioStock=get_data.get(position);

        holder.Symbol.setText(portfolioStock.getNameAr());


         String marketvalue=portfolioStock.getMarketValue();
         marketvalue=marketvalue.replaceAll(",", "");
         float MarketValue=Float.parseFloat(marketvalue);

        String noofstocks=portfolioStock.getNoOfStocks();
        float NoOfStocks=Float.parseFloat(noofstocks);

        Float CurrentValue =  MarketValue *  NoOfStocks ;
        holder. mChart.getDescription().setEnabled(false);
     //   holder. mChart.setExtraOffsets(5, 10, 5, 5);

          float CostPrice= 1000;
          float Revenue = CurrentValue - CostPrice;



        // enable rotation of the chart by touch
        holder. mChart.setRotationEnabled(true);
        holder. mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        List<PieEntry> pieEntries =new ArrayList<>();
        float chart_value []={CurrentValue,CostPrice,Revenue};
        String chart_name []={"القيمة الحالية","سعر التكلفة","الربح"};

        for(int i=0;i<chart_value.length; i++){

                pieEntries.add(new PieEntry(chart_value[i]  ,chart_name[i],"%"));

        }

        PieDataSet dataSet =new PieDataSet(pieEntries,"الرسوم البيانية للمحفظة");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data=new PieData(dataSet);

        holder.mChart.setData(data);
        holder.mChart.getDescription().setEnabled(false);


        data.setValueTextSize(14f);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        // undo all highlights
        holder. mChart.setData(data);
        holder. mChart.animateX(1000);
        // update pie chart
        holder. mChart.invalidate();

        Legend l =  holder.mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(10f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        holder. mChart.setEntryLabelColor(Color.BLACK);
        holder.mChart.setEntryLabelTextSize(9f);
        holder. mChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        holder. mChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);















        return convertView;
    }


    static  class MyViewHoldwer{
        TextView Symbol;
        PieChart mChart;

        public  MyViewHoldwer(View v){
            Symbol    = (TextView)  v.findViewById(R.id.txt_Symbol);
            mChart =  (PieChart) v.findViewById(R.id.pie_chart);

            v.setTag(this);


        }
    }

}
