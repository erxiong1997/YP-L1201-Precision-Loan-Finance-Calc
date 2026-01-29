package com.loancalculator.finance.manager.utils.value

import com.loancalculator.finance.manager.PlfDealApplication
import com.loancalculator.finance.manager.ltddata.DataDownloadingLtd
import java.io.File

object ParamsLtdUtils {

    var mLtdInitUmp = false
//    val mParseLinksList = mutableListOf<DataDownloadingLtd>()

    //正在下载列表
    val mDownloadingLtdList = mutableListOf<DataDownloadingLtd>()

    val mSaveFileLtd by lazy {
        File(
            PlfDealApplication.mPlfContext.filesDir,
            "LightDownloader"
        ).apply {
            if (!exists()) {
                mkdir()
            }
        }
    }

    // 存储点击结果通知
//    var mCompleteVideoList = mutableListOf<DataDownloadingLtd>()
}