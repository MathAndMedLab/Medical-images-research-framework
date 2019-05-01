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
                              val seriesWholeVisualization: List<BufferedImage>?,
                              val seriesEdemaVisualization: List<BufferedImage>?,
                              val seriesTumorCoreVisualization: List<BufferedImage>?,
                              val totalVolume: VolumeValue,
                              val tumorVolume: VolumeValue,
                              val edemaVolume: VolumeValue,
                              val coreVolume: VolumeValue,
                              val totalVolumeDiffPercent: Double,
                              val edemaVolumeDiffPercent: Double,
                              val coreVolumeDiffPercent: Double,
                              val footerIncluded: Boolean) {


    companion object {
        fun createMirfDefault(patientName: String,
                              patientAge: String = "no age info",
                              seriesDesc: BrainReportsDesc? = null,
                              seriesWholeVisualization: List<BufferedImage>? = null,
                              seriesEdemaVisualization: List<BufferedImage>? = null,
                              seriesTumorCoreVisualization: List<BufferedImage>? = null,
                              totalVolume: VolumeValue = VolumeValue.zero,
                              tumorVolume: VolumeValue = VolumeValue.zero,
                              edemaVolume: VolumeValue = VolumeValue.zero,
                              coreVolume: VolumeValue = VolumeValue.zero,
                              totalVolumeDiffPercent: Double = 0.0,
                              edemaVolumeDiffPercent: Double = 0.0,
                              coreVolumeDiffPercent: Double = 0.0): BrainPdfReportSpec {
            return BrainPdfReportSpec(
                getMirfLogo(),
                LocalDateTime.now(),
                patientName,
                patientAge = patientAge,
                seriesDesc = seriesDesc,
                seriesWholeVisualization = seriesWholeVisualization,
                seriesEdemaVisualization = seriesEdemaVisualization,
                seriesTumorCoreVisualization = seriesTumorCoreVisualization,
                totalVolume = totalVolume,
                tumorVolume = tumorVolume,
                edemaVolume = edemaVolume,
                coreVolume = coreVolume,
                totalVolumeDiffPercent = totalVolumeDiffPercent,
                edemaVolumeDiffPercent = edemaVolumeDiffPercent,
                coreVolumeDiffPercent = coreVolumeDiffPercent,
                footerIncluded = true
            )
        }

        private fun getMirfLogo(): BufferedImage {

            val logoPath = javaClass.getResource("/images/mirf.png")
            return ImageIO.read(logoPath)
        }
    }
}
