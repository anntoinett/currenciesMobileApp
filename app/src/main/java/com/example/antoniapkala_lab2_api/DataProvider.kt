package com.example.antoniapkala_lab2_api

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley.newRequestQueue
import com.blongho.country_data.Country
import com.blongho.country_data.World

object DataProvider {
    lateinit var queue: RequestQueue
    private lateinit var countries: List<Country>

    fun prepare(context: Context){
        queue = newRequestQueue(context)
        World.init(context)
        countries = World.getAllCountries().distinctBy {it.currency.code}
    }
    fun getFlagForCurrency(currencyCode: String): Int{
        if(currencyCode == "USD"){
            return R.drawable.us
        }
        else if(currencyCode == "GBP"){
            return R.drawable.gb
        }
        else if(currencyCode == "CHF"){
            return R.drawable.ch
        }
        else if(currencyCode == "HKD"){
            return R.drawable.hk
        }
        else if(currencyCode == "IDR"){
            return R.drawable.id
        }
        else if(currencyCode == "EUR"){
            return R.drawable.eu
        }
        else {
            return countries.find { it.currency.code == currencyCode }?.flagResource
                ?: World.getWorldFlag()
        }
    }
}