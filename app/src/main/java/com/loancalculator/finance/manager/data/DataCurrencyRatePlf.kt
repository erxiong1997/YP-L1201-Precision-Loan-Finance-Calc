package com.loancalculator.finance.manager.data

import androidx.room.Ignore
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataCurrencyRatePlf(
    val amount: Int? = 1,
    val base: String? = null,
    val date: String? = null,
    val rates: Map<String, Double>? = null
) {
    @Ignore
    var rateUnit: String = ""

    @Ignore
    var rateValue = 0.0
}

//data class CurrencyRatesPlf()