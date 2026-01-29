//package com.loancalculator.finance.manager.utils.dialog
//
//import android.content.Context
//import android.os.Bundle
//import android.view.Gravity
//import androidx.appcompat.app.AppCompatDialog
//import com.loancalculator.finance.manager.databinding.DialogCanNextLtdBinding
//import com.loancalculator.finance.manager.dismissGoLtd
//
//class DialogCanNextLtd(
//    private val mContext: Context,
//    private val body: Int? = null,
//    private val tilFunBack: () -> Unit
//) : AppCompatDialog(mContext) {
//    private lateinit var mPlcBinding: DialogCanNextLtdBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mPlcBinding = DialogCanNextLtdBinding.inflate(layoutInflater)
//        setContentView(mPlcBinding.root)
//        setCancelable(true)
//
//        body?.let { mPlcBinding.tvLtdBody.text = mContext.getString(it) }
//        mPlcBinding.ivCloseDialog.setOnClickListener { dismissGoLtd() }
//        mPlcBinding.tvOK.setOnClickListener {
//            dismissGoLtd()
//            tilFunBack()
//        }
//
//        DialogMangerLtdUtils.changeWindowHalf(window, gravity = Gravity.CENTER)
//    }
//}