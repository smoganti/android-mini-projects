package com.example.stockwatch.stockwatch


import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var stockID: TextView? = null
    var stockfullName: TextView? = null
    var stockVal: TextView? = null

    init {
        stockID = view.findViewById(R.id.stockID) as TextView
        stockfullName = view.findViewById(R.id.stockfullName) as TextView
        stockVal = view.findViewById(R.id.stockVal) as TextView
    }
}
