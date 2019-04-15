package features.reports.pdf

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


fun BufferedImage.asPdfImage(saveOriginalSize: Boolean = true) : Image {

    val stream = com.itextpdf.io.source.ByteArrayOutputStream()
    ImageIO.write(this, "png", stream)

    val pdfImage = Image(ImageDataFactory.create(stream.toByteArray()))

    if (saveOriginalSize) {
        pdfImage.setWidth(this.width.toFloat() )
        pdfImage.setHeight(this.height.toFloat())
    }

    return pdfImage
}