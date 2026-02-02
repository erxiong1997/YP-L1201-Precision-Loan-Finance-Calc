package com.loancalculator.finance.manager.utils

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.utils.value.ConstantOftenLtd.LTD_APP_FROM_VALUE
import com.loancalculator.finance.manager.utils.value.ConstantOftenLtd.LTD_AY_PERIOD_VALUE
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PlfTotalUtils {
    fun playVideoLtd(context: Context, file: File?) {
        if (file == null) return
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(
            uri, when (file.extension) {
                "mp4", "MP4" -> {
                    "video/*"
                }

                "mp3", "MP3" -> {
                    "audio/*"
                }

                "jpg", "tiff", "jpeg", "webp", "png", "gif" -> {
                    "image/*"
                }

                else -> {
                    "video/*"
                }
            }
        )
        context.startActivity(intent)
    }

    fun fileNameLtd(fileName: String): Boolean {
        return !fileName.contains("/") && !fileName.contains("\u0000")
    }

    fun fileNameLtd2(fileName: String): Boolean {
        val illegalRegex = Regex("[\\\\/:*?\"<>|]")
        return !illegalRegex.containsMatchIn(fileName)
    }

    fun getClipTextLtd(board: ClipboardManager): String? {
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

    fun getLtdFullDate(long: Long): String {
        val sim = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault())
        return sim.format(Date(long))
    }

    fun getLtdAppStatus(): Boolean {
        return DataManagerLtdUtils.getDataKeyPlf(LTD_AY_PERIOD_VALUE, "ltdValue") == "ltdValue"
                && DataManagerLtdUtils.getDataKeyPlf(LTD_APP_FROM_VALUE, "") == "ltdValue"
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