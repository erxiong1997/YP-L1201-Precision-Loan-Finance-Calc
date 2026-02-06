package com.loancalculator.finance.manager.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.plfPxDp
import kotlin.math.min

/**
 * 5段彩色圆环（中间空心）
 * 每段长度按传入的 values 比例自动分配
 */
class MultiColorRingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 5段颜色（可自定义）
    private val mPaintColors = intArrayOf(
        ContextCompat.getColor(context, R.color.plf_0ACE86),
        ContextCompat.getColor(context, R.color.plf_FF9E4C),
        ContextCompat.getColor(context, R.color.plf_8775FA),
        ContextCompat.getColor(context, R.color.plf_378EFF),
        ContextCompat.getColor(context, R.color.plf_FD4E54),
    )

    // 起始角度（0° 在右边，顺时针）
    var mStartAngle = -90f  // 默认从顶部开始

    // 传入的5个值（可以是金额、占比、分数等）
    private var values = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0)

    // 画笔
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
//        strokeCap = Paint.Cap.ROUND  // 圆角端点，更美观
    }

    // 圆环的绘制范围
    private val oval = RectF()

    /**
     * 设置5段的数据值
     * @param newValues 长度必须为5的数组
     */
    fun setValues(newValues: DoubleArray) {
        values = newValues.copyOf()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val size = min(w, h).toFloat()
        val padding = 20f.plfPxDp() / 2f + 10f  // 留点边距防裁切
        oval.set(padding, padding, size - padding, size - padding)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val total = values.sum().takeIf { it > 0f } ?: 1f  // 防止全0
        var currentAngle = mStartAngle

        mPaint.strokeWidth = 20f.plfPxDp()

        values.forEachIndexed { index, value ->
            val sweepAngle = (value.toFloat() / total.toFloat()) * 360f
            if (sweepAngle > 0f) {
                mPaint.color = mPaintColors[index % mPaintColors.size]
                canvas.drawArc(oval, currentAngle, sweepAngle, false, mPaint)
            }
            currentAngle += sweepAngle
        }
    }
}