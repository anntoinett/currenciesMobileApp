package com.example.antoniapkala_lab2_api

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class ConverterActivity: AppCompatActivity() {
    internal lateinit var amount: EditText
    internal lateinit var fromCurrency: Spinner
    internal lateinit var toCurrency: Spinner
    internal lateinit var convertButton: Button
    internal lateinit var errorMess: TextView
    internal lateinit var converted: TextView

    private var ratesProvider = RatesProvider()
    internal var data = arrayOf<CurrencyDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.converter_title)
        setContentView(R.layout.activity_converter)

        amount = findViewById(R.id.amountInput)
        fromCurrency = findViewById(R.id.spinner_from)
        toCurrency = findViewById(R.id.spinner_to)
        convertButton = findViewById(R.id.convertButton)
        errorMess = findViewById(R.id.errorMess)
        converted = findViewById(R.id.convertedAmount)

        errorMess.visibility = View.GONE
        converted.visibility = View.GONE

        convertButton.setOnClickListener{ _ -> convert() }

        ratesProvider.onDataLoad = {
            showList()
        }

        ratesProvider.onLoadError = {
            showError()
        }

        ratesProvider.makeRequest()
    }

    private fun showError() {
        errorMess.visibility = View.VISIBLE
        amount.visibility = View.GONE
        fromCurrency.visibility = View.GONE
        toCurrency.visibility = View.GONE
        convertButton.visibility = View.GONE
        errorMess.setText(R.string.downloadingError)
        errorMess.visibility = View.VISIBLE    }

    private fun showList() {
        data = data.plusElement(CurrencyDetails("Wybierz walutę", 0.0)).plusElement(CurrencyDetails("PLN", 1.0))
        loadListData()
    }

    private fun loadListData() {
        data = data.plus(ratesProvider.rates)
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data.map{currency -> currency.currencyCode}.reversed())
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        fromCurrency.setAdapter(spinnerArrayAdapter)
        toCurrency.setAdapter(spinnerArrayAdapter)

        fromCurrency.setSelection(data.size-1);
        toCurrency.setSelection(data.size-1);
    }


    private fun convert() {
        var currentView: View = findViewById(android.R.id.content)
        currentView.hideKeyboard()
        var chooseText = this.resources.getString(R.string.chooseCurrency)

        if(!amount.text.isEmpty()
                && !fromCurrency.selectedItem.toString().equals(chooseText)
                && !toCurrency.selectedItem.toString().equals(chooseText) ) {

            val num = amount.text.toString().toDouble()
            val fromCurr = data.find { it.currencyCode == fromCurrency.selectedItem.toString() } ?: CurrencyDetails("NULL", 0.0)
            val toCurr = data.find { it.currencyCode == toCurrency.selectedItem.toString() } ?: CurrencyDetails("NULL", 0.0)
            converted.text = String.format("%.6f", (num * fromCurr.rate / toCurr.rate))
        }
        else{
            converted.text = this.resources.getString(R.string.notEnoughParameters)
        }
        if (converted.visibility != View.VISIBLE) converted.visibility = View.VISIBLE
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}