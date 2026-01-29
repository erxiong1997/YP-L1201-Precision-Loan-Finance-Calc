//package com.loancalculator.finance.manager.utils.dialog
//
//import android.content.Context
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatDialog
//import com.loancalculator.finance.manager.R
//import com.loancalculator.finance.manager.databinding.DialogRenameFileLtdBinding
//import com.loancalculator.finance.manager.showToastIDLtd
//import com.loancalculator.finance.manager.utils.LtdTotalUtils
//import java.io.File
//
//class DialogRenameFileLtd(
//    private val mContext: Context,
//    private val mRenameFile: File,
//    private val tilFunBack: (File) -> Unit
//) : AppCompatDialog(mContext) {
//    private lateinit var mPlcBinding: DialogRenameFileLtdBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mPlcBinding = DialogRenameFileLtdBinding.inflate(layoutInflater)
//        setContentView(mPlcBinding.root)
//        setCancelable(false)
//
//        mPlcBinding.etFileName.setText(mRenameFile.name)
//        mPlcBinding.ivLtdCls.setOnClickListener {
//            mPlcBinding.etFileName.text = null
//        }
//
//        mPlcBinding.ivCloseDialog.setOnClickListener { dismiss() }
//        mPlcBinding.tvOK.setOnClickListener {
//            var newName = mPlcBinding.etFileName.text.toString().trim()
//            if (newName.isEmpty()) {
//                mContext.showToastIDLtd(R.string.ltd_no_file_name)
//                return@setOnClickListener
//            }
//            if (mRenameFile.name == newName) {
//                dismiss()
//                return@setOnClickListener
//            }
//            if (!LtdTotalUtils.fileNameLtd(newName) && !LtdTotalUtils.fileNameLtd2(newName)) {
//                mContext.showToastIDLtd(R.string.ltd_containts_unspor)
//                return@setOnClickListener
//            }
//            if (!newName.contains(".")) {
//                val ex = mRenameFile.extension
//                if (!newName.endsWith(ex, true)) {
//                    newName = "$newName.${ex}"
//                }
//            }
//            val file = File(mRenameFile.parent, newName)
//            if (file.exists()) {
//                mContext.showToastIDLtd(R.string.ltd_file_name_exists)
//            } else {
//                tilFunBack(file)
//                dismiss()
//            }
//        }
//
//        DialogMangerLtdUtils.changeWindowHalf(window)
//    }
//}