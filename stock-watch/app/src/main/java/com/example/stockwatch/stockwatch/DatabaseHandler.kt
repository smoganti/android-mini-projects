package com.example.stockwatch.stockwatch

/**
 * Created by vamse on 3/18/2017.
 */

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import java.util.ArrayList

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val database: SQLiteDatabase


    init {
        database = writableDatabase
        Log.d(TAG, "DatabaseHandler: Constructor DONE")
    }

    override fun onCreate(db: SQLiteDatabase) {
        // onCreate is only called is the DB does not exist
        Log.d(TAG, "onCreate: Making New DB")
        db.execSQL(SQL_CREATE_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {


    }

    fun loadStocks(): ArrayList<Stock> {

        Log.d(TAG, "loadStocks: Load all symbol-company entries from DB")
        val stocks = ArrayList<Stock>()

        val cursor = database.query(
                TABLE_NAME, // The table to query
                arrayOf(STOCKID, STOCKNAME), null, null, null, null, null)// The columns to return
        // The columns for the WHERE clause
        // The values for the WHERE clause
        // don't group the rows
        // don't filter by row groups
        // The sort order
        if (cursor != null) {
            cursor.moveToFirst()

            for (i in 0 until cursor.count) {
                val stockid = cursor.getString(0)
                val stockName = cursor.getString(1)
                stocks.add(Stock(stockid, stockName))
                cursor.moveToNext()
            }
            cursor.close()
        }
        Log.d(TAG, "loadCountries: DONE LOADING Stock DATA FROM DB")

        return stocks
    }

    fun addStock(stock: Stock) {

        Log.d(TAG, "addStock: Adding " + stock.stocksymbol)
        val values = ContentValues()
        values.put(STOCKID, stock.stocksymbol)
        values.put(STOCKNAME, stock.stockName)
        database.insert(TABLE_NAME, null, values)
        Log.d(TAG, "addStock: Add Complete")
    }

    fun deleteStock(symbol: String) {
        Log.d(TAG, "deleteStock: Deleting Stock " + symbol)
        val cnt = database.delete(TABLE_NAME, STOCKID + "= ?", arrayOf(symbol))
        Log.d(TAG, "deleteStock: " + cnt)
    }

    companion object {

        private val TAG = "DatabaseHandler"

        // If you change the database schema, you must increment the database version.
        private val DATABASE_VERSION = 1

        // DB Name
        private val DATABASE_NAME = "StockAppDB"
        // DB Table Name
        private val TABLE_NAME = "StockTable"
        ///DB Columns
        private val STOCKID = "StockID"
        private val STOCKNAME = "StockName"


        // DB Table Create Code
        private val SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                STOCKID + " TEXT not null unique," +
                STOCKNAME + " TEXT not null)"
    }

    /*
    public void dumpLog() {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d(TAG, "dumpLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            for (int i = 0; i < cursor.getCount(); i++) {
                String stockid = cursor.getString(0);
                String stockName = cursor.getString(1);
                Log.d(TAG, "dumpLog: " +
                        String.format("%s %-18s", STOCKID + ":", stockid) +
                        String.format("%s %-18s", STOCKNAME + ":", stockName));
                cursor.moveToNext();
            }
            cursor.close();
        }

        Log.d(TAG, "dumpLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }
*/

}

