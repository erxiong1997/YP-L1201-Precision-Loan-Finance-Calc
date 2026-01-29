package com.loancalculator.finance.manager

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hjq.language.MultiLanguages
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.utils.LtdTotalUtils

class PlfDealApplication : Application(), Application.ActivityLifecycleCallbacks {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MultiLanguages.attach(newBase))
    }

    companion object {
        lateinit var mPlfContext: Context

        var mAppLtdOpen = false
        val mListActivityList = mutableListOf<Activity>()

        var mLtdClParams = false

        var mStartPageFinish = false

        var mLtdRootActivity: PlfRootActivity? = null
    }

    override fun onCreate() {
        super.onCreate()
        mPlfContext = this
        MultiLanguages.init(this)
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        if (activity is PlfRootActivity) {
            mLtdRootActivity = activity
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is PlfRootActivity) {
            mLtdRootActivity = activity
        }
        if (!mAppLtdOpen && !mLtdClParams && activity !is StartPlfActivity) {
            if (LtdTotalUtils.getLtdAppStatus()) {
                activity.startActivity(Intent(activity, StartPlfActivity::class.java).apply {
                    mStartPageFinish = true
                })
            }
        }
        mAppLtdOpen = true
        mListActivityList.add(activity)
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        mListActivityList.remove(activity)
        mAppLtdOpen = mListActivityList.isNotEmpty()
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}