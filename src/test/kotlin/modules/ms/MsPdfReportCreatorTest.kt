package modules.ms


import core.data.medimage.getImageWithHighlightedSegmentation
import features.ij.asImageSeries
import features.nifti.util.Nifti1Reader
import org.junit.Test
import java.awt.image.BufferedImage
import java.io.File
import java.time.LocalDate
import java.awt.Image
import java.awt.Rectangle


class MsPdfReportCreatorTest {

    @Test
    fun checkLayout() {
        javaClass.getResource("/msReport/mask.nii") ?: return

        val spec = generateLayoutTestSpec()
        val doc = MsPdfReportCreator(spec).createReport()

        val file = File("c:/src/mirf/test_report.pdf")
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeBytes(doc.stream.toByteArray())

        println("Done")
    }

    private fun generateLayoutTestSpec(): MsPdfReportSpec {
        val images = getSeriesVisualization()
        val desc = ComparedMsReportsDesc(MsScanInfo(LocalDate.now().minusYears(1)), MsScanInfo(LocalDate.now()))

        return MsPdfReportSpec.createMirfDefault("John Doe", "63.y.o", desc, images)
    }

    private fun getSeriesVisualization(): List<BufferedImage> {
        val (seriesPath, masksPath) = getTestSeriesAndMask()
        val masks = Nifti1Reader.read(masksPath).asImageSeries()
        val series = Nifti1Reader.read(seriesPath).asImageSeries()
        series.applyMask(masks)
        return series.images.slice(listOf(284, 316, 366))
                .map { x -> x.getImageWithHighlightedSegmentation() }
                .map { cropImage(it, Rectangle(144, 350)) }
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

    private fun getTestSeriesAndMask(): Pair<String, String> {
        val series = javaClass.getResource("/msReport/t1_p.nii").path
        val masks = javaClass.getResource("/msReport/mask.nii").path

        return Pair(series, masks)
    }
}
