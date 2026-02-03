package com.loancalculator.finance.manager.utils.dialog

import android.content.res.Resources
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.loancalculator.finance.manager.plfPxDp

object DialogMangerLtdUtils {
    val mWidthValue by lazy {
        Resources.getSystem().displayMetrics.widthPixels
    }

    fun changeWindowFull(window: Window?) {
        window?.let {
            it.decorView.background = null
            it.decorView.setPadding(0, 0, 0, 0)
            val attributes = it.attributes
            attributes.gravity = Gravity.CENTER
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            attributes.height = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes = attributes
        }
    }

    fun changeWindowHalf(
        window: Window?,
        widthValue: Float = 0.86f,
        gravity: Int = Gravity.CENTER
    ) {
        window?.let {
            it.decorView.background = null
            it.decorView.setPadding(0, 0, 0, 0)
            val attributes = it.attributes
            attributes.gravity = gravity
            attributes.width = (mWidthValue * widthValue).toInt()
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes.y = (-28).plfPxDp()
            it.attributes = attributes
        }
    }
}