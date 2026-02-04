@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.loancalculator.finance.manager

import android.content.Context
import android.content.res.Resources
import android.graphics.Outline
import android.util.Base64
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import kotlin.internal.InlineOnly

@InlineOnly
inline fun <reified T> T.plfPxDp(): T {
    val scale = Resources.getSystem().displayMetrics.density
    return when (T::class) {
        Float::class -> (scale * this as Float + 0.5f) as T
        Int::class -> (scale * this as Int + 0.5f).toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}

fun String.plfBaseResult64(): String {
    return String(Base64.decode(this, Base64.DEFAULT))
}

fun Context.showToastIDPlf(tipValue: Int) {
    Toast.makeText(this, tipValue, Toast.LENGTH_SHORT).show()
}

fun AppCompatDialog?.dismissGoPlf() {
    try {
        this?.dismiss()
    } catch (_: Exception) {

    }
}

fun ImageView.imageCenterCropPlf(radius: Float) {
    scaleType = ImageView.ScaleType.CENTER_CROP
    val radius = radius * resources.displayMetrics.density
    clipToOutline = true
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
}


fun View.setSafeListener(defaultLong: Long = 460, onSafeClick: (View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        val lastClickTime = (tag as? Long) ?: 0L
        if (currentTime - lastClickTime > defaultLong) {
            tag = currentTime
            onSafeClick(it)
        }
    }
}

fun Double.formatToFixString(count: Int = 2): String {
    val s = "%.${count}f".format(this)
    return s.trimEnd('0').trimEnd('.')   // 去掉尾部 0，再去掉可能剩的 .
}

