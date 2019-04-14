package com.sednalabs.newscalender;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class NewsSourceDownloader extends AsyncTask<String,Void,String> {


    private static final String TAG = "NewsSourceDownloader";
   private MainActivity ma;
    private ArrayList<ArrayList<String>> sources = new ArrayList<>();
    private ArrayList<String> categoryList = new ArrayList<>();



    public NewsSourceDownloader(MainActivity ma){
        this.ma = ma;

    }


    @Override
    protected String doInBackground(String... params) {
        final String newssourceurl = "https://newsapi.org/v1/sources?language=en";

        Uri.Builder buildURL = Uri.parse(newssourceurl).buildUpon();
        buildURL.appendQueryParameter("country",params[0]);
        buildURL.appendQueryParameter("category",params[1]);
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: " + urlToUse);


        StringBuilder sb = new StringBuilder();
        try {

            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(ir);

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

           // Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }


        String s = sb.toString();


        return s;
    }


    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            try {
                JSONObject jsonObjects = new JSONObject(s);
                JSONArray array = jsonObjects.getJSONArray("sources");
               final int q=array.length();
                for(int i=0;i<q;i++) {
                    JSONObject test =  array.getJSONObject(i);
                    String id = test.getString("id");
                    String name = test.getString("name");
                    String url = test.getString("url");
                    String category = test.getString("category");
                    String desc = test.getString("description");
                    /*if(!categoryList.contains(category))*/
                            categoryList.add(category);
                   // Log.d(TAG, "onpostExecute: ***" + id + "***" + name + "***" + url + "***");

                    ArrayList<String> t=new ArrayList<>();
                    t.add(0,id); t.add(1,name); t.add(2,url);t.add(3,desc);
                   sources.add(t);
                   // Log.d(TAG, "onpostExecute: ***" + sources);
                }



            } catch (Exception e) {
                e.printStackTrace();
            }

            ma.setsources(sources,categoryList);
        }
    }
}
