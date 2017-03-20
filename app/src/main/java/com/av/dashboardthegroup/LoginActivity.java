package com.av.dashboardthegroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Aya on 28-02-2017.
 */

public class LoginActivity extends Activity {

    EditText Investor_No,Password;
    Button Login;
    /** Called when the activity is first created */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        Investor_No = (EditText) findViewById(R.id.Investor_No);
        Password    = (EditText) findViewById(R.id.Password);
        Login       = (Button)   findViewById(R.id.btn_login);

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        Calendar cal = Calendar.getInstance();
        String today = dateFormat.format(cal.getTime());
        Toast.makeText(LoginActivity.this,today,Toast.LENGTH_SHORT).show();
        cal.add(Calendar.MONTH,-1);
        String before = dateFormat.format(cal.getTime());
        Toast.makeText(LoginActivity.this,before,Toast.LENGTH_SHORT).show();


/*
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.MONTH,-1);*/
        //String before = dateFormat.format(cal2.getTime());




 Login.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  if (AppStatus.getInstance(LoginActivity.this).isOnline()) {
                                      Toast.makeText(LoginActivity.this, "You are online!!!!", Toast.LENGTH_SHORT).show();
                                      String getInvestor_No = Investor_No.getText().toString().trim();
                                      String getpassword = Password.getText().toString().trim();
                                      Parse_Login(getInvestor_No,getpassword);

                                  } else {

                                      Toast.makeText(LoginActivity.this, "You are not online!!!!", Toast.LENGTH_SHORT).show();

                                  }




                              }
                          }

       );

    }

    public void Parse_Login(String Inverstor_No,  String getPassword){
        try {
            JSONObject Loginobj = new JSONObject();
            Loginobj.put("UserName",Inverstor_No);
            Loginobj.put("Password", getPassword);
            Loginobj.put("Lang", "ENG");

            JSONObject Login_Data = new JSONObject();
            Login_Data.put("Login", Loginobj);

            JSONObject Request_Login = new JSONObject();
            Request_Login.put("Message", Login_Data);
            Request_Login.put("AppId", "");
            Request_Login.put("Srv", "Login");
            Request_Login.put("Version", "1");
            Request_Login.put("LstLogin", "");

            AndroidNetworking
                    .post(URL.URL_Login)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Content-type", "application/json")
                    .addJSONObjectBody(Request_Login)
                    .setPriority(Priority.HIGH)
                    .setTag("Test")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String result = response.getJSONObject("ResponseStatus").getString("ResCode");
                                if (result.equals("0")) {
                                    APP.SessionID = response.getJSONObject("Message").getJSONObject("LogResponse").getString("SessionID");
                                    //   Toast.makeText(LoginActivity.this, APP.SessionID, Toast.LENGTH_SHORT).show();
                                    String strUserName = Investor_No.getText().toString().trim();
                                    String password = Password.getText().toString().trim();
                                    JSONObject Portfolio_Request = new JSONObject();
                                    Portfolio_Request.put("NIN",strUserName);
                                    Portfolio_Request.put("Password", password);
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
                                                    Intent success = new Intent(LoginActivity.this, MainActivity.class);
                                                    success.putExtra("Response", response.toString());
                                                    startActivity(success);

                                                }

                                                @Override
                                                public void onError(ANError anError) {

                                                }
                                            });


                                } else {
                                    String error = response.getJSONObject("ResponseStatus").getString("ResDesc");
                                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(LoginActivity.this, anError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });



        } catch (JSONException e) {
            e.printStackTrace();

        }


    }



    }






