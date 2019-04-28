package pdfLayouts

import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.medimage.getImageWithHighlightedSegmentation
import com.mirf.features.numinfofromimage.getVolume
import java.awt.Image
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.time.LocalDate

class BrainPdfReportSpecBuilder constructor(
    private val patient: PatientInfo,
    private val currentImageSeries: ImageSeries,
    private val currentMasks: ImageSeries) {

    fun build() : BrainPdfReportSpec{

        val images = getSeriesVisualization(listOf(89, 110, 96))
        val desc = BrainReportsDesc(
            BrainScanInfo(LocalDate.now())
        )

        val tumorVolume = currentMasks.getVolume()
        val brainVolume = currentImageSeries.getVolume()

        val tumorPercentage = (tumorVolume.value / brainVolume.value) * 100

        return BrainPdfReportSpec.createMirfDefault(
            patientName = patient.name, patientAge = patient.age,
            seriesDesc = desc,
            seriesVisualization = images,
            totalVolume = brainVolume, activeVolume = tumorVolume,
            totalVolumeDiffPercent = tumorPercentage)
    }


    private fun getSeriesVisualization(slices: Iterable<Int>): List<BufferedImage> {
        currentImageSeries.applyMask(currentMasks)
        return currentImageSeries.images.slice(slices)
            .map { x -> x.getImageWithHighlightedSegmentation() }
            .map { cropImage(it, Rectangle(210, 210)) }
            .map { resizeImg(it, 150, 150) }
    }

    private fun resizeImg(img: BufferedImage, newW: Int, newH: Int): BufferedImage {
        val tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH)
        val dimg = BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB)

        val g2d = dimg.createGraphics()
        g2d.drawImage(tmp, 0, 0, null)
        g2d.dispose()

        return dimg
    }

    private fun cropImage(src: BufferedImage, rect: Rectangle): BufferedImage {
        return src.getSubimage(0, src.height / 8, rect.width, rect.height)
    }

}