package com.example.stockwatch.stockwatch

/**
 * Created by vamse on 3/9/2017.
 */

class Stock : Comparable<Stock> {
    var stocksymbol: String
    var stockName: String
    lateinit var price: String
    lateinit var priceChange: String
    lateinit var changePercent: String

    constructor(stockid: String, stockName: String, price: String, priceChange: String, changePercent: String) {
        this.stocksymbol = stockid
        this.stockName = stockName
        this.price = price
        this.priceChange = priceChange
        this.changePercent = changePercent

    }

    constructor(stockid: String, stockName: String) {
        this.stocksymbol = stockid
        this.stockName = stockName
    }


    override fun compareTo(s: Stock): Int {
        if (this == s) {
            return 0
        }
        val result = stocksymbol.compareTo(s.stocksymbol)
        return if (result > 0)
            1
        else
            -1
    }

    override fun toString(): String {
        return "$stocksymbol ($price$priceChange$changePercent), $stockName"
    }
}
