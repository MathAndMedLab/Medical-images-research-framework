package features.reports.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;
import core.algorithm.Algorithm;
import core.data.medimage.ImageSeriesData;
import core.data.medimage.MedImage;
import features.reports.PdfElementData;
import features.repositoryaccessors.AlgorithmExecutionException;

import javax.imageio.ImageIO;
import java.util.stream.Collectors;

public class ImageSeriesToPdfElementConverter implements Algorithm<ImageSeriesData, PdfElementData> {

    @Override
    public PdfElementData execute(ImageSeriesData input) {

        var images = input.getImages().stream().map(MedImage::getImage).collect(Collectors.toList());
        var result = new Paragraph();
        try {
            for (var image : images) {
                var stream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", stream);

                var pdfImage = new Image(ImageDataFactory.create(stream.toByteArray()));
                pdfImage.setWidth(UnitValue.createPercentValue(50));
                pdfImage.setMargins(10,10,10,10);
                pdfImage.setHeight(UnitValue.createPercentValue(50));

                result.add(pdfImage);
            }
        } catch (Exception e) {
            throw new AlgorithmExecutionException(e);
        }

        return new PdfElementData(result);
    }
}
