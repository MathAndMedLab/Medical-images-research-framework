package pdfLayouts

import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import com.mirf.core.common.VolumeValue
import com.mirf.core.common.pickImages
import com.mirf.core.data.DataTable
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.medimage.getImageWithHighlightedSegmentation
import com.mirf.features.imagefilters.resize
import com.mirf.features.numinfofromimage.getVolume
import com.mirf.features.pdf.asPdfElement
import java.awt.image.BufferedImage
import java.time.LocalDate

class MsPdfReportParagraphsBuilder constructor(
        private val patient: PatientInfo,
        private val currentImageSeries: ImageSeries,
        private val currentMasks: ImageSeries,
        private val prevVolumeInfo: MsVolumeInfo) {

    fun build(): MsPdfReportSpec {

        val images = getSeriesVisualization()
        val desc = ComparedMsReportsDesc(
                MsScanInfo(LocalDate.now().minusYears(1)),
                MsScanInfo(LocalDate.now())
        )

        val currentVolume = getVolumeInfo(currentMasks)
        val (totalVolumeDiff, activeVolumeDiff) = currentVolume.getDiffPerc(prevVolumeInfo)
        val volumeTable = getVolumeTable(
                currentVolume.totalVolume,
                currentVolume.activeVolume,
                totalVolumeDiff,
                activeVolumeDiff)

        return MsPdfReportSpec.createMirfDefault(
                patientName = patient.name, patientAge = patient.age,
                seriesDesc = desc,
                seriesVisualization = images,
                volumeTable = volumeTable)
    }

    private fun getVolumeInfo(currentMasks: ImageSeries): MsVolumeInfo {
        return MsVolumeInfo(currentMasks.getVolume(), VolumeValue.zero)
    }

    private fun getVolumeTable(totalVolume: VolumeValue, activeVolume: VolumeValue,
                               totalVolumeDiffPercent: Double, activeVolumeDiffPercent: Double): Table {

        val table = DataTable(columns = hashSetOf("name", "value"),
                rows = arrayListOf(
                        hashMapOf("name" to "Total volume", "value" to totalVolume.toString(2)),
                        hashMapOf("name" to "Active volume", "value" to activeVolume.toString(2)),
                        hashMapOf("name" to "Total volume grow rate", "value" to totalVolumeDiffPercent.volumeDiffFormat()),
                        hashMapOf("name" to "Active volume grow rate", "value" to activeVolumeDiffPercent.volumeDiffFormat())
                )
        )

        val decorator = { x: Table ->
            x.setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14f)
                    .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
                    .setWidth(UnitValue.createPercentValue(100f))
                    .setMargins(0f, 5f, 0f, 5f)
        }

        return table.asPdfElement(decorator = decorator,
                displayHeaders = false)
    }

    fun Double.volumeDiffFormat(): String {
        return (if (this >= 0) "+ " else "") + String.format("%.2f", this) + "%"
    }

    private fun getSeriesVisualization(): Paragraph {
        currentImageSeries.applyMask(currentMasks)

        val picked = currentImageSeries.images.map { it.getImageWithHighlightedSegmentation() }.pickImages()

        val elementDecorator = { el: Paragraph ->
            el.setBackgroundColor(ColorConstants.BLACK).setTextAlignment(TextAlignment.CENTER)
        }
        val imgDecorator = { x: BufferedImage -> x/*.resize(144, 200)*/ }

        return picked.asPdfElement(imageDecorator = imgDecorator,
                elementDecorator = elementDecorator)
    }
}