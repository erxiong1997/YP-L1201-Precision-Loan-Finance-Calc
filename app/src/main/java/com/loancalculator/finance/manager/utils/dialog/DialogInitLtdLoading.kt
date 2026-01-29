package com.loancalculator.finance.manager.utils.dialog

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.loancalculator.finance.manager.databinding.DialogLtdLoadingBinding

class DialogInitLtdLoading(
    private val mContext: Context, private val mContentText: Int? = null
) : AppCompatDialog(mContext) {
    private lateinit var mPlcBinding: DialogLtdLoadingBinding
    private var mAnimatorLtd: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPlcBinding = DialogLtdLoadingBinding.inflate(layoutInflater)
        setContentView(mPlcBinding.root)

        setCancelable(false)

        mPlcBinding.ivNotice1.let {
            mAnimatorLtd = ObjectAnimator.ofFloat(it, "rotation", 0f, 360f).apply {
                duration = 1200
                repeatCount = -1
                start()
            }
        }

        mContentText?.let {
            mPlcBinding.tvLoadingContent.text = mContext.getString(it)
        }

        DialogMangerLtdUtils.changeWindowFull(window)
    }

    override fun onStop() {
        super.onStop()
        mAnimatorLtd?.cancel()
    }
}