package com.loancalculator.finance.manager.utils.dialog

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.loancalculator.finance.manager.databinding.DialogPlfInitLoadingBinding

class DialogInitPlfLoading(
    private val mContext: Context, private val mContentText: Int? = null
) : AppCompatDialog(mContext) {
    private lateinit var mPlcBinding: DialogPlfInitLoadingBinding
    private var mAnimatorPlf: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPlcBinding = DialogPlfInitLoadingBinding.inflate(layoutInflater)
        setContentView(mPlcBinding.root)

        setCancelable(false)

        mPlcBinding.ivNotice1.let {
            mAnimatorPlf = ObjectAnimator.ofFloat(it, "rotation", 0f, 360f).apply {
                duration = 1200
                repeatCount = -1
                start()
            }
        }

        mContentText?.let {
            mPlcBinding.tvLoadingContent.text = mContext.getString(it)
        }

        DialogMangerPlfUtils.changeWindowFull(window)
    }

    override fun onStop() {
        super.onStop()
        mAnimatorPlf?.cancel()
    }
}