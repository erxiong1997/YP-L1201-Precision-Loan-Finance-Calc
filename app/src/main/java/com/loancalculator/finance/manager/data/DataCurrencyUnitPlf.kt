package com.loancalculator.finance.manager.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataCurrencyUnitPlf(
    val currencyDrawable: Int,
    val currencyUnit: String,
    val currencyName: String,
    val currencySymbol: String,
) {
    var fingerSelect = false
}