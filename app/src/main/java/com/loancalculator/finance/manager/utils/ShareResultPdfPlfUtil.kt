package com.loancalculator.finance.manager.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import androidx.core.graphics.createBitmap
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.showToastIDPlf

object ShareResultPdfPlfUtil {

    /**
     * 把 Bitmap 转成单页 PDF，然后直接调起系统分享
     *
     * @param context Context
     * @param bitmap 要转成 PDF 的图片（建议 ARGB_8888）
     * @param pdfFileName 生成的 PDF 文件名（不含后缀）
     * @param shareTitle 分享面板标题
     * @param quality PDF 内图片的 JPEG 压缩质量（0-100，默认 90）
     */
    fun shareBitmapAsSinglePagePdf(
        context: Context,
        bitmap: Bitmap,
        pdfFileName: String = "result",
        shareTitle: String = "",
        quality: Int = 90
    ) {
        var pdfDocument: PdfDocument? = null
        var page: PdfDocument.Page? = null
        var tempPdfFile: File? = null

        try {
            // 1. 创建 PDF 文档
            pdfDocument = PdfDocument()

            // 2. 创建页面（尺寸与 Bitmap 一致）
            val pageInfo = PdfDocument.PageInfo.Builder(
                bitmap.width,
                bitmap.height,
                1  // 页码，从 1 开始
            ).create()

            page = pdfDocument.startPage(pageInfo)

            // 3. 把 Bitmap 画到 PDF 页面上
            val canvas: Canvas = page.canvas
            canvas.drawColor(Color.WHITE)           // 可选：背景填白
            canvas.drawBitmap(bitmap, 0f, 0f, null)

            pdfDocument.finishPage(page)

            // 4. 保存到临时文件（用 cacheDir 避免权限麻烦）
            tempPdfFile = File(context.cacheDir, "$pdfFileName.pdf")
            if (tempPdfFile.exists()) {
                tempPdfFile.delete()
            }
            FileOutputStream(tempPdfFile).use { out ->
                pdfDocument.writeTo(out)
            }

            // 5. 通过 FileProvider 获取分享 Uri
            val pdfUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                tempPdfFile
            )

            // 6. 启动分享
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, pdfUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, shareTitle))

        } catch (e: IOException) {
            e.printStackTrace()
            // 可以在这里 Toast "生成 PDF 失败"
        } finally {
            // 清理
            pdfDocument?.close()
            // tempPdfFile 可选择不立即删除，让系统缓存自己清理
        }
    }

    // 辅助方法：从 View 创建 Bitmap（比如分享整个屏幕或某个布局）
    fun createBitmapFromView(view: android.view.View): Bitmap? {
        return try {
            val bitmap = createBitmap(view.width, view.height)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun generateInvestmentPdf(
        context: Context,
        listText: List<String>,
        listIndex: List<Int>,
        pdfFileName: String = "share_pdf_file",
        shareAfterCreate: Boolean = true
    ) {
        var pdfDocument: PdfDocument? = null
        var page: PdfDocument.Page? = null
        var tempPdfFile: File? = null

        try {
            // 1. 创建 PDF 文档（A4 大小，595x842 pt）
            pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            page = pdfDocument.startPage(pageInfo)

            // 2. 设置画笔（字体大小、颜色等）
            val canvas: Canvas = page.canvas
            val paint = Paint().apply {
                color = Color.BLACK
                textSize = 16f  // 调整字体大小
                isAntiAlias = true
                isFakeBoldText = true
            }

            // 3. 一个一个写上文本（模拟用户图片/PDF 的布局，从上到下，左对齐）
            var yPosition = 50f  // 从顶部开始，单位 pt
            var count = 0
            for ((i, text) in listText.withIndex()) {
                if (i in listIndex) {
                    canvas.drawText(text, 50f, yPosition, paint)
                    yPosition += 28f
                } else {
                    count++
                    canvas.drawText(
                        text, if (count == 1) {
                            50f
                        } else {
                            400f
                        }, yPosition, paint
                    )
                    if (count == 2) {
                        count = 0
                        yPosition += 28f
                        if ((i + 1) in listIndex) {
                            yPosition += 28f
                        }
                    }
                }
            }
            // 4. 完成页面
            pdfDocument.finishPage(page)

            // 5. 保存到临时文件
            tempPdfFile = File(context.cacheDir, "$pdfFileName.pdf")
            if (tempPdfFile.exists()) {
                tempPdfFile.delete()
            }
            FileOutputStream(tempPdfFile).use { out ->
                pdfDocument.writeTo(out)
            }

            if (shareAfterCreate) {
                // 6. 分享 PDF（用 FileProvider）
                val pdfUri: Uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    tempPdfFile
                )
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, pdfUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(shareIntent, ""))
            }

        } catch (e: Exception) {
            context.showToastIDPlf(R.string.plf_sharing_failed)
        } finally {
            pdfDocument?.close()
        }

    }
}