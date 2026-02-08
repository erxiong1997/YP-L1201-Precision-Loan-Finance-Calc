package com.loancalculator.finance.manager

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.loancalculator.finance.manager.PlfDealApplication.Companion.mStartPageFinish
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.PlfMainToolActivity
import com.loancalculator.finance.manager.activity.set.PlfStartLanguageActivity
import com.loancalculator.finance.manager.databinding.ActivityStartPlfBinding
import com.loancalculator.finance.manager.utils.DataManagerPlfUtils
import com.loancalculator.finance.manager.utils.value.ConstantNextPlf.PLF_ENTER_MAIN_RESULT

class StartPlfActivity : PlfBindingActivity<ActivityStartPlfBinding>() {
    private var mAgainStartPlf = "no"
    private var mBackGroundShow = false
    private var mBookShowing = false
    private var mAdType = ""

    init {
        mHandlerPlf = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    6381 -> {
                        startPlfActivity()
                    }

                    6382 -> {
//                        if (!ParamsPlfUtils.mPlfInitUmp && mPlcBinding.progressStart.progress == 10) {
//                            sendEmptyMessageDelayed(6382, 156)
//                            return
//                        }
                        if (mPlcBinding.progressStart.progress >= 100) {
                            mPlcBinding.progressStart.progress = 100
//                            sendEmptyMessage(6384)
                            sendEmptyMessage(6381)
                        } else {
//                            mPlcBinding.progressStart.progress += 1
//                            mPlcBinding.progressStart.progress += 5
                            mPlcBinding.progressStart.progress += 19
                            sendEmptyMessageDelayed(6382, 156)
                        }
                    }
                }
            }
        }
    }

    override fun beginViewAndDoPlf() {
        mPlcBinding.progressStart.max = 100
        mHandlerPlf?.sendEmptyMessageDelayed(6382, 220)
    }

    private fun startPlfActivity() {
        if (isFinishing || isDestroyed) return
        if (!PlfDealApplication.mAppPlfOpen) {
            mAgainStartPlf = "yes"
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
//        if (DataManagerPlfUtils.getDataKeyPlf(
//                PLF_SHOW_HIDE_LANGUAGE_START, "showLanguage"
//            ) == "showLanguage"
//        ) {
        if (DataManagerPlfUtils.getDataKeyPlf(PLF_ENTER_MAIN_RESULT, "") != "home") {
            startActivity(Intent(this, PlfStartLanguageActivity::class.java))
            finish()
            return
        }
//        }
        startActivity(Intent(this, PlfMainToolActivity::class.java).apply {

        })
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (mAgainStartPlf == "yes") {
            mAgainStartPlf = "no"
            startPlfActivity()
        } else {
            if (mBackGroundShow) {
                mBackGroundShow = false
                mBookShowing = false
//                mHandlerPlf?.sendEmptyMessage(6384)
                mHandlerPlf?.sendEmptyMessage(6381)
            }
        }
    }

    override fun getLayoutValue(): ActivityStartPlfBinding {
        return ActivityStartPlfBinding.inflate(layoutInflater)
    }
}