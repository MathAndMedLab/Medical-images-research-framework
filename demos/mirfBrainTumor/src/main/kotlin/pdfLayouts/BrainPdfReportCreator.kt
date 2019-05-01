package pdfLayouts

import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceGray
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.*
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import com.itextpdf.layout.property.VerticalAlignment
import com.mirf.features.reports.pdf.PdfDocumentInfo
import com.mirf.features.reports.pdf.asPdfImage
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO


class BrainPdfReportCreator(private val spec: BrainPdfReportSpec) {

    private val marginBlock = Paragraph().setMarginBottom(10f)

    fun createReport(): PdfDocumentInfo {

        val resultStream = ByteArrayOutputStream()
        val writer = PdfWriter(resultStream)
        val pdf = PdfDocument(writer)

        val document = Document(pdf, PageSize.A4)
        document.setMargins(0f, 10f, 10f, 10f)
        val pageWidth = pdf.defaultPageSize.width
        val pageHeight = pdf.defaultPageSize.height

        document.add(createHeader())

        document.add(marginBlock)

        if (spec.seriesDesc != null) {
            document.add(createScansDesc())
            document.add(marginBlock)
        }
        val table = Table(arrayOf(UnitValue.createPercentValue(50f), UnitValue.createPercentValue(50f)))
        table.addText("Total brain volume").addText(spec.totalVolume.toString())
                .setTextAlignment(TextAlignment.CENTER)
        document.add(reformatVolumeBlock(table))

        if (spec.seriesWholeVisualization != null) {
            document.add(createSectionHeader("Edema:"))
            document.add(createSeriesVisual(spec.seriesWholeVisualization))
            document.add(marginBlock)
            val tumorTable = Table(arrayOf(UnitValue.createPercentValue(50f), UnitValue.createPercentValue(50f)))
            tumorTable.addText("Whole tumor volume")
                    .addText(spec.tumorVolume.toString())
                    .addText("Tumor percentage compared to brain volume")
                    .addText("%.2f".format(spec.totalVolumeDiffPercent) + "%")
            document.add(reformatVolumeBlock(tumorTable))
            document.add(marginBlock)
        }

        if (spec.seriesTumorCoreVisualization != null) {
            document.add(createSectionHeader("Necrotic/cystic core:"))
            document.add(createSeriesVisual(spec.seriesTumorCoreVisualization))
            document.add(marginBlock)
            val coreTable = Table(arrayOf(UnitValue.createPercentValue(50f), UnitValue.createPercentValue(50f)))
            coreTable.addText("Necrotic/cystic core tumor volume")
                    .addText(spec.coreVolume.toString())
                    .addText("Percentage compared to brain volume")
                    .addText("%.2f".format(spec.coreVolumeDiffPercent) + "%")
            document.add(reformatVolumeBlock(coreTable))
            document.add(marginBlock)
        }

        if (spec.seriesEdemaVisualization != null) {
            document.add(createSectionHeader("Enhancing core:"))
            document.add(createSeriesVisual(spec.seriesEdemaVisualization))
            document.add(marginBlock)
            val edemaTable = Table(arrayOf(UnitValue.createPercentValue(50f), UnitValue.createPercentValue(50f)))
            edemaTable.addText("Enhancing core tumor volume")
                    .addText(spec.edemaVolume.toString())
                    .addText("Percentage compared to brain volume")
                    .addText("%.2f".format(spec.edemaVolumeDiffPercent) + "%")
            document.add(reformatVolumeBlock(edemaTable))
            document.add(marginBlock)
        }

        document.add(createConclusion())

        document.close()
        return PdfDocumentInfo(pdf, resultStream)
    }


    private fun reformatVolumeBlock(table: Table): Table {
        table.setTextAlignment(TextAlignment.CENTER)
                .setFontSize(11f)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setWidth(UnitValue.createPercentValue(100f))
                .setMargins(0f, 5f, 0f, 5f)
        return table
    }

    private fun createConclusion(): Paragraph {
        val result = Paragraph("Conclusion").setFontSize(16f)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setWidth(UnitValue.createPercentValue(100f))
                .setMargins(0f, 5f, 0f, 5f)
        return result
    }

    private fun createSeriesVisual(images: List<BufferedImage>?): IBlockElement {
        val result = Paragraph()
        for (image in images!!)
            result.addImg(image) { img ->
                img.setMargins(0f, 5f, 0f, 5f)
            }

        return result.setBackgroundColor(ColorConstants.BLACK).setTextAlignment(TextAlignment.CENTER)
    }


    private fun createHeader(): Paragraph {

        val header = Paragraph()
        header.setTextAlignment(TextAlignment.CENTER)

        header.width = UnitValue.createPercentValue(100f)
        header.setVerticalAlignment(VerticalAlignment.MIDDLE)


        val logo: Image = spec.companyImage.asPdfImage()
        logo.setRelativePosition(100f, 0f, 0f, 5f)

        if (DEBUG_SHOW_BORDERS)
            header.setBorder(SolidBorder(1f))

        val titleText = Text(TITLE).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)).setFontSize(18f)
        val title = Paragraph(titleText).setRelativePosition(logo.width.value / 2, 0f, 0f, 0f)
        header.add(title)
        header.add(logo)

        return header
    }

    private fun createSectionHeader(text: String): Paragraph {
        val header = Paragraph()
        header.setTextAlignment(TextAlignment.CENTER)

        header.width = UnitValue.createPercentValue(100f)
        header.setVerticalAlignment(VerticalAlignment.MIDDLE)
        val titleText = Text(text).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)).setFontSize(15f)
        val title = Paragraph(titleText)
        header.add(title)
        return header
    }

    private fun createScansDesc(): Table {

        if (spec.seriesDesc == null)
            throw Exception("scans description must not be null")

        val result = Table(arrayOf(UnitValue.createPercentValue(30f), UnitValue.createPercentValue(70f)))
                .setWidth(UnitValue.createPercentValue(100f))
                .setMargins(0f, 5f, 0f, 5f)

        val patientName = Text(spec.patientName)
        patientName.setFontSize(14f)
        patientName.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))

        val patientAge = Text(spec.patientAge)
        patientName.setFontSize(12f)
        patientName.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
        patientAge.setFontColor(ColorConstants.GRAY)

        val patientInfo = Paragraph().add(patientName).add("\n").add(patientAge)
        patientInfo.setHorizontalAlignment(HorizontalAlignment.LEFT)

        val patientCell = Cell().add(patientInfo).setBorder(Border.NO_BORDER)
        if (DEBUG_SHOW_BORDERS)
            patientCell.setBorder(SolidBorder(1f))

        result.addCell(patientCell)


        return result
    }

    companion object {
        const val TITLE = "Brain Tumor Segmentation Report"
        var DEBUG_SHOW_BORDERS = false
    }
}

private fun Paragraph.addImg(image: BufferedImage, decorator: (Image) -> Unit = {}): Paragraph {
    val stream = com.itextpdf.io.source.ByteArrayOutputStream()
    ImageIO.write(image, "png", stream)

    val pdfImage = Image(ImageDataFactory.create(stream.toByteArray()))
    decorator(pdfImage)

    this.add(pdfImage)
    return this
}

private fun Table.addText(value: String, decorator: ((Cell) -> Unit) = { }): Table {
    val cell = Cell().add(Paragraph(value))
    decorator(cell)

    this.addCell(cell)
    return this
}

