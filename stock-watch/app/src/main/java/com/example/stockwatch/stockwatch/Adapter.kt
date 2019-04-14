package com.example.stockwatch.stockwatch

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Adapter(internal var mystockList: List<Stock>, internal var myMainActivity: MainActivity) : RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stock_list, parent, false)
        itemView.setOnClickListener(myMainActivity)
        itemView.setOnLongClickListener(myMainActivity)
        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val stock = mystockList[position]
        if (java.lang.Float.parseFloat(stock.changePercent) >= 0) {
            holder.stockID!!.text = stock.stocksymbol
            holder.stockVal!!.text = stock.price + "     \u25B2" + stock.priceChange.replace("+", "") + "(" + stock.changePercent + "%)"
            holder.stockfullName!!.text = stock.stockName
            holder.stockID!!.setTextColor(Color.GREEN)
            holder.stockVal!!.setTextColor(Color.GREEN)
            holder.stockfullName!!.setTextColor(Color.GREEN)

        } else {
            holder.stockID!!.text = stock.stocksymbol
            holder.stockVal!!.text = stock.price + "     \u25BC" + stock.priceChange.replace("+", "") + "(" + stock.changePercent + "%)"
            holder.stockfullName!!.text = stock.stockName
            holder.stockID!!.setTextColor(Color.RED)
            holder.stockVal!!.setTextColor(Color.RED)
            holder.stockfullName!!.setTextColor(Color.RED)
        }
    }


    override fun getItemCount(): Int {
        return mystockList.size
    }
}
