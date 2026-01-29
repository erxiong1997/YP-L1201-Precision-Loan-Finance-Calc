package com.loancalculator.finance.manager.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.loancalculator.finance.manager.clueftLtd.LtdTokenMeans

object PermissionLtdUtils {
    fun getNotifyStatus(context: Context): Boolean {
        return XXPermissions.isGrantedPermissions(context, Permission.POST_NOTIFICATIONS)
    }

    fun getNotifyPermissionApply(activity: AppCompatActivity, tilFunBack: (Boolean) -> Unit) {
        if (getNotifyStatus(activity)) {
            tilFunBack(true)
        } else {
            XXPermissions.with(activity).permission(Permission.POST_NOTIFICATIONS)
                .request(object : OnPermissionCallback {
                    override fun onGranted(
                        permissions: MutableList<String>, allGranted: Boolean
                    ) {
//                        PlcTokenMeans.getLtdTokenValue(activity)
                        tilFunBack(true)
                    }

                    override fun onDenied(
                        permissions: MutableList<String>, doNotAskAgain: Boolean
                    ) {
                        tilFunBack(false)
                    }
                })
        }
    }
}