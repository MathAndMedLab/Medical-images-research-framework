package features.reports

import com.itextpdf.layout.element.IBlockElement
import core.data.Data
import core.data.MirfData

/**
 * Host class for [IBlockElement] to transmit throughout framework.
 */
class PdfElementData(val pdfElement: IBlockElement) : MirfData()
