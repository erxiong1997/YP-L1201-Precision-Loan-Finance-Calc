package com.loancalculator.finance.manager.activity

import android.content.Context
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hjq.language.MultiLanguages

/**
 * 热启动
 * 下载有时候会很久没反应
 * 控制参数
 * fb+x+pinterest
 */
abstract class PlfRootActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MultiLanguages.attach(newBase))
    }

    protected var mHandlerLtd: Handler? = null

    //归因
    protected var mStatusLook = false

    //是否开始加载native
    protected var mStarNativeValue = false

    //是否暂停
    protected var mPagePause = false

    //======================================================================

    override fun onDestroy() {
        super.onDestroy()
        mHandlerLtd?.removeCallbacksAndMessages(null)
        mHandlerLtd = null
    }
}