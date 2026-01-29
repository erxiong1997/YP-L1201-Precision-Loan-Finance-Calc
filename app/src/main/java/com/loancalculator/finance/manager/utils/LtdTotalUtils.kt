package com.loancalculator.finance.manager.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.showToastIDLtd
import com.loancalculator.finance.manager.utils.value.ConstantOftenLtd.LTD_APP_FROM_VALUE
import com.loancalculator.finance.manager.utils.value.ConstantOftenLtd.LTD_AY_PERIOD_VALUE
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object LtdTotalUtils {
    fun playVideoLtd(context: Context, file: File?) {
        if (file == null) return
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(
            uri, when (file.extension) {
                "mp4", "MP4" -> {
                    "video/*"
                }

                "mp3", "MP3" -> {
                    "audio/*"
                }

                "jpg", "tiff", "jpeg", "webp", "png", "gif" -> {
                    "image/*"
                }

                else -> {
                    "video/*"
                }
            }
        )
        context.startActivity(intent)
    }

    fun shareVideoLtd(activity: Activity, file: File) {
        try {
            val uri = FileProvider.getUriForFile(
                activity, "${activity.packageName}.fileprovider", file
            )
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = when (file.extension) {
                    "mp4", "MP4" -> {
                        "video/*"
                    }

                    "mp3", "MP3" -> {
                        "audio/*"
                    }

                    "jpg", "tiff", "jpeg", "webp", "png", "gif" -> {
                        "image/*"
                    }

                    else -> {
                        "video/*"
                    }
                }
                putExtra(Intent.EXTRA_STREAM, uri)
            }
            activity.startActivity(Intent.createChooser(shareIntent, ""))
        } catch (_: Exception) {
            activity.showToastIDLtd(R.string.ltd_sharing_failed)
        }
    }

    fun getFileDateLtd(long: Long): String {
        val sim = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        return sim.format(Date(long))
    }

    fun fileNameLtd(fileName: String): Boolean {
        return !fileName.contains("/") && !fileName.contains("\u0000")
    }

    fun fileNameLtd2(fileName: String): Boolean {
        val illegalRegex = Regex("[\\\\/:*?\"<>|]")
        return !illegalRegex.containsMatchIn(fileName)
    }

    fun getClipTextLtd(board: ClipboardManager): String? {
        return try {
            val clip = board.primaryClip ?: return null
            if (clip.itemCount > 0) {
                clip.getItemAt(0).text.toString()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    fun openAppBrowserLtd(context: Context, ltdType: String) {
        try {
            var intent: Intent? = null
            when (ltdType) {
                "ltdFacebook" -> {
                    intent = Intent(Intent.ACTION_VIEW).apply {
                        data = "https://www.facebook.com/".toUri()
                        setPackage("com.facebook.katana")
                        addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                        )
                    }
                }

                "ltdIns" -> {
                    intent = try {
                        Intent(Intent.ACTION_VIEW, "instagram://app".toUri())
                    } catch (_: Exception) {
                        // 如果 Scheme 格式错误或不被识别，直接打开网页版
                        Intent(Intent.ACTION_VIEW, "https://www.instagram.com".toUri())
                    }
                }

                "ltdX" -> {
                    val packageName = "com.twitter.android"
                    val pm = context.packageManager
                    intent = pm.getLaunchIntentForPackage(packageName)
                    intent?.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                    )
                }

                "ltdPin" -> {
                    intent = Intent(Intent.ACTION_VIEW).apply {
                        data = "https://www.pinterest.com/".toUri()
                        setPackage("com.pinterest")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            val dvpIntent =
                Intent(
                    Intent.ACTION_VIEW, when (ltdType) {
                        "ltdFacebook" -> {
                            "https://www.facebook.com/".toUri()
                        }

                        "ltdX" -> {
                            "https://x.com/home".toUri()
                        }

                        "ltdPin" -> {
                            "https://www.pinterest.com".toUri()
                        }

                        "ltdIns" -> {
                            "https://www.instagram.com/reels".toUri()
                        }

                        else -> {
                            "".toUri()
                        }
                    }
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            try {
                context.startActivity(dvpIntent)
            } catch (_: ActivityNotFoundException) {
                context.showToastIDLtd(R.string.ltd_failed_open_app_installed)
            }
        }
    }

    fun getLtdFullDate(long: Long): String {
        val sim = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault())
        return sim.format(Date(long))
    }

    fun getLtdAppStatus(): Boolean {
        return DataManagerLtdUtils.getDataKeyLtd(LTD_AY_PERIOD_VALUE, "ltdValue") == "ltdValue"
                && DataManagerLtdUtils.getDataKeyLtd(LTD_APP_FROM_VALUE, "") == "ltdValue"
    }


    fun getClueForBitmap(context: Context, index: Int): Bitmap? {
        return try {
            val resourceId =
                context.resources.getIdentifier("ltd_pic_${index}", "raw", context.packageName)
            getClueForBitmapTwo(context.resources.openRawResource(resourceId).readBytes())
        } catch (e: Exception) {
            null
        }
    }

    private fun getClueForBitmapTwo(bytes: ByteArray): Bitmap? {
        return try {
            val pasword = "ltdpowervideoltd"
            val key = "ooiuh52sa7fsadf0".toByteArray()
            val newKey = getPasswordValue(pasword, key)
            val newBytes = getBitmapValue(bytes, newKey)
            return BitmapFactory.decodeByteArray(newBytes, 0, newBytes.size)
        } catch (e: Exception) {
            null
        }
    }

    private fun getPasswordValue(password: String, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }

    private fun getBitmapValue(encryptedData: ByteArray, key: SecretKey): ByteArray {
        val iv = encryptedData.copyOfRange(0, 16)
        val actualData = encryptedData.copyOfRange(16, encryptedData.size)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        return cipher.doFinal(actualData)
    }
}