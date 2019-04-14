package com.example.stockwatch.stockwatch

import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.AsyncTask
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputFilter
import android.text.InputType
import android.util.JsonReader
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import org.json.JSONArray
import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Array
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Collections
import java.util.Date
import java.util.HashMap

class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener, Asynclinker, Asynclinker2 {

    internal var t1 = "Main Activity"
    private var swiper: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var myAdapter: Adapter? = null
    private val mystocksList = ArrayList<Stock>()
    private var databaseHandler: DatabaseHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        recyclerView = findViewById(R.id.recycler) as RecyclerView

        myAdapter = Adapter(mystocksList, this)

        recyclerView!!.adapter = myAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)



        databaseHandler = DatabaseHandler(this)

        if (networkCheck()) {
            val list = databaseHandler!!.loadStocks()

            val m = list.size
            if (m == 0) {
                // do nothing and continue
            } else {
                for (i in 0 until m) {

                    val task = MydataAsyncTask(this@MainActivity)
                    task.execute(list[i].stocksymbol, list[i].stockName)

                }
            }
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Please check your Internet connection and try again !")
            builder.setTitle("No Network Connection")

            val dialog = builder.create()
            dialog.show()

        }
        swiper = findViewById(R.id.swiper) as SwipeRefreshLayout
        swiper!!.setOnRefreshListener { doRefresh() }

        Log.d(t1, "\t Inside onCreate Method")
    }

    private fun doRefresh() {

        if (networkCheck()) {
            val cStock = databaseHandler!!.loadStocks()
            mystocksList.clear()
            for (i in cStock.indices) {

                val task = MydataAsyncTask(this@MainActivity)
                task.execute(cStock[i].stocksymbol, cStock[i].stockName)

            }

            swiper!!.isRefreshing = false
            myAdapter!!.notifyDataSetChanged()
            Toast.makeText(this, "Stocks Updated", Toast.LENGTH_SHORT).show()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Please Check your internet connection and try again !")
            builder.setTitle("No Network Connection")

            val dialog = builder.create()
            dialog.show()
            swiper!!.isRefreshing = false

        }

    }

    fun networkCheck(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.add_stock -> {
                add_stockdialog()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun add_stockdialog() {

        if (networkCheck()) {
            val builder = AlertDialog.Builder(this)

            val et = EditText(this)
            et.inputType = InputType.TYPE_CLASS_TEXT
            et.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
            et.gravity = Gravity.CENTER_HORIZONTAL

            builder.setView(et)
            builder.setMessage("Please enter a stock symbol:")
            builder.setTitle("Stock Selection")
            builder.setPositiveButton("OK") { dialog, id ->
                val sid = et.text.toString().trim { it <= ' ' }
                val task = MynameAsyncTask(this@MainActivity)
                task.execute(sid)
            }
            builder.setNegativeButton("CANCEL") { dialog, id ->
                // User cancelled the dialog
            }

            val dialog = builder.create()
            dialog.show()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Please Check your internet connection and try again !")
            builder.setTitle("No Network Connection")

            val dialog = builder.create()
            dialog.show()
        }

    }


    override fun onPause() {
        super.onPause()


        Log.d(t1, "\t Inside OnPause Method")
    }


    override fun onResume() {
        super.onResume()

        Log.d(t1, "\t Inside onResume Method")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(t1, "\t Inside OnDestroy Method")
    }

    override fun onClick(v: View) {
        val position = recyclerView!!.getChildLayoutPosition(v)
        val stock = mystocksList[position]

        val url = stockURL + stock.stocksymbol
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)

    }

    override fun onLongClick(v: View): Boolean {
        val pos = recyclerView!!.getChildLayoutPosition(v)
        val s = mystocksList[pos]
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton("Delete") { dialog, id ->
            databaseHandler!!.deleteStock(mystocksList[pos].stocksymbol)
            mystocksList.removeAt(pos)
            myAdapter!!.notifyDataSetChanged()
        }
        builder.setNegativeButton("Cancel") { dialog, id ->
            // User cancelled the dialog
        }

        builder.setMessage("Do you wish to Delete StockSymbol " + mystocksList[pos].stocksymbol + "?")
        builder.setTitle("Delete Stock")

        val dialog = builder.create()
        dialog.show()


        return true

    }


    override fun asyncNameloadPostExec(jsonList: ArrayList<Stock>) {

        if (jsonList.isEmpty()) {
            val builder1 = AlertDialog.Builder(this)
            builder1.setMessage("Error No Such Stock Exists")
            builder1.setTitle("Error")

            val dialog1 = builder1.create()

            dialog1.show()
        } else if (jsonList.size == 1) {
            val stockid = jsonList[0].stocksymbol
            val stockname = jsonList[0].stockName
            val task = MydataAsyncTask(this@MainActivity)
            task.execute(stockid, stockname)

        } else {
            val IsArray = arrayOfNulls<CharSequence>(jsonList.size)

            for (i in jsonList.indices)
                IsArray[i] = jsonList[i].stocksymbol + " - " + jsonList[i].stockName

            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Select from the List")
            builder.setItems(IsArray) { dialog, which ->
                //tv2.setText(sArray[which]);
                /*selectedName = IsArray[which].toString();*/
                val stockid2 = jsonList[which].stocksymbol
                val stockname2 = jsonList[which].stockName
                val task = MydataAsyncTask(this@MainActivity)
                task.execute(stockid2, stockname2)
            }

            builder.setNegativeButton("Cancel") { dialog, id ->
                // User cancelled the dialog
            }

            val dialog = builder.create()

            dialog.show()
        }


    }

    override fun addNewStock(stockdata: Stock) {

        if (mystocksList.size != 0) {

            if (mystocksList.toString().contains(stockdata.stockName)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Stock Symbol " + stockdata.stocksymbol + " is already present")
                builder.setTitle("Duplicate Stock")

                val dialog = builder.create()
                dialog.show()
            } else {
                mystocksList.add(stockdata)
                Collections.sort(mystocksList)
                databaseHandler!!.addStock(stockdata)
                myAdapter!!.notifyDataSetChanged()
            }
        } else {
            mystocksList.add(stockdata)
            Collections.sort(mystocksList)
            databaseHandler!!.addStock(stockdata)
            myAdapter!!.notifyDataSetChanged()

        }
    }

    companion object {
        private val stockURL = "http://www.marketwatch.com/investing/stock/"
    }

}

