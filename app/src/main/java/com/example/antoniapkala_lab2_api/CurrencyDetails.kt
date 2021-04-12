package com.example.antoniapkala_lab2_api

data class CurrencyDetails(var currencyCode:String, var rate:Double, var flag:Int = 0, var table:String = "A", var increase: Boolean? = null) {
}