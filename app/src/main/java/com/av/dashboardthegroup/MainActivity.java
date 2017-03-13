package com.av.dashboardthegroup;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Maiada on 02-03-2017.
 */

public class MainActivity extends AppCompatActivity {
    TextView CurrentMarketIndex, ChangePercentage, ChangeValue,
            MarketTrasactionValue, TransactionVolume, NoOfMarketTrades;
    private ExpandGridView gridView;
    public static DataSnapshot dataSnapshot_StocksData;
    public PerferredStockDataAdapter perferredStockDataAdapter;
    MyViewHoldwer holder = null;
    private static LayoutInflater inflater = null;
    DataSnapshot dataSnap;
    private Activity activity;
    ScrollView scrollView;

    JSONObject get_result;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO make logo behind name of activity "Dashboard"
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.actionbar);
        /*scrollView =(ScrollView)findViewById(R.id.scroll);
        scrollView.requestFocus();
*/
        //TODO  Declare textview from view to access it .
        //TODO First box about market data
        CurrentMarketIndex = (TextView) findViewById(R.id.txt_CurrentMarketIndex);
        ChangePercentage = (TextView) findViewById(R.id.txt_ChangePercentage);
        ChangeValue = (TextView) findViewById(R.id.txt_ChangeValue);

        //TODO Second box about market data
        MarketTrasactionValue = (TextView) findViewById(R.id.txt_MarketTrasactionValue);
        TransactionVolume = (TextView) findViewById(R.id.txt_TransactionVolume);
        NoOfMarketTrades = (TextView) findViewById(R.id.txt_NoOfMarketTrades);


        //TODO get instance from Firebase to fetch data
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef_1 = database.getReference();
        DatabaseReference myRef_2 = database.getReference();

        myRef_1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //TODO Get market data first box value from Firebase
                DataSnapshot get_CurrentMarketIndex = dataSnapshot.child("Market_Data").child("CurrentMarketIndex");
                DataSnapshot get_ChangePercentage = dataSnapshot.child("Market_Data").child("ChangePercentage");
                DataSnapshot get_ChangeValue = dataSnapshot.child("Market_Data").child("ChangeValue");
                DataSnapshot get_Sign = dataSnapshot.child("Market_Data").child("Sign");


                String string_CurrentMarketIndex = FormatNumber(get_CurrentMarketIndex.getValue().toString());
                CurrentMarketIndex.setText(string_CurrentMarketIndex);
                ChangePercentage.setText(get_ChangePercentage.getValue().toString() + "%");
                ChangeValue.setText(get_ChangeValue.getValue().toString());

                if (get_Sign.getValue().toString().equals("+")) {
                    ChangePercentage.setTextColor(getResources().getColor(R.color.plus_sign));
                    ChangeValue.setTextColor(getResources().getColor(R.color.plus_sign));

                }
                if (get_Sign.getValue().toString().equals("-")) {
                    ChangePercentage.setTextColor(getResources().getColor(R.color.minus_sign));
                    ChangeValue.setTextColor(getResources().getColor(R.color.minus_sign));

                }
                if (get_Sign.getValue().toString().equals("=")) {
                    ChangePercentage.setTextColor(getResources().getColor(R.color.equal_sign));
                    ChangeValue.setTextColor(getResources().getColor(R.color.minus_sign));

                }

                //TODO Get market data Second box value from Firebase
                DataSnapshot get_MarketTrasactionValue = dataSnapshot.child("Market_Data").child("MarketTrasactionValue");
                DataSnapshot get_TransactionVolume = dataSnapshot.child("Market_Data").child("TransactionVolume");
                DataSnapshot get_NoOfMarketTrades = dataSnapshot.child("Market_Data").child("NoOfMarketTrades");


                String string_MarketTrasactionValue = FormatNumber(get_MarketTrasactionValue.getValue().toString());
                MarketTrasactionValue.setText(string_MarketTrasactionValue);


                String string_TransactionVolume = FormatNumber(get_TransactionVolume.getValue().toString());
                TransactionVolume.setText(string_TransactionVolume);
                String string_NoOfMarketTrades = FormatNumber(get_NoOfMarketTrades.getValue().toString());
                NoOfMarketTrades.setText(string_NoOfMarketTrades);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //TODO Define GridView To show Data with box beside
        gridView = new ExpandGridView(this);
        gridView = (ExpandGridView) findViewById(R.id.grid);
        gridView.setExpanded(true);
        gridView.setFocusable(false); // ToDO make Gridview not start at top of screen you have to set.

        perferredStockDataAdapter = new PerferredStockDataAdapter(this);


        //TODO Get Data For Prefered list
        myRef_2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot get_StocksData = dataSnapshot.child("Stocks_Data");
                dataSnapshot_StocksData = get_StocksData;


                gridView.setAdapter(perferredStockDataAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //TODO Line Chart Steps
      /*  LineChart graph = (LineChart) findViewById(R.id.chart);

// creating list of entry
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 4f));
        entries.add(new Entry(1, 8f));
        entries.add(new Entry(2, 6f));
        entries.add(new Entry(3, 2f));
        entries.add(new Entry(4, 18f));
        entries.add(new Entry(5, 9f));

        LineDataSet dataSet = new LineDataSet(entries, "# of Calls");
        LineData lineData = new LineData(dataSet);

        graph.getXAxis().setEnabled(false);
        graph.getAxisLeft().setDrawAxisLine(false);
        graph.setData(lineData);
*/

        //TODO Portfolio


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String NIN = bundle.getString("NIN");
            String Password = bundle.getString("Password");
            try {
                JSONObject Portfolio_Request = new JSONObject();
                Portfolio_Request.put("NIN", NIN);
                Portfolio_Request.put("Password", Password);
                Portfolio_Request.put("MobileNumber", "");

                AndroidNetworking
                        .post(URL.URL_Portfolio)
                        .addHeaders("Accept", "application/json")
                        .addHeaders("Content-type", "application/json")
                        .addJSONObjectBody(Portfolio_Request)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                get_result = response;


                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        try {
            JSONArray get_jsonarray = get_result.getJSONArray("portfolios");
            for (int i = 0; i < get_jsonarray.length(); i++) {
                JSONObject jsonas = get_jsonarray.getJSONObject(i);



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


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



        /*    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    dataSnap = dataSnapshot_StocksData.child(String.valueOf(position));

                    final CharSequence[] items = {"BUY", "SELL", "Cancel"};

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





                }
            });*/

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
