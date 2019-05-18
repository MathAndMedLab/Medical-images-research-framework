package pdfLayouts

import com.itextpdf.layout.element.IBlockElement
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.mirf.core.common.VolumeValue
import com.mirf.core.data.DataTable
import com.mirf.features.reports.PdfElementData
import java.awt.image.BufferedImage
import java.time.LocalDateTime
import javax.imageio.ImageIO

data class MsPdfReportSpec(val companyImage: BufferedImage,
                           val reportCreationTime: LocalDateTime,
                           val patientName: String,
                           val patientAge: String,
                           val seriesDesc: ComparedMsReportsDesc,
                           val seriesVisualization: Paragraph,
                           val volumeTable: Table,
                           val totalVolume: VolumeValue,
                           val footerIncluded: Boolean) {


    companion object {
        fun createMirfDefault(patientName: String,
                              patientAge: String = "no age info",
                              seriesDesc: ComparedMsReportsDesc,
                              seriesVisualization: Paragraph,
                              volumeTable: Table,
                              totalVolume: VolumeValue = VolumeValue.zero): MsPdfReportSpec {
            return MsPdfReportSpec(
                    getMirfLogo(),
                    LocalDateTime.now(),
                    patientName,
                    patientAge = patientAge,
                    seriesDesc = seriesDesc,
                    seriesVisualization = seriesVisualization,
                    volumeTable = volumeTable,
                    totalVolume = totalVolume,
                    footerIncluded = true
            )
        }

        private fun getMirfLogo(): BufferedImage {

            val logoPath = javaClass.getResource("/images/logo.png")
            return ImageIO.read(logoPath)
        }
    }
}
