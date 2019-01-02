package features.numinfofromimage;

import core.algorithm.Algorithm;
import core.data.ParametrizedData;
import core.data.attribute.MirfAttributes;
import core.data.medimage.ImageSeriesData;
import features.dicomimage.data.DicomAttributes;

/**
 * Calculates the volume of the given {@link ImageSeriesData}.
 */
public class ImageSeriesVoxelVolumeCalcAlg implements Algorithm<ImageSeriesData, ParametrizedData> {

    @Override
    public ParametrizedData execute(ImageSeriesData input) {

        double result = 0;

        for (var image : input.getImages()) {
            //TODO: (avlomakin) replace with Log.Warning
            if (!image.getAttributes().hasAttribute(MirfAttributes.INSTANCE.getTHRESHOLDED()))
                System.out.println("Warning, performing volume calculation on non-thresholded image, possible unexpected result");

            double onePixelVolume = image.getAttributes().findAttributeValue(DicomAttributes.ONE_PIXEL_VOLUME);

            var img = image.getImage();
            var raw = img.getRaster().getPixels(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight(), (int[]) null);

            for (var aLine : raw) result += (aLine != 0) ? onePixelVolume : 0;
        }
        return new ParametrizedData<>(result);
    }
}
