package com.loancalculator.finance.manager

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.loancalculator.finance.manager.PlfDealApplication.Companion.mStartPageFinish
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.PlfMainToolActivity
import com.loancalculator.finance.manager.databinding.ActivityStartPlfBinding
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils

class StartPlfActivity : PlfBindingActivity<ActivityStartPlfBinding>() {
    private var mAgainStartLtd = "no"
    private var mBackGroundShow = false
    private var mBookShowing = false
    private var mAdType = ""

    init {
        mHandlerLtd = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    6381 -> {
                        startLtdActivity()
                    }

                    6382 -> {
                        if (!ParamsLtdUtils.mLtdInitUmp && mPlcBinding.progressStart.progress == 10) {
                            sendEmptyMessageDelayed(6382, 156)
                            return
                        }
                        if (mPlcBinding.progressStart.progress >= 100) {
                            mPlcBinding.progressStart.progress = 100
//                            sendEmptyMessage(6384)
                            sendEmptyMessage(6381)
                        } else {
//                            mPlcBinding.progressStart.progress += 1
//                            mPlcBinding.progressStart.progress += 5
                            mPlcBinding.progressStart.progress += 9
                            sendEmptyMessageDelayed(6382, 156)
                        }
                    }
                }
            }
        }
    }

    override fun beginViewAndDoLtd() {
        mPlcBinding.progressStart.max = 100
        mHandlerLtd?.sendEmptyMessageDelayed(6382, 220)
    }

    private fun startLtdActivity() {
        if (isFinishing || isDestroyed) return
        if (!PlfDealApplication.mAppLtdOpen) {
            mAgainStartLtd = "yes"
            return
        }
        if (mStartPageFinish) {
            mStartPageFinish = false
            finish()
            return
        }
        if (mAdType == "finish") {
            setResult(1212)
            finish()
            return
        }
//        if (DataManagerLtdUtils.getDataKeyPlf(
//                LTD_SHOW_HIDE_LANGUAGE_START, "showLanguage"
//            ) == "showLanguage"
//        ) {
//            if (!DataManagerLtdUtils.getDataKeyPlf(LTD_ENTER_MAIN_RESULT, false)) {
//                startActivity(Intent(this, LtdStartLanguageActivity::class.java))
//                finish()
//                return
//            }
//        }
        startActivity(Intent(this, PlfMainToolActivity::class.java).apply {

        })
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (mAgainStartLtd == "yes") {
            mAgainStartLtd = "no"
            startLtdActivity()
        } else {
            if (mBackGroundShow) {
                mBackGroundShow = false
                mBookShowing = false
//                mHandlerLtd?.sendEmptyMessage(6384)
                mHandlerLtd?.sendEmptyMessage(6381)
            }
        }
    }

    override fun getLayoutValue(): ActivityStartPlfBinding {
        return ActivityStartPlfBinding.inflate(layoutInflater)
    }
}