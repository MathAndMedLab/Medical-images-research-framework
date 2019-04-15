package modules.ms

import com.itextpdf.layout.element.IBlockElement
import core.data.medimage.MedImage
import java.awt.image.BufferedImage
import java.time.LocalDateTime
import javax.imageio.ImageIO

data class MsPdfReportSpec(val companyImage: BufferedImage,
                           val reportCreationTime: LocalDateTime,
                           val patientName: String,
                           val patientAge: String,
                           val seriesDesc: ComparedMsReportsDesc?,
                           val seriesVisualization: List<BufferedImage>?,
                           val volume: IBlockElement?,
                           val footerIncluded: Boolean) {


    companion object {
        fun createMirfDefault(patientName: String,
                              patientAge: String = "no age info",
                              seriesDesc: ComparedMsReportsDesc? = null,
                              seriesVisualization: List<BufferedImage>? = null,
                              volume: IBlockElement? = null): MsPdfReportSpec {
            return MsPdfReportSpec(getMirfLogo(),
                    LocalDateTime.now(),
                    patientName,
                    patientAge = patientAge,
                    seriesDesc = seriesDesc,
                    seriesVisualization = seriesVisualization,
                    volume = volume,
                    footerIncluded = true)
        }

        private fun getMirfLogo(): BufferedImage {

            val logoPath = javaClass.getResource("/images/logo.png")
            return ImageIO.read(logoPath)
        }
    }
}
