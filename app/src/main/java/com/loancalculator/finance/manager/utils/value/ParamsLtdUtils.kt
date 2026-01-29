package com.loancalculator.finance.manager.utils.value

import com.loancalculator.finance.manager.PlfDealApplication
import java.io.File

object ParamsLtdUtils {

    val mSaveFilePlf by lazy {
        File(
            PlfDealApplication.mPlfContext.filesDir,
            "PrecisionLoanFiles"
        ).apply {
            if (!exists()) {
                mkdir()
            }
        }
    }

    // 存储点击结果通知
//    var mCompleteVideoList = mutableListOf<DataDownloadingLtd>()
}