package pdfLayouts

import com.mirf.core.common.VolumeValue
import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.medimage.getImageWithHighlightedSegmentation
import com.mirf.features.ij.asImageSeries
import com.mirf.features.nifti.util.Nifti1Reader
import org.junit.Test
import java.awt.image.BufferedImage
import java.io.File
import java.time.LocalDate
import java.awt.Image
import java.awt.Rectangle
import java.nio.file.Paths


class MsPdfReportCreatorTest {

    @Test
    fun checkLayout() {
        javaClass.getResource("/mask.nii") ?: return

        val (series, masks) = getSeriesAndMasks()
        val builder = MsPdfReportSpecBuilder(PatientInfo("John Doe", "27"),
                currentImageSeries = series,
                currentMasks = masks,
                prevVolumeInfo = MsVolumeInfo(totalVolume = VolumeValue.zero, activeVolume = VolumeValue.zero))

        val spec = builder.build()

        val doc = MsPdfReportCreator(spec).createReport()

        val resultPath = Paths.get(javaClass.getResource("/").path.removePrefix("/").removeSuffix("\\"), "check_layout_result.pdf").toString()
        val file = File(resultPath)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeBytes(doc.stream.toByteArray())

        println("Done, file location - $resultPath")
    }

    private fun getSeriesAndMasks(): Pair<ImageSeries, ImageSeries> {
        val (seriesPath, masksPath) = getTestSeriesAndMask()
        val masks = Nifti1Reader.read(masksPath).asImageSeries().also { it.attributes.add(MirfAttributes.THRESHOLDED.new(Unit)) }

        val series = Nifti1Reader.read(seriesPath).asImageSeries()
        return Pair(series, masks)
    }

    private fun getTestSeriesAndMask(): Pair<String, String> {
        val series = javaClass.getResource("/t1.nii").path
        val masks = javaClass.getResource("/mask.nii").path

        return Pair(series, masks)
    }
}
