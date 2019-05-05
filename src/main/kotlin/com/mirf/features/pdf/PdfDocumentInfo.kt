package com.mirf.features.pdf

import com.itextpdf.kernel.pdf.PdfDocument
import java.io.ByteArrayOutputStream

data class PdfDocumentInfo(val doc: PdfDocument, val stream: ByteArrayOutputStream) {
}