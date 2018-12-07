package features.numinfofromimage;

import core.algorithm.Algorithm;
import core.data.ParametrizedData;
import core.data.attribute.MirfAttributes;
import core.data.medimage.ImageSeriesData;
import core.data.medimage.MedImage;
import features.dicomimage.data.DicomAttributes;

import java.awt.image.BufferedImage;

/**
 * Calculates the volume of the given {@link ImageSeriesData}.
 */
public class ImageSeriesVoxelVolumeCalcAlg implements Algorithm<ImageSeriesData, ParametrizedData> {

    @Override
    public ParametrizedData execute(ImageSeriesData input) {

        double result = 0;

        for (MedImage image : input.images) {
            //TODO: (avlomakin) replace with Log.Warning
            if (!image.attributes.hasAttribute(MirfAttributes.THRESHOLDED))
                System.out.println("Warning, performing volume calculation on non-thresholded image, possible unexpected result");

            double onePixelVolume = image.attributes.findAttributeValue(DicomAttributes.ONE_PIXEL_VOLUME);

            BufferedImage img = image.getImage();
            int[] raw = img.getRaster().getPixels(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight(), (int[]) null);

            for (int aLine : raw) result += (aLine != 0) ? onePixelVolume : 0;
        }
        return new ParametrizedData<>(result);
    }
}
