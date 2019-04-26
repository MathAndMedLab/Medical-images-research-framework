package pdfLayouts

import com.mirf.core.common.VolumeValue
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.medimage.getImageWithHighlightedSegmentation
import com.mirf.features.numinfofromimage.getVolume
import java.awt.Image
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.time.LocalDate

class MsPdfReportSpecBuilder constructor(
    private val patient: PatientInfo,
    private val currentImageSeries: ImageSeries,
    private val currentMasks: ImageSeries,
    private val prevVolumeInfo: MsVolumeInfo ) {

    fun build() : MsPdfReportSpec{

        val images = getSeriesVisualization(listOf(140, 178, 200))
        val desc = ComparedMsReportsDesc(
            MsScanInfo(LocalDate.now().minusYears(1)),
            MsScanInfo(LocalDate.now())
        )

        val currentVolume = getVolumeInfo(currentMasks)
        val (totalVolumeDiff, activeVolumeDiff) = currentVolume.getDiffPerc(prevVolumeInfo)

        return MsPdfReportSpec.createMirfDefault(
            patientName = patient.name, patientAge = patient.age,
            seriesDesc = desc,
            seriesVisualization = images,
            totalVolume = currentVolume.totalVolume, activeVolume = currentVolume.activeVolume,
            totalVolumeDiffPercent = totalVolumeDiff, activeVolumeDiffPercent = activeVolumeDiff )
    }

    private fun getVolumeInfo(currentMasks: ImageSeries): MsVolumeInfo {
        return MsVolumeInfo(currentMasks.getVolume(), VolumeValue.zero)
    }

    private fun getSeriesVisualization(slices: Iterable<Int>): List<BufferedImage> {
        currentImageSeries.applyMask(currentMasks)
        return currentImageSeries.images.slice(slices)
            .map { x -> x.getImageWithHighlightedSegmentation() }
            //.map { cropImage(it, Rectangle(144, 350)) }
            .map { resizeImg(it, 144, 250) }
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
        return src.getSubimage(0, src.height / 4, rect.width, rect.height)
    }

}