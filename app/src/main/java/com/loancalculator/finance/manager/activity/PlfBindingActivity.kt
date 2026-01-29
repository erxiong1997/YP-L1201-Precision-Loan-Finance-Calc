package com.loancalculator.finance.manager.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.statusBarHeight
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.ltdPxDp

/**
 * mBarType  hideAll hideBottom hideTop
 */
const val HIDE_ALL = "allHide"
const val HIDE_BOTTOM = "bottomHide"
const val HIDE_TOP = "topHide"

abstract class PlfBindingActivity<LL : ViewBinding>(
    private val mBarShow: String = HIDE_BOTTOM,
    private val mBarTextWhite: Boolean = true,
    private val mTopView: Boolean = true,
    private val mSetBack: Boolean = true
) : PlfRootActivity() {
    protected lateinit var mPlcBinding: LL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPlcBinding = getLayoutValue()
        setContentView(mPlcBinding.root)
        setBarShow()
        fillTopBar()

        onBackPressedDispatcher.addCallback(this, mOnBackPressedBack)
        if (mSetBack) {
            try {
                val backID = findViewById<ImageView>(R.id.ivBackAll)
                backID.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
            } catch (_: Exception) {
            }
        }
        beginViewAndDoLtd()
    }

    protected val mOnBackPressedBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            doBackPressed()
        }
    }

    protected open fun doBackPressed() {
        finish()
    }

    abstract fun beginViewAndDoLtd()

    open fun setLtdRecyclerView() {}

    private fun setBarShow() {
        val immersionBar = ImmersionBar.with(this)
        immersionBar.statusBarDarkFont(!mBarTextWhite)
        when (mBarShow) {
            HIDE_ALL -> {
                immersionBar.hideBar(BarHide.FLAG_HIDE_BAR)
            }

            HIDE_TOP -> {
                immersionBar.hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
            }

            HIDE_BOTTOM -> {
                immersionBar.hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
            }
        }
        immersionBar.init()
    }

    private fun fillTopBar() {
        if (mTopView) {
            try {
                findViewById<View>(R.id.topBarViewLtd)?.apply {
                    layoutParams.height = statusBarHeight
                }
            } catch (_: Exception) {
                findViewById<View>(R.id.topBarViewLtd)?.apply {
                    layoutParams.height = 46.ltdPxDp()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun getLayoutValue(): LL
}