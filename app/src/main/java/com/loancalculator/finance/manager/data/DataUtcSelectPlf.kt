package com.loancalculator.finance.manager.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataUtcSelectPlf(val utcPlf: String, val utcOffsetValue: String) {
    @Json(ignore = true)
    var fingerSelect = false
    var mCurTime = ""
}