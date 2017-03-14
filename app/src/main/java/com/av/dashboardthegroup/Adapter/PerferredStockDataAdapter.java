package com.av.dashboardthegroup.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.av.dashboardthegroup.MainActivity;
import com.av.dashboardthegroup.R;
import com.google.firebase.database.DataSnapshot;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by Aya on 3/14/2017.
 */

public class PerferredStockDataAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    DataSnapshot dataSnap;
    private Activity activity;
    MyViewHoldwer holder = null;

    public PerferredStockDataAdapter(Activity a,DataSnapshot get_data) {
        dataSnap=get_data;
        activity=a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }
    @Override
    public int getCount() {
        return (int)dataSnap.getChildrenCount();
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


      DataSnapshot  child = dataSnap.child(String.valueOf(position));

        holder.Symbol.setText(child.child("Symbol").getValue().toString());

        String get_CurrentPrice = BigDecimal(child.child("CurrentPrice").getValue().toString());
        holder.CurrentPrice.setText(get_CurrentPrice);

        String get_ChangePercentage = BigDecimal(child.child("ChangePercentage").getValue().toString());
        holder.ChangePercentage.setText(get_ChangePercentage);

        String get_ChangeValue = BigDecimal(child.child("ChangeValue").getValue().toString());
        holder.ChangeValue.setText(get_ChangeValue);



        if(child.child("ChangeSign").getValue().toString().equals("+")){
            holder.ChangePercentage.setTextColor(activity.getResources().getColor(R.color.plus_sign));
            holder.ChangeValue.setTextColor(activity.getResources().getColor(R.color.plus_sign));

        }
        if(child.child("ChangeSign").getValue().toString().equals("-")){
            holder.ChangePercentage.setTextColor(activity.getResources().getColor(R.color.minus_sign));
            holder.ChangeValue.setTextColor(activity.getResources().getColor(R.color.minus_sign));

        }
        if(child.child("ChangeSign").getValue().toString().equals("=")){
            holder.ChangePercentage.setTextColor(activity.getResources().getColor(R.color.equal_sign));
            holder.ChangeValue.setTextColor(activity.getResources().getColor(R.color.minus_sign));

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


    static  class MyViewHoldwer {
        TextView Symbol, CurrentPrice, ChangePercentage, ChangeValue;

        public MyViewHoldwer(View v) {
            Symbol = (TextView) v.findViewById(R.id.txt_Symbol);
            CurrentPrice = (TextView) v.findViewById(R.id.txt_CurrentPrice);
            ChangePercentage = (TextView) v.findViewById(R.id.txt_ChangePercentage);
            ChangeValue = (TextView) v.findViewById(R.id.txt_ChangeValue);
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



