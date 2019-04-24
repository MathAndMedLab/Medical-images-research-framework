package com.mirf.features.reports.pdf

import com.itextpdf.kernel.pdf.PdfDocument
import java.io.ByteArrayOutputStream

data class PdfDocumentInfo(val doc: PdfDocument, val stream: ByteArrayOutputStream) {
}