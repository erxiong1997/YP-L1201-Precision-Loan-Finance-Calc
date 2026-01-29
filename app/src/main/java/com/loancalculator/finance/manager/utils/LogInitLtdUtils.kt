package com.loancalculator.finance.manager.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object LogInitLtdUtils {
    fun clueLtdValue(context: Context, value: String) {
        val bundle = Bundle()
        bundle.putString("notifyCountRe", value)
        FirebaseAnalytics.getInstance(context).logEvent("notifyCountReTil", bundle)
    }

    fun clueCompleteValue(context: Context, value: String) {
        val bundle = Bundle()
        bundle.putString("notifyComplete", value)
        FirebaseAnalytics.getInstance(context).logEvent("notifyCompleteTil", bundle)
    }
}