package pdfLayouts

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


class BrainPdfReportCreatorTest {

    @Test
    fun checkLayout() {
        javaClass.getResource("/mask.nii") ?: return

        val spec = generateLayoutTestSpec()
        val doc = BrainPdfReportCreator(spec).createReport()

        val resultPath = Paths.get(javaClass.getResource("/").path, "/check_layout_result.pdf").toString()
        val file = File(resultPath)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeBytes(doc.stream.toByteArray())

        println("Done, file location - $resultPath")
    }

    private fun generateLayoutTestSpec(): BrainPdfReportSpec {
        val images = getSeriesVisualization()
        val desc = BrainReportsDesc(
                BrainScanInfo(LocalDate.now())
        )

        return BrainPdfReportSpec.createMirfDefault("John Doe", "63.y.o", desc, images)
    }

    private fun getSeriesVisualization(): List<BufferedImage> {
        val (seriesPath, masksPath) = getTestSeriesAndMask()
        val masks = Nifti1Reader.read(masksPath).asImageSeries()
        val series = Nifti1Reader.read(seriesPath).asImageSeries()
        series.applyMask(masks)
        return series.images.slice(listOf(89, 110, 120))
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

    private fun getTestSeriesAndMask(): Pair<String, String> {
        val series = javaClass.getResource("/t1.nii").path
        val masks = javaClass.getResource("/mask.nii").path

        return Pair(series, masks)
    }
}
