package pdfLayouts

import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import com.mirf.core.common.VolumeValue
import com.mirf.core.data.DataTable
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.medimage.MedImage
import com.mirf.core.data.medimage.getImageWithHighlightedSegmentation
import com.mirf.features.imagefilters.resize
import com.mirf.features.numinfofromimage.getVolume
import com.mirf.features.pdf.asPdfElementData
import com.mirf.features.reports.PdfElementData
import java.time.LocalDate

class MsPdfReportSpecBuilder constructor(
        private val patient: PatientInfo,
        private val currentImageSeries: ImageSeries,
        private val currentMasks: ImageSeries,
        private val prevVolumeInfo: MsVolumeInfo) {

    fun build(): MsPdfReportSpec {

        val images = getSeriesVisualization(listOf(140, 178, 200))
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
                               totalVolumeDiffPercent: Double, activeVolumeDiffPercent: Double): PdfElementData {

        val table = DataTable(columns = hashSetOf("name", "value"),
                rows = arrayListOf(
                        hashMapOf("name" to "Total volume", "value" to totalVolume.toString()),
                        hashMapOf("name" to "Active volume", "value" to activeVolume.toString()),
                        hashMapOf("name" to "Total volume grow rate", "value" to totalVolumeDiffPercent.toString()),
                        hashMapOf("name" to "Active volume grow rate", "value" to activeVolumeDiffPercent.toString())))

        val decorator = { x: Table ->
            x.setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14f)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setWidth(UnitValue.createPercentValue(100f))
                    .setMargins(0f, 5f, 0f, 5f)
        }

        return table.asPdfElementData(decorator = decorator,
                displayHeaders = false)
    }

    private fun getSeriesVisualization(slices: Iterable<Int>): PdfElementData {
        currentImageSeries.applyMask(currentMasks)

        val imageDecorator = { image: MedImage ->
            image.getImageWithHighlightedSegmentation().resize(144, 200)
        }
        val elementDecorator = { el: Paragraph ->
            el.setBackgroundColor(ColorConstants.BLACK).setTextAlignment(TextAlignment.CENTER)
        }

        return currentImageSeries.asPdfElementData(slices,
                imageDecorator = imageDecorator,
                elementDecorator = elementDecorator)
    }
}