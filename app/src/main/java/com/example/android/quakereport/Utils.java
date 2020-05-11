package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public abstract class Utils {

    private static final String LOG_TAG = Utils.class.getName();

    public static List<Earthquake> fetchEarthquakeData(String requestUrl)  {
        // create url object.
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "error with creating URL",e);
        }

        String jsonResponce = null;
        try {
            jsonResponce = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem making the HTTP request",e);
        }

        List<Earthquake> earthquakeEvents = extractFeatureFromJson(jsonResponce);

        return earthquakeEvents;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResonse = "";

        if(url == null)
            return null;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResonse = readFromStream(inputStream);
            }
            else
                Log.e(LOG_TAG, "error response code: " + urlConnection.getResponseCode());
        } catch (IOException e) {
            Log.d(LOG_TAG, "Problem retrieving the earthquake JSON results.",e);
        }finally {
            if(urlConnection != null)
                urlConnection.disconnect();

            if(inputStream != null)
                inputStream.close();
        }

        return jsonResonse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Earthquake> extractFeatureFromJson(String earthquakeJson){
        if(TextUtils.isEmpty(earthquakeJson))
            return null;

        List<Earthquake> earthquakes = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(earthquakeJson);
            JSONArray features = root.getJSONArray("features");

            for(int i = 0; i < features.length(); i++){
                JSONObject featureObject = (JSONObject) features.get(i);
                JSONObject properties = featureObject.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");

                String logation = properties.getString("place");

                long timeInMilliseconds = properties.getLong("time");

                String url = properties.getString("url");

                earthquakes.add(new Earthquake(magnitude,logation,timeInMilliseconds,url));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results",e);
        }

        return earthquakes;
    }


}
