package com.av.dashboardthegroup.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.av.dashboardthegroup.MainActivity;
import com.av.dashboardthegroup.Model.Portfolios;
import com.av.dashboardthegroup.R;

import java.util.ArrayList;

/**
 * Created by Aya on 3/14/2017.
 */

public class PortfoliosAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    MyViewHoldwer holder = null;
    ArrayList<Portfolios> get_data;

    public PortfoliosAdapter(Activity a,ArrayList<Portfolios> data) {
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
        holder.Symbol.setText(portfolioStock.getSymbol());
        holder.imageView.setImageResource(R.drawable.chart3);


        return convertView;
    }


    static  class MyViewHoldwer{
        TextView Symbol;
        ImageView imageView;
        public  MyViewHoldwer(View v){
            Symbol    = (TextView)  v.findViewById(R.id.txt_Symbol);
            imageView =  (ImageView) v.findViewById(R.id.image);

            v.setTag(this);


        }
    }
}
