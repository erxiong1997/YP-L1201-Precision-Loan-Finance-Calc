package com.loancalculator.finance.manager.utils

import com.loancalculator.finance.manager.data.DataUtcSelectPlf
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object DealRecentPlfUtils {
    //保存世界时间的时区，方便下次展示
    const val PLF_WORLD_TIME_UTC = "plfWorldTimeUtc"

    fun addWorldTimeUtcRecent(dataRecentInfo: DataUtcSelectPlf) {
        try {
            val recordData = DataManagerLtdUtils.getDataKeyPlf(PLF_WORLD_TIME_UTC, "")
            val list = mutableListOf<DataUtcSelectPlf>()
            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(
                MutableList::class.java, DataUtcSelectPlf::class.java
            )
            val adapter = moshi.adapter<MutableList<DataUtcSelectPlf>>(type)
            if (recordData.isNotEmpty()) {
                adapter.fromJson(recordData)?.let {
                    list.addAll(it)
                }
            }
            list.add(0, dataRecentInfo)
            DataManagerLtdUtils.setDataKeyPlf(PLF_WORLD_TIME_UTC, adapter.toJson(list))
        } catch (_: Exception) {
        }
    }

    fun addWorldTimeUtcRecent(saveList: MutableList<DataUtcSelectPlf>) {
        try {
            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(
                MutableList::class.java, DataUtcSelectPlf::class.java
            )
            val adapter = moshi.adapter<MutableList<DataUtcSelectPlf>>(type)
            if (saveList.isEmpty()) {
                DataManagerLtdUtils.setDataKeyPlf(PLF_WORLD_TIME_UTC, "")
            } else {
                DataManagerLtdUtils.setDataKeyPlf(PLF_WORLD_TIME_UTC, adapter.toJson(saveList))
            }
        } catch (_: Exception) {
        }
    }

    fun getWorldTimeUtcRecent(curList: MutableList<DataUtcSelectPlf>): MutableList<DataUtcSelectPlf> {
        try {
            val recordData = DataManagerLtdUtils.getDataKeyPlf(PLF_WORLD_TIME_UTC, "")
            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(
                MutableList::class.java, DataUtcSelectPlf::class.java
            )
            val adapter = moshi.adapter<MutableList<DataUtcSelectPlf>>(type)
            if (recordData.isNotEmpty()) {
                adapter.fromJson(recordData)?.let {
                    curList.addAll(it)
                }
            }
        } catch (_: Exception) {
        }
        return curList
    }
}