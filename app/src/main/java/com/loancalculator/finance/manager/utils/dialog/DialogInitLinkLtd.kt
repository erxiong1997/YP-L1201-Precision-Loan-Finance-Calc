package com.loancalculator.finance.manager.utils.dialog

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.databinding.DialogInitLinkViewBinding
import com.loancalculator.finance.manager.dismissGoLtd
import com.loancalculator.finance.manager.utils.LtdTotalUtils

class DialogInitLinkLtd(
    private val mContext: Context,
) : AppCompatDialog(mContext) {
    private lateinit var mPlcBinding: DialogInitLinkViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPlcBinding = DialogInitLinkViewBinding.inflate(layoutInflater)
        setContentView(mPlcBinding.root)
        setCancelable(false)
        
        mPlcBinding.tvGoIns.text = mContext.getString(R.string.ltd_go_instagram)
        mPlcBinding.tvGoPin.text = mContext.getString(R.string.ltd_go_pinterest)

        mPlcBinding.ivCloseDialog.setOnClickListener { dismissGoLtd() }
        mPlcBinding.tvGoIns.setOnClickListener {
            dismissGoLtd()
            LtdTotalUtils.openAppBrowserLtd(mContext, "ltdIns")
        }
        mPlcBinding.tvGoPin.setOnClickListener {
            dismissGoLtd()
            LtdTotalUtils.openAppBrowserLtd(mContext, "ltdPin")
        }
        DialogMangerLtdUtils.changeWindowHalf(window)
    }

}