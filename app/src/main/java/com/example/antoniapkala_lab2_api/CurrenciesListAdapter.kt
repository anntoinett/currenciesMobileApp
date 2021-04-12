package com.example.antoniapkala_lab2_api

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest


class CurrenciesListAdapter(var dataSet:Array<CurrencyDetails>, val context: Context) : RecyclerView.Adapter<CurrenciesListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val currencyCodeTextView: TextView
        val rateTextView: TextView
        val flagView: ImageView
        val arrowView: ImageView

        init {
            currencyCodeTextView = view.findViewById(R.id.currencyCodeTextView)
            rateTextView = view.findViewById(R.id.currencyRateTextView)
            flagView = view.findViewById(R.id.flagView)
            arrowView = view.findViewById(R.id.arrowView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.currency_row, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currency = dataSet[position]
        viewHolder.currencyCodeTextView.text = currency.currencyCode
        viewHolder.flagView.setImageResource(currency.flag)

        if(currency.increase == true) viewHolder.arrowView.setImageResource(R.drawable.green_arrow)
        else if(currency.increase == false) viewHolder.arrowView.setImageResource(R.drawable.red_arrow)
        else viewHolder.arrowView.visibility = View.GONE


        viewHolder.rateTextView.text = String.format("%.4f", currency.rate)
        viewHolder.itemView.setOnClickListener{ goToDetails(currency.currencyCode, currency.table) }
    }

    private fun goToDetails(currencyCode: String, currencyTable:String){
        val intent = Intent(context, HistoricRatesActivity::class.java).apply{
            putExtra("currencyCode", currencyCode)
            putExtra("currencyTable", currencyTable)
        }
        context.startActivity(intent)
    }

    override fun getItemCount() = dataSet.size
}
