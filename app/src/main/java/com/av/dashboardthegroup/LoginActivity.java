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




 Login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
     String strUserName = Investor_No.getText().toString().trim();
     String password = Password.getText().toString().trim();

    try {
        JSONObject Loginobj = new JSONObject();
        Loginobj.put("UserName",strUserName);
        Loginobj.put("Password", password);
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
              Portfolio_Request.put("NIN", strUserName);
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


       );

    }




    }
