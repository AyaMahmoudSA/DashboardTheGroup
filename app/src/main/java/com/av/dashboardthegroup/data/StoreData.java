package com.av.dashboardthegroup.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.av.dashboardthegroup.APP;

/**
 * Created by Maiada on 3/22/2017.
 */

public class StoreData {

    SharedPreferences  sharedPreferences;
    SharedPreferences.Editor editor;


    public static final String PREFS_NAME = "com.av.dashboardthegroup";
    private String inverstor_num = "NIN";
    private String inverstor_pass = "PASSWORD";


    public StoreData(){
        super();
        sharedPreferences = APP.context.getSharedPreferences(PREFS_NAME , Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public  void   SaveInvestor_No(String Investor_No){
        editor.putString(inverstor_num,Investor_No);
        editor.commit();
    }

    public  String LoadInvestor_No(){

        String  getInvestor_No= sharedPreferences.getString(inverstor_num, "");
        return  getInvestor_No;
    }


    public void SavePassword(String Password){
      editor.putString(inverstor_pass,Password);
      editor.commit();
    }


    public  String LoadPassword(){
        String getPassword=sharedPreferences.getString(inverstor_pass,"");
        return  getPassword;


    }


}
