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
import androidx.core.graphics.createBitmap
import com.loancalculator.finance.manager.PlfDealApplication.Companion.mDirTableFile
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.showToastIDPlf
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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
        pdfFileName: String = "Pdf_${System.currentTimeMillis()}",
        shareAfterCreate: Boolean = true
    ) {
        var pdfDocument: PdfDocument? = null
        var page: PdfDocument.Page? = null
        var tempPdfFile: File? = null

        try {
            // 1. 创建 PDF 文档（A4 大小，595x842 pt）
            pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(594, 842, 1).create()
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
            tempPdfFile = File(mDirTableFile, "$pdfFileName.pdf")
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
                    putExtra(Intent.EXTRA_TITLE, tempPdfFile.name)
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

    fun generateInvestmentPdf(
        context: Context,
        listText: List<String>,
        listIndex: List<Int>,
        listHorTable: List<LoanMonthDetail>?,
        pdfFileName: String = "Pdf_${System.currentTimeMillis()}",
        shareAfterCreate: Boolean = true
    ) {
        if (listHorTable == null) return
        var pdfDocument: PdfDocument? = null
        var page: PdfDocument.Page? = null
        var canvas: Canvas? = null
        var yPosition = 50f
        var tempPdfFile: File? = null

        try {
            // 1. 创建 PDF 文档（A4 大小，595x842 pt）
            pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            page = pdfDocument.startPage(pageInfo)
            canvas = page.canvas

            // 2. 设置画笔（字体大小、颜色等）
            val paint = Paint().apply {
                color = Color.BLACK
                textSize = 16f  // 调整字体大小
                isAntiAlias = true
                isFakeBoldText = true
            }

            // 3. 一个一个写上文本（模拟用户图片/PDF 的布局，从上到下，左对齐）
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

            // 4. 绘制 Amortization Table
            val headers = arrayOf(
                context.getString(R.string.app_jin_hao),
                context.getString(R.string.plf_payment),
                context.getString(R.string.plf_interest),
                context.getString(R.string.plf_principal),
                context.getString(R.string.plf_balance)
            )
            val columnX = floatArrayOf(50f, 100f, 200f, 300f, 400f)
            val nf = java.text.NumberFormat.getNumberInstance()

            // 绘制表头
            for (j in headers.indices) {
                canvas.drawText(headers[j], columnX[j], yPosition, paint)
            }
            yPosition += 28f

            // 绘制表格行
            for (detail in listHorTable) {
                if (yPosition > 800f) {  // 接近页面底部，换页
                    pdfDocument.finishPage(page)
                    val newPageInfo =
                        PdfDocument.PageInfo.Builder(595, 842, pdfDocument.pages.size + 1).create()
                    page = pdfDocument.startPage(newPageInfo)
                    canvas = page.canvas
                    yPosition = 50f

                    // 新页重新绘制表头
                    for (j in headers.indices) {
                        canvas.drawText(headers[j], columnX[j], yPosition, paint)
                    }
                    yPosition += 28f
                }

                // 绘制行数据
                canvas?.drawText(detail.month.toString(), columnX[0], yPosition, paint)
                canvas?.drawText(nf.format(detail.payment.toInt()), columnX[1], yPosition, paint)
                canvas?.drawText(
                    nf.format(detail.interestPart.toInt()),
                    columnX[2],
                    yPosition,
                    paint
                )
                canvas?.drawText(
                    nf.format(detail.principalPart.toInt()),
                    columnX[3],
                    yPosition,
                    paint
                )
                canvas?.drawText(detail.remainingPrincipal.toString(), columnX[4], yPosition, paint)
                yPosition += 28f
            }

            // 5. 完成最后一页
            pdfDocument.finishPage(page)

            // 6. 保存到临时文件
            tempPdfFile = File(mDirTableFile, "$pdfFileName.pdf")
            if (tempPdfFile.exists()) {
                tempPdfFile.delete()
            }
            FileOutputStream(tempPdfFile).use { out ->
                pdfDocument.writeTo(out)
            }

            if (shareAfterCreate) {
                // 7. 分享 PDF（用 FileProvider）
                val pdfUri: Uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    tempPdfFile
                )
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, pdfUri)
                    putExtra(Intent.EXTRA_TITLE, tempPdfFile.name)
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

    /**
     * 生成包含基本信息 + 分期表的 Excel 文件，并分享
     *
     * @param context Context
     * @param loanAmount 贷款金额 (如 10000.0)
     * @param annualRate 年利率 (如 0.10)
     * @param termYears 年限 (如 1)
     * @param firstPaymentDate 首付日期 (如 "05/02/2026")
     * @param totalPrincipalPaid 总本金 (通常 = loanAmount)
     * @param totalInterest 总利息 (如 6703.0)
     * @param totalAmount 总本息 (如 126702.81)
     * @param maturityDate 到期日 (如 "05/02/2027")
     * @param rows 分期表数据 List<Map<String, Any>> 或自定义数据类
     * @param fileNameWithoutExt 文件名（不含 .xlsx）
     */

    fun generateAndShareExcel(
        context: Context,
        loanAmount: Int,
        interestRate: Double,
        loanTerm: String,
        startDate: String,
        monthlyPayment: Double,
        totalPay: String,
        totalInterestPay: String,
        payingOffDate: String,
        rows: List<LoanMonthDetail>?,  // 分期表每一行
        fileNameWithoutExt: String = "Amortization_${System.currentTimeMillis()}"
    ) {
        if (rows == null) {
            context.showToastIDPlf(R.string.plf_sharing_failed)
            return
        }
        try {
            val workbook: Workbook = XSSFWorkbook()
            val sheet: Sheet = workbook.createSheet("Amortization Table")

            // 样式准备
            val headerStyle = workbook.createCellStyle().apply {
                fillForegroundColor = IndexedColors.WHITE.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
                alignment = HorizontalAlignment.CENTER
                verticalAlignment = VerticalAlignment.CENTER
                borderTop = BorderStyle.THIN
                borderBottom = BorderStyle.THIN
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
            }
            val boldFont = workbook.createFont().apply { bold = true }
            headerStyle.setFont(boldFont)

            val cellStyle = workbook.createCellStyle().apply {
                alignment = HorizontalAlignment.CENTER
                verticalAlignment = VerticalAlignment.CENTER
                borderTop = BorderStyle.THIN
                borderBottom = BorderStyle.THIN
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
            }

//        val dateStyle = workbook.createCellStyle().apply {
//            dataFormat = workbook.creationHelper.createDataFormat().getFormat("yyyy-MM-dd")
//            alignment = HorizontalAlignment.CENTER
//        }

            var rowIndex = 0

            // ── 第一部分：基本信息 ──
            sheet.createRow(rowIndex++).apply {
                createCell(0).apply {
                    setCellValue(context.getString(R.string.plf_loan_amount))
                    this.cellStyle = cellStyle
                }
                createCell(1).apply {
                    setCellValue(loanAmount.toDouble())
                    this.cellStyle = cellStyle
                }
            }
            sheet.createRow(rowIndex++).apply {
                createCell(0).apply {
                    setCellValue(context.getString(R.string.plf_interest_rate))
                    this.cellStyle = cellStyle
                }
                createCell(1).apply {
                    setCellValue("${interestRate}%")
                    this.cellStyle = cellStyle
                }
            }
            sheet.createRow(rowIndex++).apply {
                createCell(0).apply {
                    setCellValue(context.getString(R.string.plf_loan_term))
                    this.cellStyle = cellStyle
                }
                createCell(1).apply {
                    setCellValue(loanTerm)
                    this.cellStyle = cellStyle
                }
            }
            sheet.createRow(rowIndex++).apply {
                createCell(0).apply {
                    setCellValue(context.getString(R.string.plf_start_date))
                    this.cellStyle = cellStyle
                }
                createCell(1).apply {
                    setCellValue(startDate)
                    this.cellStyle = cellStyle
                }
            }

            rowIndex++  // 空行

            sheet.createRow(rowIndex++).apply {
                createCell(0).apply {
                    setCellValue(context.getString(R.string.plf_monthly_payment))
                    this.cellStyle = cellStyle
                }
                createCell(1).apply {
                    setCellValue(monthlyPayment)
                    this.cellStyle = cellStyle
                }
            }
            sheet.createRow(rowIndex++).apply {
                createCell(0).apply {
                    setCellValue(context.getString(R.string.plf_total_payment))
                    this.cellStyle = cellStyle
                }
                createCell(1).apply {
                    setCellValue(totalPay)
                    this.cellStyle = cellStyle
                }
            }
            sheet.createRow(rowIndex++).apply {
                createCell(0).apply {
                    setCellValue(context.getString(R.string.plf_total_interest_payable))
                    this.cellStyle = cellStyle
                }
                createCell(1).apply {
                    setCellValue(totalInterestPay)
                    this.cellStyle = cellStyle
                }
            }
            sheet.createRow(rowIndex++).apply {
                createCell(0).apply {
                    setCellValue(context.getString(R.string.plf_paying_off_date))
                    this.cellStyle = cellStyle
                }
                createCell(1).apply {
                    setCellValue(payingOffDate)
                    this.cellStyle = cellStyle
                }
            }

            rowIndex += 2  // 多空两行分隔

            // ── 第二部分：分期表标题 ──
            val headerRow = sheet.createRow(rowIndex++)
            listOf(
                context.getString(R.string.app_jin_hao),
                context.getString(R.string.plf_payment),
                context.getString(R.string.plf_interest),
                context.getString(R.string.plf_principal),
                context.getString(R.string.plf_balance)
            ).forEachIndexed { i, title ->
                val cell = headerRow.createCell(i)
                cell.setCellValue(title)
                cell.cellStyle = headerStyle
            }

            // ── 分期表数据 ──
            rows.forEach { row ->
                val dataRow = sheet.createRow(rowIndex++)
                dataRow.createCell(0).apply {
                    setCellValue(row.month.toDouble())
                    this.cellStyle = cellStyle
                }
                dataRow.createCell(1).apply {
                    setCellValue(row.payment)
                    this.cellStyle = cellStyle
                }
                dataRow.createCell(2).apply {
                    setCellValue(row.interestPart)
                    this.cellStyle = cellStyle
                }
                dataRow.createCell(3).apply {
                    setCellValue(row.principalPart)
                    this.cellStyle = cellStyle
                }
                dataRow.createCell(4).apply {
                    setCellValue(row.remainingPrincipal)
                    this.cellStyle = cellStyle
                }
            }

            sheet.setColumnWidth(0, 20 * 256)   // #
            sheet.setColumnWidth(1, 16 * 256)   // Payment
            sheet.setColumnWidth(2, 16 * 256)   // Interest
            sheet.setColumnWidth(3, 16 * 256)   // Principal
            sheet.setColumnWidth(4, 16 * 256)

            // 自动调整列宽
//            (0..4).forEach { sheet.autoSizeColumn(it) }

            // 保存文件
            val file = File(mDirTableFile, "$fileNameWithoutExt.xlsx")
            FileOutputStream(file).use { out ->
                workbook.write(out)
            }
            workbook.close()

            // 分享
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, ""))
        } catch (_: Exception) {
            context.showToastIDPlf(R.string.plf_sharing_failed)
        }
    }
}