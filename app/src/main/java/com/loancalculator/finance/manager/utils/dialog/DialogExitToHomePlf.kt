package com.loancalculator.finance.manager.utils.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatDialog
import com.loancalculator.finance.manager.databinding.DialogDeleteConfirmPlfBinding
import com.loancalculator.finance.manager.databinding.DialogExitToHomePlfBinding
import com.loancalculator.finance.manager.dismissGoPlf

class DialogExitToHomePlf(
    private val mContext: Context,
    private val mDeleteBody: String? = null,
    private val tilFunBack: () -> Unit
) : AppCompatDialog(mContext) {
    private lateinit var mPlcBinding: DialogExitToHomePlfBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPlcBinding = DialogExitToHomePlfBinding.inflate(layoutInflater)
        setContentView(mPlcBinding.root)
        setCancelable(true)

        mDeleteBody?.let { mPlcBinding.tvDeleteContent.text = it }
        mPlcBinding.tvCancel.setOnClickListener { dismissGoPlf() }
        mPlcBinding.tvYes.setOnClickListener {
            dismissGoPlf()
            tilFunBack()
        }

        DialogMangerPlfUtils.changeWindowHalf(window, gravity = Gravity.CENTER)
    }
}