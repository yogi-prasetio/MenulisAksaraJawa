package com.android.menulisaksarajawa.ui.database;

import android.util.Log;

import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jArr = null;
    static String json = "";
    static String error = "";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
                                      ArrayList params) {

        // Making HTTP request
        try {

            // check for request method
            if(method.equals("POST")){
                // request method is POST
                // defaultHttpClient
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

//                httpPost.setEntity(new UrlEncodedFormEntity(params));
//                try {
//                    Log.e("API123", " " +convertStreamToString(httpPost.getEntity().getContent()));
//                    Log.e("API123",httpPost.getURI().toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                HttpResponse httpResponse = httpClient.execute(httpPost);
//                Log.e("API123",""+httpResponse.getStatusLine().getStatusCode());
//                error= String.valueOf(httpResponse.getStatusLine().getStatusCode());
//                HttpEntity httpEntity = httpResponse.getEntity();
//                is = httpEntity.getContent();

                StringBuilder requestBody = new StringBuilder();


                List<BasicNameValuePair> paramList = new ArrayList<>(params);
                for (BasicNameValuePair param : paramList) {
                    if (requestBody.length() != 0) {
                        requestBody.append("&");
                    }
                    requestBody.append(param.getName()).append("=").append(param.getValue());
                }

                // Menentukan entitas langsung tanpa URL encoding
                StringEntity stringEntity = new StringEntity(requestBody.toString(), "UTF-8");
                stringEntity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(stringEntity);

                try {
                    Log.e("API123", " " + convertStreamToString(httpPost.getEntity().getContent()));
                    Log.e("API123", httpPost.getURI().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                HttpResponse httpResponse = httpClient.execute(httpPost);
                Log.e("API123", "" + httpResponse.getStatusLine().getStatusCode());
                error = String.valueOf(httpResponse.getStatusLine().getStatusCode());
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if(method.equals("GET")){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
//                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += params.get(0);
                Log.d("API_GET", url);
//                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.d("API123",json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try to parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            jObj.put("error_code",error);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
}
