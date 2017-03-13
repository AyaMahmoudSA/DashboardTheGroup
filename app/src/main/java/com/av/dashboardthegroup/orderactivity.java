package com.av.dashboardthegroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by Maiada on 3/13/2017.
 */

public class orderactivity extends Activity {
    WebView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

  /*      display = (WebView)findViewById(R.id.webview);
        display.loadUrl("http://sa3ednyclient.azurewebsites.net/#/home");
        WebSettings webSettings = display.getSettings();
        webSettings.setJavaScriptEnabled(true);*/

        /*savedInstanceState=getIntent().getExtras();
        Toast.makeText(orderactivity.this,savedInstanceState.get("Symbol").toString(),Toast.LENGTH_SHORT).show();*/

        /*display.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }});*/

    }
}
