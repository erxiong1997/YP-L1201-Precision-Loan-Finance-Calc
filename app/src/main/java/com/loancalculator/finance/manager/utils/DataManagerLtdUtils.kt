package com.loancalculator.finance.manager.utils

import android.content.Context
import androidx.core.content.edit
import com.loancalculator.finance.manager.PlfDealApplication

object DataManagerLtdUtils {
    private const val SHARED_PREFERENCES_NAME = "LightDownloaderData"

    fun setDataKeyLtd(saveKey: String, saveData: Any) {
        try {
            val sp = PlfDealApplication.Companion.mPlfContext.getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
            )
            sp.edit {
                when (saveData) {
                    is String -> putString(saveKey, saveData)
                    is Float -> putFloat(saveKey, saveData)
                    is Long -> putLong(saveKey, saveData)
                    is Int -> putInt(saveKey, saveData)
                    is Boolean -> putBoolean(saveKey, saveData)
                }
            }
        } catch (e: Exception) {

        }
    }

    fun <T> getDataKeyLtd(getKey: String, defaultData: T): T {
        return try {
            val sp = PlfDealApplication.Companion.mPlfContext.getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
            )
            when (defaultData) {
                is String -> sp.getString(getKey, defaultData) as T
                is Float -> sp.getFloat(getKey, defaultData) as T
                is Long -> sp.getLong(getKey, defaultData) as T
                is Int -> sp.getInt(getKey, defaultData) as T
                is Boolean -> sp.getBoolean(getKey, defaultData) as T
                else -> defaultData
            }
        } catch (e: Exception) {
            defaultData
        }
    }
}