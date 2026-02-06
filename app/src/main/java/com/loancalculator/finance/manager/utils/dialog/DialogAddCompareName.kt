package com.loancalculator.finance.manager.utils.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import androidx.core.widget.doAfterTextChanged
import com.loancalculator.finance.manager.databinding.DialogAddCompareNameBinding

class DialogAddCompareName(
    private val mContext: Context,
    private val tilFunBack: (String) -> Unit
) : AppCompatDialog(mContext) {
    private lateinit var mPlcBinding: DialogAddCompareNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPlcBinding = DialogAddCompareNameBinding.inflate(layoutInflater)
        setContentView(mPlcBinding.root)
        setCancelable(false)

        mPlcBinding.tvCancel.setOnClickListener { dismiss() }
        mPlcBinding.etSetName.doAfterTextChanged {
            if (!it.isNullOrEmpty()) {
                mPlcBinding.tvSetNameRemind.visibility = View.GONE
            }
        }
        mPlcBinding.tvYes.setOnClickListener {
            val text = mPlcBinding.etSetName.text.toString().trim()
            if (text.isEmpty()) {
                mPlcBinding.tvSetNameRemind.visibility = View.VISIBLE
            } else {
                dismiss()
                tilFunBack(text)
            }
        }

        DialogMangerPlfUtils.changeWindowHalf(window)
    }
}