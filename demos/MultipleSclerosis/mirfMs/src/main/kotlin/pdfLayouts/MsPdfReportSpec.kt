package pdfLayouts

import com.itextpdf.layout.element.IBlockElement
import com.mirf.core.common.VolumeValue
import java.awt.image.BufferedImage
import java.time.LocalDateTime
import javax.imageio.ImageIO

data class MsPdfReportSpec(val companyImage: BufferedImage,
                           val reportCreationTime: LocalDateTime,
                           val patientName: String,
                           val patientAge: String,
                           val seriesDesc: ComparedMsReportsDesc?,
                           val seriesVisualization: List<BufferedImage>?,
                           val totalVolume: VolumeValue,
                           val activeVolume: VolumeValue,
                           val totalVolumeDiffPercent: Double,
                           val activeVolumeDiffPercent: Double,
                           val footerIncluded: Boolean) {


    companion object {
        fun createMirfDefault(patientName: String,
                              patientAge: String = "no age info",
                              seriesDesc: ComparedMsReportsDesc? = null,
                              seriesVisualization: List<BufferedImage>? = null,
                              totalVolume: VolumeValue = VolumeValue.zero,
                              activeVolume: VolumeValue = VolumeValue.zero,
                              totalVolumeDiffPercent: Double = 0.0,
                              activeVolumeDiffPercent: Double = 0.0): MsPdfReportSpec {
            return MsPdfReportSpec(
                getMirfLogo(),
                LocalDateTime.now(),
                patientName,
                patientAge = patientAge,
                seriesDesc = seriesDesc,
                seriesVisualization = seriesVisualization,
                totalVolume = totalVolume,
                activeVolume = activeVolume,
                totalVolumeDiffPercent = totalVolumeDiffPercent,
                activeVolumeDiffPercent = activeVolumeDiffPercent,
                footerIncluded = true
            )
        }

        private fun getMirfLogo(): BufferedImage {

            val logoPath = javaClass.getResource("/images/logo.png")
            return ImageIO.read(logoPath)
        }
    }
}
