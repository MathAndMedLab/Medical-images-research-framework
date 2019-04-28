package pdfLayouts

import com.mirf.core.common.VolumeValue
import java.awt.image.BufferedImage
import java.time.LocalDateTime
import javax.imageio.ImageIO

data class BrainPdfReportSpec(val companyImage: BufferedImage,
                              val reportCreationTime: LocalDateTime,
                              val patientName: String,
                              val patientAge: String,
                              val seriesDesc: BrainReportsDesc?,
                              val seriesVisualization: List<BufferedImage>?,
                              val totalVolume: VolumeValue,
                              val activeVolume: VolumeValue,
                              val totalVolumeDiffPercent: Double,
                              val activeVolumeDiffPercent: Double,
                              val footerIncluded: Boolean) {


    companion object {
        fun createMirfDefault(patientName: String,
                              patientAge: String = "no age info",
                              seriesDesc: BrainReportsDesc? = null,
                              seriesVisualization: List<BufferedImage>? = null,
                              totalVolume: VolumeValue = VolumeValue.zero,
                              activeVolume: VolumeValue = VolumeValue.zero,
                              totalVolumeDiffPercent: Double = 0.0,
                              activeVolumeDiffPercent: Double = 0.0): BrainPdfReportSpec {
            return BrainPdfReportSpec(
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

            val logoPath = javaClass.getResource("/images/mirf.png")
            return ImageIO.read(logoPath)
        }
    }
}
