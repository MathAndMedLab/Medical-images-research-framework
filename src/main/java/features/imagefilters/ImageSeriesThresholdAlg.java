package features.imagefilters;

import core.algorithm.Algorithm;
import core.data.attribute.MirfAttributes;
import core.data.attribute.Switch;
import core.data.medimage.ImageSeriesData;
import core.data.medimage.MedImage;

import java.awt.image.BufferedImage;

/**
 * Filters the pixels of all images provided in {@link ImageSeriesData} to be within specified boundaries.
 */
public class ImageSeriesThresholdAlg implements Algorithm<ImageSeriesData, ImageSeriesData> {
    private byte upperBound;
    private byte lowerBound;

    public ImageSeriesThresholdAlg(byte lowerBound, byte upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public ImageSeriesData execute(ImageSeriesData input) {

        input.images.forEach(this::thresholdImage);
        //List<MedImage> newImages = input.images.stream().map(this::thresholdImage).collect(Collectors.toList());

        return input;
    }

    private MedImage thresholdImage(MedImage medImage){

        //TODO: (avlomakin) add MedImage cloning if required

        BufferedImage img = medImage.getImage();

        int[] raw = img.getRaster().getPixels(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight(), (int[]) null);
        for (int i = 0; i < raw.length; i++)
                raw[i] = ((raw[i] >= lowerBound) && (raw[i] <= upperBound)) ? raw[i] : 0;

        img.getRaster().setPixels(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight(), raw);

        medImage.attributes.add(MirfAttributes.THRESHOLDED, Switch.instance);

        return medImage;
    }
}
