package com.example.antoniapkala_lab2_api

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray

class RatesProvider() {
    private lateinit var tmp_rates: Array<CurrencyDetails>
    var rates = arrayOf<CurrencyDetails>()
    var onDataLoad: (()->Unit)? = null
    var onLoadError: (()->Unit)? = null

    fun makeRequest(){
        val queue = DataProvider.queue
        val urlA = "http://api.nbp.pl/api/exchangerates/tables/A/last/2?format=json"
        val currencyListARequest = JsonArrayRequest(Request.Method.GET,urlA, null,
                Response.Listener { response ->
                    loadData(response)
                    if(rates.size > 0) rates = rates.plus(tmp_rates)
                    else rates = tmp_rates
                },
                Response.ErrorListener { _ ->
                    onLoadError?.invoke()
                })

        val urlB = "http://api.nbp.pl/api/exchangerates/tables/B/last/2?format=json"
        val currencyListBRequest = JsonArrayRequest(Request.Method.GET,urlB, null,
                Response.Listener { response ->
                    loadData(response)
                    if(rates.size > 0) rates = rates.plus(tmp_rates)
                    else rates = tmp_rates
                    onDataLoad?.invoke()
                },
                Response.ErrorListener { _ ->
                    onLoadError?.invoke()
                })

        queue.add(currencyListARequest)
        queue.add(currencyListBRequest)
    }

    private fun loadData(response: JSONArray?) {
        response?.let{
            val ratesToday = response.getJSONObject(1).getJSONArray("rates")
            val ratesYesterday = response.getJSONObject(0).getJSONArray("rates")
            val ratesCount = ratesToday.length()
            val tmpData = arrayOfNulls<CurrencyDetails>(ratesCount)
            val table = response.getJSONObject(1).getString("table")
            var increase: Boolean? = null
            for(i in 0 until ratesCount){
                val currencyCode = ratesToday.getJSONObject(i).getString("code")
                val currencyRate = ratesToday.getJSONObject(i).getDouble("mid")
                val yesterdayRate = ratesYesterday.getJSONObject(i).getDouble("mid")
                val flagID = DataProvider.getFlagForCurrency(currencyCode)
                if(currencyRate > yesterdayRate) increase = true
                else if(currencyRate < yesterdayRate) increase = false

                val currencyObject = CurrencyDetails(currencyCode, currencyRate, flagID, table, increase)
                tmpData[i] = currencyObject
            }
            tmp_rates = tmpData as Array<CurrencyDetails>
        }
    }
}