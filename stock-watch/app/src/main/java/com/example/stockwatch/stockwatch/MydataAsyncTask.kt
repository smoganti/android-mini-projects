package com.example.stockwatch.stockwatch

import android.net.Uri
import android.os.AsyncTask
import android.util.Log

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Constructor
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by vamse on 3/15/2017.
 */

class MydataAsyncTask internal constructor(private val linker: Asynclinker2) : AsyncTask<String, Void, String>() {

    private val finURL = "http://finance.google.com/finance/info?client=ig"
     lateinit var stockdata: Stock
     lateinit var stockcompany: String
     lateinit var stocksymbol: String


    override fun onPostExecute(s: String) {
        linker.addNewStock(stockdata!!)
    }

    override fun doInBackground(vararg params: String): String? {

        stocksymbol = params[0]
        stockcompany = params[1]

        val buildURL = Uri.parse(finURL).buildUpon()
        buildURL.appendQueryParameter("q", params[0])
        val urlToUse = buildURL.build().toString()
        Log.d(TAG, "doInBackground: " + urlToUse)

        val sb = StringBuilder()
        try {
            val url = URL(urlToUse)

            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            val `is` = conn.inputStream
            val reader = BufferedReader(InputStreamReader(`is`))

            var line: String
            line = reader.readLine()
            while ((line ) != null) {
                sb.append(line).append('\n')
            }

            Log.d(TAG, "doInBackground: " + sb.toString().replace("// [", " ["))

        } catch (e: Exception) {
            Log.e(TAG, "doInBackground: ", e)
            return null
        }


        parseJSON(sb.toString().replace("// [", " ["))

        return null
    }

    private fun parseJSON(s: String) {

        try {
            val myJsonFlist = JSONArray(s)

            val jsonobjects = myJsonFlist.getJSONObject(0)
            val ticker = jsonobjects.getString("t")
            if (ticker == stocksymbol) {

                stockdata = Stock(stocksymbol, stockcompany, jsonobjects.getString("l"), jsonobjects.getString("c"), jsonobjects.getString("cp"))
                Log.d(TAG, "parseJSON: " + stockdata!!.toString())
                /*stockdata.setPrice(jsonobjects.getString("l"));
                stockdata.setPriceChange(jsonobjects.getString("c"));
                stockdata.setChangePercent(jsonobjects.getString("cp"));*/
            } else {
                // Toast.makeText(getApplicationContext(), "Error downloading data ", Toast.LENGTH_SHORT);
                return
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    companion object {
        private val TAG = "AsyncFLoaderTask"
    }
}

