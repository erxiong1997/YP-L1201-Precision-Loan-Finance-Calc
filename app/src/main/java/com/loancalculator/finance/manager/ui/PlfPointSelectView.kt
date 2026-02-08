package com.loancalculator.finance.manager.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.plfPxDp

class PlfPointSelectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    //选中
    private val mPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.plf_0ACE86)
        }
    }
    private val mPaint2 by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.plf_BCBCBD)
        }
    }
    private var mSelectPosition = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val space4 = 4f.plfPxDp()


        canvas.drawCircle(
            space4, height / 2f, space4, if (mSelectPosition == 0) {
                mPaint
            } else {
                mPaint2
            }
        )
        canvas.drawCircle(
            4 * space4, height / 2f, space4, if (mSelectPosition == 1) {
                mPaint
            } else {
                mPaint2
            }
        )
        canvas.drawCircle(
            7 * space4, height / 2f, space4, if (mSelectPosition == 2) {
                mPaint
            } else {
                mPaint2
            }
        )
        canvas.drawCircle(
            10 * space4, height / 2f, space4, if (mSelectPosition == 3) {
                mPaint
            } else {
                mPaint2
            }
        )
    }

    fun changePlfPosition(position: Int) {
        mSelectPosition = position
        invalidate()
    }
}