package com.example.antoniapkala_lab2_api

import android.app.VoiceInteractor
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import org.json.JSONArray
import java.security.AccessController.getContext

class CurrenciesActivity : AppCompatActivity() {
    internal lateinit var adapter: CurrenciesListAdapter
    internal lateinit var currenciesListRecyclerView: RecyclerView
    internal lateinit var progress: ProgressBar
    internal lateinit var errorInfo: TextView
    private var ratesProvider = RatesProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.currencies_title)
        setContentView(R.layout.activity_currencies)
        currenciesListRecyclerView = findViewById(R.id.currenciesListRecyclerView)
        progress = findViewById(R.id.progressBar)
        errorInfo = findViewById(R.id.errorInfo)

        currenciesListRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val tmpData = arrayOf(CurrencyDetails("EUR", 4.56), CurrencyDetails("USD", 3.80))
        adapter = CurrenciesListAdapter(tmpData, this)
        currenciesListRecyclerView.adapter = adapter

        currenciesListRecyclerView.visibility = View.GONE
        errorInfo.visibility = View.GONE
        progress.visibility = View.VISIBLE


        ratesProvider.onDataLoad = {
            showList()
        }

        ratesProvider.onLoadError = {
            showError()
        }

        ratesProvider.makeRequest()
    }

    private fun showError() {
        progress.visibility = View.GONE
        adapter.dataSet = emptyArray()
        adapter.notifyDataSetChanged()
        errorInfo.setText(R.string.downloadingError)
        errorInfo.visibility = View.VISIBLE    }

    private fun showList() {
        progress.visibility = View.GONE
        adapter.dataSet = ratesProvider.rates
        adapter.notifyDataSetChanged()
        currenciesListRecyclerView.visibility = View.VISIBLE    }
}