package com.av.dashboardthegroup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.av.dashboardthegroup.Adapter.PerferredStockDataAdapter;
import com.av.dashboardthegroup.Adapter.PortfoliosAdapter;
import com.av.dashboardthegroup.Expandable.ExpandGridView;
import com.av.dashboardthegroup.Expandable.ExpandListView;
import com.av.dashboardthegroup.Model.MarketChartData;
import com.av.dashboardthegroup.Model.Portfolios;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.R.attr.animation;

/**
 * Created by Aya on 02-03-2017.
 */

public class MainActivity extends AppCompatActivity {

    TextView CurrentMarketIndex, ChangePercentage, ChangeValue,
             MarketTrasactionValue, TransactionVolume, NoOfMarketTrades;
    CardView cardView;

    LinearLayout linearLayout ;

    private ExpandGridView gridView;
    public static DataSnapshot dataSnapshot_StocksData;
    public PerferredStockDataAdapter perferredStockDataAdapter;

    ArrayList<MarketChartData> addchartvalue;


    public PortfoliosAdapter portfoliosAdapter;
    private ExpandListView listview;
    public  static  ArrayList<Portfolios> get_respone;
    Animation animation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO Make connection to get data
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        //TODO make logo behind name of activity "Dashboard"
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.actionbar);

        //TODO  Declare textview from view to access it .
        //TODO First box about market data
        CurrentMarketIndex = (TextView) findViewById(R.id.txt_CurrentMarketIndex);
        ChangePercentage = (TextView) findViewById(R.id.txt_ChangePercentage);
        ChangeValue = (TextView) findViewById(R.id.txt_ChangeValue);

        //TODO Second box about market data
        MarketTrasactionValue = (TextView) findViewById(R.id.txt_MarketTrasactionValue);
        TransactionVolume = (TextView) findViewById(R.id.txt_TransactionVolume);
        NoOfMarketTrades = (TextView) findViewById(R.id.txt_NoOfMarketTrades);

        linearLayout   = (LinearLayout) findViewById(R.id.linear_color);


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
                    linearLayout.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.shadow_green));                  //  ChangeValue.setTextColor(getResources().getColor(R.color.plus_sign));

                }else{

                    if (get_Sign.getValue().toString().equals("-")) {
                        linearLayout.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.shadow_red));

                    }else{
                        if (get_Sign.getValue().toString().equals("=")) {
                            linearLayout.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.shadow_yellow));                  //  ChangeValue.setTextColor(getResources().getColor(R.color.minus_sign));

                        }

                    }
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

        animation = AnimationUtils.loadAnimation(this,R.anim.grid_item_anim);
        GridLayoutAnimationController controller = new GridLayoutAnimationController(animation, .2f, .2f);
        gridView.setLayoutAnimation(controller);
   /*     cardView=(CardView)findViewById(R.id.cardview);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        cardView.setBackgroundColor(color);*/


        //TODO Get Data For Prefered list
        myRef_2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot get_StocksData = dataSnapshot.child("Stocks_Data");
            //    dataSnapshot_StocksData = get_StocksData;
                perferredStockDataAdapter = new PerferredStockDataAdapter(MainActivity.this,get_StocksData);
                gridView.setAdapter(perferredStockDataAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //END

        //TODO Portfolio
        get_respone=new ArrayList<>();
        listview = new ExpandListView(this);
        listview = (ExpandListView) findViewById(R.id.lv_portfolio);
        listview.setExpanded(true);
        listview.setFocusable(false);

            try {
            //    get_respone.clear();
                Portfolios full_portfolio = new Portfolios();

                JSONObject   json_object = new JSONObject(getIntent().getStringExtra("Response"));
                APP.Balance = json_object.getString("Balance");
                JSONArray get_portfolios = json_object.getJSONArray("portfolios");
                for (int i = 0; i < get_portfolios.length(); i++) {
                    Portfolios portfolio = new Portfolios();
                    JSONObject portfolios = get_portfolios.getJSONObject(i);
                    portfolio.setMarketValue(portfolios.getString("MarketValue"));
                    portfolio.setNoOfStocks(portfolios.getString("NoOfStocks"));
                    JSONObject company = portfolios.getJSONObject("company");
                    portfolio.setSymbol(company.getString("Symbol"));
                    get_respone.add(portfolio);
                   // m.put(json_object.getString("Balance"));
                  //  m.put(i,portfolios.getString("MarketValue"));
                }

                //   Chart_Data.put("Charts_Data",m);
                portfoliosAdapter =new PortfoliosAdapter(MainActivity.this,get_respone);
                listview.setAdapter(portfoliosAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
         //END

         // TODO Get MarketChart
        AndroidNetworking.get(URL.URL_MarketChartData)
                .setPriority(Priority.HIGH)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-type", "application/json")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            JSONArray jsonArray=response;
                            addchartvalue =new ArrayList<>();
                            for(int i=0 ;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                MarketChartData marketChartData=new MarketChartData();
                                marketChartData.setDate(object.getString("Date"));
                                marketChartData.setPrice(object.getString("Price"));
                                addchartvalue.add(marketChartData);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        animation.cancel();
    }



}
