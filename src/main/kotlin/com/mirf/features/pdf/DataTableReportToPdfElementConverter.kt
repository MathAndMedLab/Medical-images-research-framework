package com.mirf.features.pdf

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.UnitValue
import com.mirf.core.common.debugDisplayInWindow
import com.mirf.core.data.DataTable
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.medimage.MedImage
import com.mirf.core.data.report.DataTableAlgorithmReport
import com.mirf.features.reports.PdfElementData
import com.mirf.features.repositoryaccessors.AlgorithmExecutionException
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


//----------------------------------------------------------------------------------------------------------

fun DataTable.asPdfElement(decorator: (Table) -> Table = { it },
                           displayHeaders: Boolean = true): Table {
    val table = Table((0 until this.columns.size).map { UnitValue.createPercentValue(100f / this.columns.size) }.toTypedArray())

    table.width = UnitValue.createPercentValue(100f)

    if (displayHeaders)
        addHeaders(table, this.columns)

    for (row in this.rows)
        addRow(table, row, this.columns)

    return decorator(table)
}

fun DataTable.asPdfElementData(decorator: (Table) -> Table = { it },
                               displayHeaders: Boolean = true): PdfElementData {
    return PdfElementData(this.asPdfElement(decorator, displayHeaders))
}

fun DataTableAlgorithmReport.asPdfElementData(): PdfElementData {
    return this.table.asPdfElementData()
}

private fun addRow(table: Table, items: HashMap<String, String>, headers: Collection<String>) {
    for (header in headers)
        table.addCell(Cell().add(Paragraph(items.get(header))))
}

private fun addHeaders(table: Table, items: Collection<String>) {
    for (item in items)
        table.addHeaderCell(Cell().add(Paragraph(item)))
}

//----------------------------------------------------------------------------------------------------------

fun ImageSeries.asPdfElement(range: Iterable<Int> = 0 until this.images.size,
                             imageDecorator: (MedImage) -> BufferedImage = { it.image!! },
                             elementDecorator: (Paragraph) -> Paragraph = { it }): Paragraph {
    val result = Paragraph()
    try {
        for (i in range) {
            val image = imageDecorator(this.images[i])

            val stream = ByteArrayOutputStream()
            ImageIO.write(image, "png", stream)

            val pdfImage = Image(ImageDataFactory.create(stream.toByteArray()))
            pdfImage.width = UnitValue.createPercentValue(33f)
            pdfImage.setMargins(5f, 5f, 5f, 5f)
            pdfImage.setHeight(UnitValue.createPercentValue(50f))

            result.add(pdfImage)
        }
    } catch (e: Exception) {
        throw AlgorithmExecutionException(e)
    }
    return elementDecorator(result)
}

fun ImageSeries.asPdfElementData(range: Iterable<Int> = 0 until this.images.size,
                                 imageDecorator: (MedImage) -> BufferedImage = { it.image!! },
                                 elementDecorator: (Paragraph) -> Paragraph = { it }): PdfElementData {
    return PdfElementData(this.asPdfElement(range, imageDecorator, elementDecorator))
}

fun List<BufferedImage>.asPdfElementData(range: Iterable<Int> = 0 until this.size,
                                         imageDecorator: (BufferedImage) -> BufferedImage = { it },
                                         elementDecorator: (Paragraph) -> Paragraph = { it }): PdfElementData {
    return PdfElementData(this.asPdfElement(range, imageDecorator, elementDecorator))
}

fun List<BufferedImage>.asPdfElement(range: Iterable<Int> = 0 until this.size,
                                     imageDecorator: (BufferedImage) -> BufferedImage = { it },
                                     elementDecorator: (Paragraph) -> Paragraph = { it }): Paragraph {
    val images = this

    val result = Paragraph()
    try {
        for (i in range) {
            val image = imageDecorator(images[i])

            val stream = ByteArrayOutputStream()
            ImageIO.write(image, "png", stream)

            val pdfImage = Image(ImageDataFactory.create(stream.toByteArray()))
            pdfImage.width = UnitValue.createPercentValue(33f)
            pdfImage.setMargins(5f, 5f, 5f, 5f)
            pdfImage.setHeight(UnitValue.createPercentValue(50f))

            result.add(pdfImage)
        }
    } catch (e: Exception) {
        throw AlgorithmExecutionException(e)
    }
    return elementDecorator(result)
}

