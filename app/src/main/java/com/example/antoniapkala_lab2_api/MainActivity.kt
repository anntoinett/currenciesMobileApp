package com.example.antoniapkala_lab2_api

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    internal lateinit var currenciesButton: Button
    internal lateinit var goldButton: Button
    internal lateinit var converterButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Aplikacja walutowa")
        setContentView(R.layout.activity_main)

        currenciesButton = findViewById(R.id.convertButton)
        goldButton = findViewById(R.id.goldButton)
        converterButton = findViewById(R.id.converterButton)

        currenciesButton.setOnClickListener{_ -> showCurrencies()}
        goldButton.setOnClickListener{_ -> showGold()}
        converterButton.setOnClickListener{_ -> showConverter()}

        DataProvider.prepare(applicationContext)

    }

    private fun showCurrencies() {
        val intent = Intent(this, CurrenciesActivity::class.java)
        startActivity(intent)
    }

    private fun showGold() {
        val intent = Intent(this, GoldActivity::class.java).apply{
            putExtra("currencyCode", "złota")
        }
        startActivity(intent)
    }

    private fun showConverter() {
        val intent = Intent(this, ConverterActivity::class.java)
        startActivity(intent)
    }
}