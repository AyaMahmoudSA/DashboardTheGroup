package com.av.dashboardthegroup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Maiada on 02-03-2017.
 */

public class MainActivity extends AppCompatActivity {
    TextView CurrentMarketIndex,ChangePercentage,ChangeValue,
             MarketTrasactionValue,TransactionVolume,NoOfMarketTrades ;
    private ExpandGridView gridView;
    public static DataSnapshot dataSnapshot_StocksData;
    public PerferredStockDataAdapter  perferredStockDataAdapter ;
    MyViewHoldwer holder = null;
    private static LayoutInflater inflater = null;
    DataSnapshot dataSnap;
    private Activity activity;
    ScrollView scrollView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO make logo behind name of activity "Dashboard"
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled (true);
        actionbar.setHomeAsUpIndicator (R.drawable.actionbar);
        /*scrollView =(ScrollView)findViewById(R.id.scroll);
        scrollView.requestFocus();
*/
        //TODO  Declare textview from view to access it .
        //TODO First box about market data
        CurrentMarketIndex    = (TextView) findViewById(R.id.txt_CurrentMarketIndex);
        ChangePercentage      = (TextView) findViewById(R.id.txt_ChangePercentage);
        ChangeValue           = (TextView) findViewById(R.id.txt_ChangeValue);

        //TODO Second box about market data
        MarketTrasactionValue = (TextView) findViewById(R.id.txt_MarketTrasactionValue);
        TransactionVolume     = (TextView) findViewById(R.id.txt_TransactionVolume);
        NoOfMarketTrades      = (TextView) findViewById(R.id.txt_NoOfMarketTrades);


        //TODO get instance from Firebase to fetch data
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef_1 = database.getReference();
        DatabaseReference myRef_2 = database.getReference();

        myRef_1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

             //TODO Get market data first box value from Firebase
             DataSnapshot   get_CurrentMarketIndex = dataSnapshot.child("Market_Data").child("CurrentMarketIndex");
             DataSnapshot   get_ChangePercentage   = dataSnapshot.child("Market_Data").child("ChangePercentage");
             DataSnapshot   get_ChangeValue        = dataSnapshot.child("Market_Data").child("ChangeValue");
             DataSnapshot   get_Sign               = dataSnapshot.child("Market_Data").child("Sign");




             String string_CurrentMarketIndex = FormatNumber(get_CurrentMarketIndex.getValue().toString()) ;
             CurrentMarketIndex.setText(string_CurrentMarketIndex);
             ChangePercentage.setText(get_ChangePercentage.getValue().toString()+"%");
             ChangeValue.setText(get_ChangeValue.getValue().toString());

             if(get_Sign.getValue().toString().equals("+")){
                 ChangePercentage.setTextColor(getResources().getColor(R.color.plus_sign));
                 ChangeValue.setTextColor(getResources().getColor(R.color.plus_sign));

             }
             if(get_Sign.getValue().toString().equals("-")){
                    ChangePercentage.setTextColor(getResources().getColor(R.color.minus_sign));
                    ChangeValue.setTextColor(getResources().getColor(R.color.minus_sign));

              }
             if(get_Sign.getValue().toString().equals("=")){
                    ChangePercentage.setTextColor(getResources().getColor(R.color.equal_sign));
                    ChangeValue.setTextColor(getResources().getColor(R.color.minus_sign));

              }

             //TODO Get market data Second box value from Firebase
             DataSnapshot   get_MarketTrasactionValue = dataSnapshot.child("Market_Data").child("MarketTrasactionValue");
             DataSnapshot   get_TransactionVolume     = dataSnapshot.child("Market_Data").child("TransactionVolume");
             DataSnapshot   get_NoOfMarketTrades      = dataSnapshot.child("Market_Data").child("NoOfMarketTrades");


             String string_MarketTrasactionValue = FormatNumber(get_MarketTrasactionValue.getValue().toString()) ;
             MarketTrasactionValue.setText(string_MarketTrasactionValue);


             String string_TransactionVolume = FormatNumber(get_TransactionVolume.getValue().toString()) ;
             TransactionVolume.setText(string_TransactionVolume);
             String string_NoOfMarketTrades = FormatNumber(get_NoOfMarketTrades.getValue().toString()) ;
             NoOfMarketTrades.setText(string_NoOfMarketTrades);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gridView=new ExpandGridView(this);
        gridView = (ExpandGridView) findViewById(R.id.grid);
        gridView.setExpanded(true);
        gridView.setFocusable(false); // To make Gridview not start at top of screen you have to set

        perferredStockDataAdapter = new PerferredStockDataAdapter(this);



        myRef_2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot  get_StocksData = dataSnapshot.child("Stocks_Data");
                dataSnapshot_StocksData=get_StocksData;


                gridView.setAdapter(perferredStockDataAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }






    //TODO Method to check if string double or int and after that do format for number
    public  String FormatNumber(String NumberFormatString){
        Double double_Value = Double.parseDouble(NumberFormatString);
       if (double_Value == Math.floor(double_Value)){
           int int_Value         = double_Value.intValue();
           NumberFormat numberFormat   = NumberFormat.getNumberInstance(Locale.US);
           String string_Value = numberFormat.format(int_Value);
           return  string_Value;
       }else{
           NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
           String string_Value  = numberFormat.format(double_Value);
           return  string_Value;
       }
    }




    public  class PerferredStockDataAdapter extends BaseAdapter{

        public PerferredStockDataAdapter(Activity a) {
            //      get_allstockdataadapter=get_allstockdata;
            activity=a;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        }
        @Override
        public int getCount() {
            return (int) dataSnapshot_StocksData.getChildrenCount();
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
                convertView = inflater.inflate(R.layout.row_preferred_data, null);
                holder = new MyViewHoldwer(convertView);
                convertView.setTag(holder);
                Log.d("row", "Creating row");

            } else {
                holder = (MyViewHoldwer) convertView.getTag();
                Log.d("row", "Recycling use");
            }

            dataSnap = dataSnapshot_StocksData.child(String.valueOf(position));

            holder.Symbol.setText(dataSnap.child("Symbol").getValue().toString());

            String get_CurrentPrice = BigDecimal(dataSnap.child("CurrentPrice").getValue().toString());
            holder.CurrentPrice.setText(get_CurrentPrice);

            String get_ChangePercentage = BigDecimal(dataSnap.child("ChangePercentage").getValue().toString());
            holder.ChangePercentage.setText(get_ChangePercentage);

            String get_ChangeValue = BigDecimal(dataSnap.child("ChangeValue").getValue().toString());
            holder.ChangeValue.setText(get_ChangeValue);



            if(dataSnap.child("ChangeSign").getValue().toString().equals("+")){
                holder.ChangePercentage.setTextColor(getResources().getColor(R.color.plus_sign));
                holder.ChangeValue.setTextColor(getResources().getColor(R.color.plus_sign));

            }
            if(dataSnap.child("ChangeSign").getValue().toString().equals("-")){
                holder.ChangePercentage.setTextColor(getResources().getColor(R.color.minus_sign));
                holder.ChangeValue.setTextColor(getResources().getColor(R.color.minus_sign));

            }
            if(dataSnap.child("ChangeSign").getValue().toString().equals("=")){
                holder.ChangePercentage.setTextColor(getResources().getColor(R.color.equal_sign));
                holder.ChangeValue.setTextColor(getResources().getColor(R.color.minus_sign));

            }

            return convertView;
        }



    }

    static  class MyViewHoldwer{
        TextView Symbol,CurrentPrice,ChangePercentage,ChangeValue;
        public  MyViewHoldwer(View v){
            Symbol = (TextView) v.findViewById(R.id.txt_Symbol);
            CurrentPrice   = (TextView) v.findViewById(R.id.txt_CurrentPrice);
            ChangePercentage     = (TextView) v.findViewById(R.id.txt_ChangePercentage);
            ChangeValue   = (TextView) v.findViewById(R.id.txt_ChangeValue);
            v.setTag(this);


        }
    }

    public  String BigDecimal(String NumberFormatString){
        BigDecimal bigDecimal_value = new BigDecimal(NumberFormatString);
        BigDecimal string_value = bigDecimal_value.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        String result = string_value.toString();

        return  result ;
    }



}
