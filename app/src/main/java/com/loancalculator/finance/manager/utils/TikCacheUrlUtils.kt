package com.loancalculator.finance.manager.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.loancalculator.finance.manager.ltddata.DataSaveHubLtd
import com.loancalculator.finance.manager.utils.value.ConstantOftenLtd.LTD_TIK_CACHE_URL

object TikCacheUrlUtils {

    fun getTikOldUrl(): MutableList<DataSaveHubLtd> {
        val listDataLtd = mutableListOf<DataSaveHubLtd>()
        try {
            val historyData = DataManagerLtdUtils.getDataKeyLtd(LTD_TIK_CACHE_URL, "")
            if (historyData.isEmpty()) {
                return listDataLtd
            }
            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(
                MutableList::class.java, DataSaveHubLtd::class.java
            )
            val adapter = moshi.adapter<MutableList<DataSaveHubLtd>>(type)
            adapter.fromJson(historyData)?.let {
                listDataLtd.addAll(it)
            }
        } catch (_: Exception) {
        }
        return listDataLtd
    }

    fun setTikOldUrl(newData: DataSaveHubLtd) {
        if (newData.searchUrl.isEmpty() || newData.ltdCacheUrl.isEmpty()) return
        val listDataLtd = mutableListOf<DataSaveHubLtd>()
        try {
            val historyData = DataManagerLtdUtils.getDataKeyLtd(LTD_TIK_CACHE_URL, "")
            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(
                MutableList::class.java, DataSaveHubLtd::class.java
            )
            val adapter = moshi.adapter<MutableList<DataSaveHubLtd>>(type)
            if (historyData.isNotEmpty()) {
                adapter.fromJson(historyData)?.let {
                    listDataLtd.addAll(it)
                }
            }
            for (data in listDataLtd) {
                if (data.searchUrl == newData.searchUrl) {
                    listDataLtd.remove(data)
                    break
                }
            }
            listDataLtd.add(0, newData)
            DataManagerLtdUtils.setDataKeyLtd(
                LTD_TIK_CACHE_URL, adapter.toJson(listDataLtd)
            )
        } catch (_: Exception) {
        }
    }


}