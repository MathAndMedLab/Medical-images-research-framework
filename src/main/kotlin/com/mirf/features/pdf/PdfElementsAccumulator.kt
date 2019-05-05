package com.mirf.features.pdf

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.mirf.core.algorithm.Algorithm
import com.mirf.core.data.CollectionData
import com.mirf.core.data.FileData
import com.mirf.features.reports.PdfElementData

import java.io.ByteArrayOutputStream

/**
 * [Algorithm] that puts all [PdfElementData] from input to PDF document and returns [FileData] with generated PDF
 */
class PdfElementsAccumulator(private val reportName: String) : Algorithm<CollectionData<PdfElementData>, FileData> {

    override fun execute(input: CollectionData<PdfElementData>): FileData {

        val resultStream = ByteArrayOutputStream()
        val writer = PdfWriter(resultStream)

        val pdf = PdfDocument(writer)

        val document = Document(pdf, PageSize.A4)

        document.setMargins(10f, 10f, 10f, 10f)

        for (element in input.collection)
            document.add(element.pdfElement)

        document.close()
        return FileData(resultStream.toByteArray(), reportName, EXTENSION)
    }

    companion object {

        private const val EXTENSION = ".pdf"
    }
}
