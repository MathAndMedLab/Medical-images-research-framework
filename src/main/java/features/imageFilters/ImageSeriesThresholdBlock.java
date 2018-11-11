package features.imageFilters;

import core.algorithm.Algorithm;
import core.data.medImage.ImageSeries;
import core.data.medImage.MedImage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filters the pixels of all images provided in {@link ImageSeries} to be within specified boundaries.
 */
public class ImageSeriesThresholdBlock implements Algorithm<ImageSeries, ImageSeries> {

    private byte upperBound;
    private byte lowerBound;

    public ImageSeriesThresholdBlock(byte lowerBound, byte upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public ImageSeries execute(ImageSeries input) {
        //TODO: (avlomakin) read about checked and unchecked exceptions (with lambdas)

        List<MedImage> newImages = input.images.stream().map(medImage -> {
            try {
                return thresholdImage(medImage);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        return new ImageSeries(input.tags, newImages);
    }

    private MedImage thresholdImage(MedImage medImage) throws CloneNotSupportedException {
        byte[][] pixels = medImage.getImagePixels();

        //TODO: (avlomakin) read about value-type arrays (RLY ??????????? arrays != collections?????????????)
        // arrays are storages of fixed size, while collections are extensible. Like arrays and vectors in C++
        for (byte[] pixel : pixels)
            for (int i = 0; i < pixel.length; i++)
                pixel[i] = ((pixel[i] >= lowerBound) && (pixel[i] <= upperBound)) ? pixel[i] : 0;

        MedImage result = medImage.clone();
        result.setImagePixels(pixels);

        return result;
    }
}
