package com.loancalculator.finance.manager.utils

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.utils.value.ConstantOftenPlf.PLF_APP_FROM_VALUE
import com.loancalculator.finance.manager.utils.value.ConstantOftenPlf.PLF_AY_PERIOD_VALUE
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PlfTotalUtils {
    fun getClipTextPlf(board: ClipboardManager): String? {
        return try {
            val clip = board.primaryClip ?: return null
            if (clip.itemCount > 0) {
                clip.getItemAt(0).text.toString()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    fun getPlfFullDate(long: Long): String {
        val sim = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault())
        return sim.format(Date(long))
    }

    fun getPlfAppStatus(): Boolean {
        return DataManagerPlfUtils.getDataKeyPlf(PLF_AY_PERIOD_VALUE, "plfValue") == "plfValue"
                && DataManagerPlfUtils.getDataKeyPlf(PLF_APP_FROM_VALUE, "") == "plfValue"
    }

    fun shareAppLinkPlf(context: Context) {
        val packageName = context.packageName
        val playStoreUrl =
            "https://play.google.com/store/apps/details?id=$packageName"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, playStoreUrl)
        }

        context.startActivity(
            Intent.createChooser(intent, context.getString(R.string.plf_share))
        )
    }

}