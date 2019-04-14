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

/**
 * Created by vamse on 4/19/2017.
 */

public class NewsArticleDownloader extends AsyncTask<String,Void,String> {

    private static final String TAG = "NewsArticleDownloader";
    NewsService ns;
    String newssource;
    ArrayList<Articles> sourcedata = new ArrayList<>();
    private final String newssourcearticle = "https://newsapi.org/v1/articles";


    public NewsArticleDownloader(NewsService ns,String newssource) {
        this.ns = ns;
        this.newssource = newssource;
    }


    @Override
    protected String doInBackground(String... params) {
        Uri.Builder buildURL = Uri.parse(newssourcearticle).buildUpon();
        buildURL.appendQueryParameter("source",newssource);
        //buildURL.appendPath("&apiKey=a1b2c3");
        String urlToUse = buildURL.build().toString()+"&apiKey=26cbb938c07d4adaa552f9eb17931ac8";
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
                JSONArray array = jsonObjects.getJSONArray("articles");
               final int q=array.length()-1;
                for(int i=0;i<q;i++) {
                    JSONObject test =  array.getJSONObject(i);
                    String author = test.getString("author");
                    String title = test.getString("title");
                    String desc = test.getString("description");
                    String urltoimg = test.getString("urlToImage");
                    String publishedat = test.getString("publishedAt");
                    String url = test.getString("url");

                    // Log.d(TAG, "onpostExecute: ***" + id + "***" + name + "***" + url + "***");


                    sourcedata.add(new Articles(title,author,publishedat,desc,urltoimg,url));

                  //  Log.d(TAG, "onpostExecute: ***" + sourcedata);
                }



            } catch (Exception e) {
                e.printStackTrace();
            }

            ns.setarticles(sourcedata);
        }
    }
}
