package features.reports

import com.itextpdf.layout.element.IBlockElement
import core.data.Data

/**
 * Host class for [IBlockElement] to transmit throughout framework.
 */
class PdfElementData(val pdfElement: IBlockElement) : Data()
