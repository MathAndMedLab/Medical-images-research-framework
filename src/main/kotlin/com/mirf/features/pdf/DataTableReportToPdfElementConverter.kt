package com.mirf.features.pdf

import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.UnitValue
import com.mirf.core.data.DataTable
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.report.DataTableAlgorithmReport
import com.mirf.features.reports.PdfElementData
import com.mirf.features.repositoryaccessors.AlgorithmExecutionException
import java.awt.image.BufferedImage
import java.util.*
import javax.imageio.ImageIO


//----------------------------------------------------------------------------------------------------------

fun DataTable.asPdfElementData() : PdfElementData {
    val font: PdfFont? = PdfFontFactory.createFont(StandardFonts.HELVETICA)
    val bold: PdfFont? = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)

    val table = Table(this.columns.size)

    table.width = UnitValue.createPercentValue(100f)

    addHeaders(table, this.columns, bold)
    for (row in this.rows)
        addRow(table, row, font, this.columns)

    return PdfElementData(table)
}

fun DataTableAlgorithmReport.asPdfElementData(): PdfElementData {
    return this.table.asPdfElementData()
}

private fun addRow(table: Table, items: Dictionary<String, String>, font: PdfFont?, headers: Collection<String>) {
    for (header in headers)
        table.addCell(Cell().add(Paragraph(items.get(header)).setFont(font)))
}

private fun addHeaders(table: Table, items: Collection<String>, font: PdfFont?) {
    for (item in items)
        table.addHeaderCell(Cell().add(Paragraph(item).setFont(font)))
}

//----------------------------------------------------------------------------------------------------------

fun ImageSeries.asPdfElementData(range: Iterable<Int> = 0 until this.images.size): PdfElementData {
    val images = this.images.map { x -> x.image }

    val result = Paragraph()
    try {
        for (i in range) {
            val image = images[i]
            val stream = ByteArrayOutputStream()
            ImageIO.write(image, "jpg", stream)

            val pdfImage = Image(ImageDataFactory.create(stream.toByteArray()))
            pdfImage.width = UnitValue.createPercentValue(33f)
            pdfImage.setMargins(5f, 5f, 5f, 5f)
            pdfImage.setHeight(UnitValue.createPercentValue(50f))

            result.add(pdfImage)
        }
    } catch (e: Exception) {
        throw AlgorithmExecutionException(e)
    }
    return PdfElementData(result)
}

fun List<BufferedImage>.asPdfElementData(range: Iterable<Int> = 0 until this.size): PdfElementData {
    val images = this

    val result = Paragraph()
    try {
        for (i in range) {
            val image = images[i]
            val stream = ByteArrayOutputStream()
            ImageIO.write(image, "jpg", stream)

            val pdfImage = Image(ImageDataFactory.create(stream.toByteArray()))
            pdfImage.width = UnitValue.createPercentValue(33f)
            pdfImage.setMargins(5f, 5f, 5f, 5f)
            pdfImage.setHeight(UnitValue.createPercentValue(50f))

            result.add(pdfImage)
        }
    } catch (e: Exception) {
        throw AlgorithmExecutionException(e)
    }
    return PdfElementData(result)
}

