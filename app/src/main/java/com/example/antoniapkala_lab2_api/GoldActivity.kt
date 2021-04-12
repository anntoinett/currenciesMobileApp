package com.example.antoniapkala_lab2_api

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import com.github.mikephil.charting.charts.LineChart
import org.json.JSONArray

class GoldActivity : HistoricRatesActivity(){

    override lateinit var todayRate: TextView
    override lateinit var lineChartM: LineChart
    override lateinit var lineChartW: LineChart
    override lateinit var data: Array<Pair<String,Double>>
    override lateinit var currencyCode: String
    override lateinit var progress: ProgressBar

    override fun getHistoricRates(){
        val queue = newRequestQueue(this)
        val url = "http://api.nbp.pl/api/cenyzlota/last/30/?format=json"

        val historicGoldRequest = JsonArrayRequest(
                Request.Method.GET,url, null,
                Response.Listener { response ->
                    loadData(response)
                    showData()
                    lineChartW.visibility = View.GONE
                    yesterdayRate.visibility = View.GONE
                },
                Response.ErrorListener { response ->
                    showError()
                })

        queue.add(historicGoldRequest)
    }


    private fun loadData(response: JSONArray?) {
        response?.let{
            val rates = response
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<Pair<String,Double>>(ratesCount)
            for(i in 0 until ratesCount){
                val date = rates.getJSONObject(i).getString("data")
                val rate = rates.getJSONObject(i).getDouble("cena")
                tmpData[i] = Pair(date,rate)
            }
            this.data = tmpData as Array<Pair<String,Double>>
        }
        progress.visibility = View.GONE
    }

}