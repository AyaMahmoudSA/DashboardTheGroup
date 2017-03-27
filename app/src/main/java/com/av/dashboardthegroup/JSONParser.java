package com.av.dashboardthegroup;

/**
 * Created by Maiada on 3/27/2017.
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONParser {
    int timeoutConnection = 10000;
    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jArr = null;
    static String json = "";

    // constructor
    public JSONParser() {
    }

    @SuppressWarnings("deprecation")
    public JSONObject getJSONFromUrl(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            @SuppressWarnings("deprecation")
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url);

            // httpGet.addHeader("Accept-Encoding", "gzip");

            HttpParams httpParameters = new BasicHttpParams();

            // Set the timeout in milliseconds until a connection is

            httpParameters.setIntParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, timeoutConnection);
            httpParameters.setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
                    timeoutConnection);
            httpParameters.setLongParameter(ConnManagerPNames.TIMEOUT,
                    timeoutConnection);
            // DefaultHttpClient httpClient = new
            // DefaultHttpClient(httpParameters);
            // HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");

            is = httpEntity.getContent();
            if (contentEncoding != null
                    && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                is = new GZIPInputStream(is);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf-8"), 8);
            // iso-8859-1
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();

            jObj = new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // return JSON String
        return jObj;

    }


    @SuppressWarnings("deprecation")
    public String getStringFromUrl(String url) {

        // Making HTTP request
        try {
            @SuppressWarnings("deprecation")
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url);

            // httpGet.addHeader("Accept-Encoding", "gzip");
            HttpParams httpParameters = new BasicHttpParams();

            // Set the timeout in milliseconds until a connection is

            httpParameters.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeoutConnection);
            httpParameters.setIntParameter(CoreConnectionPNames.SO_TIMEOUT,timeoutConnection);
            httpParameters.setLongParameter(ConnManagerPNames.TIMEOUT,timeoutConnection);


            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");

            is = httpEntity.getContent();
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                is = new GZIPInputStream(is);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            // iso-8859-1
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // return String
        return json;

    }

    @SuppressWarnings("deprecation")
    public JSONArray getJSONArrFromUrl(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            // HttpPost httpPost = new HttpPost(url);
            HttpParams httpParameters = new BasicHttpParams();

            // Set the timeout in milliseconds until a connection is
            httpParameters.setIntParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, timeoutConnection);
            httpParameters.setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
                    timeoutConnection);
            httpParameters.setLongParameter(ConnManagerPNames.TIMEOUT,
                    timeoutConnection);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            Header contentEncoding = httpResponse
                    .getFirstHeader("Content-Encoding");
            is = httpEntity.getContent();
            if (contentEncoding != null
                    && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                is = new GZIPInputStream(is);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            // iso-8859-1
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            jArr = new JSONArray(json);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // return JSON String
        return jArr;

    }
}
