package com.loancalculator.finance.manager

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hjq.language.MultiLanguages
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.room.mPlfLoanRoom
import com.loancalculator.finance.manager.utils.PlfTotalUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class PlfDealApplication : Application(), Application.ActivityLifecycleCallbacks {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MultiLanguages.attach(newBase))
    }

    companion object {
        lateinit var mPlfContext: Context

        var mAppPlfOpen = false
        val mListActivityList = mutableListOf<Activity>()

        var mPlfClParams = false

        var mStartPageFinish = false

        var mPlfRootActivity: PlfRootActivity? = null

        val mDirTableFile by lazy {
            val dir = File(mPlfContext.filesDir, "shareFile")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            dir
        }
    }

    override fun onCreate() {
        super.onCreate()
        mPlfContext = this
        MultiLanguages.init(this)
        registerActivityLifecycleCallbacks(this)
        mPlfLoanRoom
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                mDirTableFile.walk().filter {
                    it.isFile
                }.forEach {
                    if (it.exists()) {
                        try {
                            it.delete()
                        } catch (_: Exception) {
                        }
                    }
                }
            }
        }
    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        if (activity is PlfRootActivity) {
            mPlfRootActivity = activity
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is PlfRootActivity) {
            mPlfRootActivity = activity
        }
        if (!mAppPlfOpen && !mPlfClParams && activity !is StartPlfActivity) {
            if (PlfTotalUtils.getPlfAppStatus()) {
                activity.startActivity(Intent(activity, StartPlfActivity::class.java).apply {
                    mStartPageFinish = true
                })
            }
        }
        mAppPlfOpen = true
        mListActivityList.add(activity)
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        mListActivityList.remove(activity)
        mAppPlfOpen = mListActivityList.isNotEmpty()
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}