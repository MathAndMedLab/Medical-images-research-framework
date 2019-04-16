package com.mirf.features.reports

import com.itextpdf.layout.element.IBlockElement
import com.mirf.core.data.MirfData

/**
 * Host class for [IBlockElement] to transmit throughout framework.
 */
class PdfElementData(val pdfElement: IBlockElement) : MirfData()
