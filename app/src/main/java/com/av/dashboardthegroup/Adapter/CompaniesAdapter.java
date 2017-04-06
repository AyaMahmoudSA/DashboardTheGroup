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

import com.av.dashboardthegroup.Model.CompanyData;
import com.av.dashboardthegroup.PieChartActivity;
import com.av.dashboardthegroup.R;
import com.github.mikephil.charting.charts.PieChart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.av.dashboardthegroup.PieChartActivity.list_companies;

/**
 * Created by Maiada on 4/6/2017.
 */
public class CompaniesAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    private Activity activity;
    MyViewHoldwer holder = null;
    ArrayList<CompanyData> get_companyDataArrayList;



    public CompaniesAdapter(Activity a, ArrayList<CompanyData> companyDataArrayList) {
        get_companyDataArrayList=companyDataArrayList;
        activity=a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return get_companyDataArrayList.size();
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
            convertView = inflater.inflate(R.layout.row_list_company, null);
            holder = new MyViewHoldwer(convertView);
            convertView.setTag(holder);
            Log.d("row", "Creating row");

        } else {
            holder = (MyViewHoldwer) convertView.getTag();
            Log.d("row", "Recycling use");
        }


        CompanyData cd=get_companyDataArrayList.get(position);
        holder.CompanyName.setText(cd.getNameEn());



        String url="http://www.thegroup.com.qa/iphone/Logos/"+cd.getSymbol()+".png";
        Picasso.with(activity).load(url).into(holder.CompanySymbol);

     /*   list_companies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/








        return convertView;

    }
    static  class MyViewHoldwer{
        TextView CompanyName;
        ImageView CompanySymbol;
        ImageView set_check;
        public  MyViewHoldwer(View v){
            CompanyName    = (TextView)  v.findViewById(R.id.txt_CompanyName);
            CompanySymbol =  (ImageView) v.findViewById(R.id.img_CompanySymbol);
            set_check=(ImageView) v.findViewById(R.id.check);
            v.setTag(this);


        }
    }
}
