package com.example.stockwatch.stockwatch

import android.net.Uri
import android.os.AsyncTask
import android.util.Log

import org.json.JSONArray
import org.json.JSONObject

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by vamse on 3/18/2017.
 */

class MynameAsyncTask internal constructor(private val link: Asynclinker) : AsyncTask<String, Void, String>() {

    private val stockURL = "http://stocksearchapi.com/api/" + "?api_key=a8daa99e10f46356038711c93903d53d05c43d93"
    internal val t1 = "MynameAsyncTask"
    internal var jsonList = ArrayList<Stock>()
    internal lateinit var userSearchText: String


    override fun doInBackground(vararg params: String): String? {

        userSearchText = params[0]

        val buildURL = Uri.parse(stockURL).buildUpon()
        buildURL.appendQueryParameter("search_text", params[0])

        val urlToUse = buildURL.build().toString()
        Log.d(t1, "doInBackground: " + urlToUse)

        val sb = StringBuilder()
        try {
            val url = URL(urlToUse)

            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            val `is` = conn.inputStream
            val ISR = InputStreamReader(`is`)
            val reader = BufferedReader(ISR)
            if (reader != null){
            var line: String
            line = reader.readLine()
            while ((line) != null) {
                sb.append(line).append('\n')
            }
            Log.d(t1, "doInBackground: " + sb.toString())
        }
        } catch (e: Exception) {
            Log.e(t1, "do in Background", e)
        }

        parseJSON(sb.toString())

        return null

    }


    private fun parseJSON(s: String) {

        try {
            val myJsonList = JSONArray(s)

            for (i in 0 until myJsonList.length()) {
                val jobj = myJsonList.getJSONObject(i)
                val id = jobj.getString("company_symbol")
                val name = jobj.getString("company_name")
                jsonList.add(Stock(id, name))
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onPostExecute(s: String) {
        link.asyncNameloadPostExec(jsonList)


    }
}