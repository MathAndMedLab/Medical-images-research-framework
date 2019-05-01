package pdfLayouts

import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.attribute.Switch
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.medimage.getImageWithHighlightedSegmentation
import com.mirf.features.numinfofromimage.getVolume
import java.awt.Color
import java.awt.Image
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.time.LocalDate

class BrainPdfReportSpecBuilder constructor(
    private val patient: PatientInfo,
    private val currentImageSeries: ImageSeries,
    private val wholeMasks: ImageSeries,
    private val edemaMasks: ImageSeries,
    private val coreMasks: ImageSeries) {

    fun build() : BrainPdfReportSpec{

        val edemaImages = getSeriesVisualization(edemaMasks,
                Color(0x00,0x00,0xff, 128),
                listOf(70, 60, 69))
        val coreImages = getSeriesVisualization(coreMasks,
                Color(0xff,0x00,0x00, 128),
                listOf(70, 60, 69))
        val wholeImages = getSeriesVisualization(wholeMasks,
                Color(0x00, 0xff, 0x00, 128),
                listOf(70, 60, 69))

        val desc = BrainReportsDesc(
            BrainScanInfo(LocalDate.now())
        )

        val tumorVolume = wholeMasks.getVolume()
        val coreVolume = coreMasks.getVolume()
        val edemaVolume = edemaMasks.getVolume()
        val brainVolume = currentImageSeries.getVolume()

        val tumorPercentage = (tumorVolume.value / brainVolume.value) * 100
        val corePercentage = (coreVolume.value / brainVolume.value) * 100
        val edemaPercentage = (edemaVolume.value / brainVolume.value) * 100

        return BrainPdfReportSpec.createMirfDefault(
            patientName = patient.name, patientAge = patient.age,
            seriesDesc = desc,
            seriesWholeVisualization = wholeImages,
            seriesEdemaVisualization = edemaImages,
            seriesTumorCoreVisualization = coreImages,
            totalVolume = brainVolume,
            tumorVolume = tumorVolume,
            edemaVolume = edemaVolume,
            coreVolume = coreVolume,
            totalVolumeDiffPercent = tumorPercentage,
            edemaVolumeDiffPercent = edemaPercentage,
            coreVolumeDiffPercent = corePercentage)
    }


    private fun getSeriesVisualization(currentMasks: ImageSeries, hightlightColor: Color, slices: Iterable<Int>): List<BufferedImage> {
        var tempImg = currentImageSeries.clone() as ImageSeries
        tempImg.attributes.add(MirfAttributes.THRESHOLDED.new(Switch.get()))
        tempImg.applyMask(currentMasks)
        return tempImg.images.slice(slices)
            .map { x -> x.getImageWithHighlightedSegmentation(hightlightColor) }
            .map { cropImage(it, Rectangle(210, 210)) }
            .map { resizeImg(it, 130, 100) }
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