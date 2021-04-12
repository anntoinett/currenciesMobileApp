package com.example.antoniapkala_lab2_api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONObject

open class HistoricRatesActivity : AppCompatActivity() {

    open internal lateinit var todayRate: TextView
    open internal lateinit var yesterdayRate: TextView
    open internal lateinit var lineChartW: LineChart
    open internal lateinit var lineChartM: LineChart
    open internal lateinit var currencyCode: String
    internal lateinit var currencyTable: String
    open internal lateinit var data: Array<Pair<String,Double>>
    open internal lateinit var progress: ProgressBar
    open internal lateinit var errorMess: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.historicRatesTitle)
        setContentView(R.layout.activity_historic_rates)

        progress = findViewById(R.id.progressBar2)
        todayRate = findViewById(R.id.convertedAmount)
        yesterdayRate = findViewById(R.id.yesterdayRate)
        lineChartM = findViewById(R.id.monthlyRates)
        lineChartW = findViewById(R.id.weeklyRates)
        errorMess = findViewById(R.id.error)

        currencyCode = intent.getStringExtra("currencyCode") ?: "USD"
        currencyTable = intent.getStringExtra("currencyTable") ?: "A"

        todayRate.visibility = View.GONE
        yesterdayRate.visibility = View.GONE
        lineChartW.visibility = View.GONE
        lineChartM.visibility = View.GONE
        errorMess.visibility = View.GONE


        getHistoricRates()
    }

    open fun getHistoricRates(){
        val queue = DataProvider.queue
        val url_month = "http://api.nbp.pl/api/exchangerates/rates/%s/%s/last/30/".format(currencyTable, currencyCode)

        val monthRatesRequest = JsonObjectRequest(
            Request.Method.GET,url_month, null,
            Response.Listener { response ->
                loadData(response)
                showData()
            },
            Response.ErrorListener { _ ->
                showError()
            })

        queue.add(monthRatesRequest)
    }

    fun showData() {

        todayRate.text = getString(R.string.todayRateText,data.last().second)
        yesterdayRate.text = getString(R.string.yesterdayRateText,data[data.size-2].second)

        var entriesW = ArrayList<Entry>()
        var entriesM = ArrayList<Entry>()

        for ((index,element) in data.withIndex()){
            entriesM.add(Entry(index.toFloat(), element.second.toFloat()))
            if(index >= data.size -7) entriesW.add(Entry(index.toFloat(), element.second.toFloat()))
        }

        var lineDataSetW = LineDataSet(entriesW, "Kurs")
        var lineDataSetM = LineDataSet(entriesM, "Kurs")


        val goldColor = ContextCompat.getColor(this, R.color.gold);
        val goldDarkColor = ContextCompat.getColor(this, R.color.goldDark);
        val blackColor = ContextCompat.getColor(this, R.color.black);

        lineDataSetM.setColor(goldColor)
        lineDataSetM.setCircleColor(blackColor);
        lineDataSetM.setCircleHoleColor(goldColor)

        lineDataSetW.setColor(goldDarkColor)
        lineDataSetW.setCircleColor(blackColor);
        lineDataSetW.setCircleHoleColor(goldDarkColor)

        var lineDataW = LineData(lineDataSetW)
        var lineDataM = LineData(lineDataSetM)

        lineChartW.data = lineDataW
        lineChartM.data = lineDataM


        val descriptionW = Description()
        val descriptionM = Description()

        descriptionW.text = "Kurs %s z ostatnich 7 notowań".format(currencyCode)
        descriptionM.text = "Kurs %s z ostatnich 30 notowań".format(currencyCode)

        lineChartW.description = descriptionW
        lineChartM.description = descriptionM


        lineChartW.xAxis.valueFormatter = IndexAxisValueFormatter(data.map {it.first }.toTypedArray())
        lineChartW.invalidate()

        lineChartM.xAxis.valueFormatter = IndexAxisValueFormatter(data.map {it.first }.toTypedArray())
        lineChartM.invalidate()

        progress.visibility = View.GONE
        todayRate.visibility = View.VISIBLE
        yesterdayRate.visibility = View.VISIBLE
        lineChartW.visibility = View.VISIBLE
        lineChartM.visibility = View.VISIBLE
    }

    private fun loadData(response: JSONObject?) {

        response?.let{
            val rates = response.getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<Pair<String,Double>>(ratesCount)
            for(i in 0 until ratesCount){
                val date = rates.getJSONObject(i).getString("effectiveDate")
                val rate = rates.getJSONObject(i).getDouble("mid")

                tmpData[i] = Pair(date,rate)
            }
            this.data = tmpData as Array<Pair<String,Double>>
        }
    }

    fun showError() {
        todayRate.visibility = View.VISIBLE
        yesterdayRate.visibility = View.GONE
        progress.visibility = View.GONE
        lineChartM.visibility = View.GONE
        lineChartW.visibility = View.GONE
        errorMess.setText(R.string.downloadingError)
        errorMess.visibility = View.VISIBLE    }
}