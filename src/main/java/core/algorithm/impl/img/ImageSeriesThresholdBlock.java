package core.algorithm.impl.img;

import core.algorithm.Algorithm;
import model.data.ImageSeries;
import model.data.MedImage;

import java.util.List;
import java.util.stream.Collectors;

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
        for (byte[] pixel : pixels)
            for (int i = 0; i < pixel.length; i++)
                pixel[i] = ((pixel[i] >= lowerBound) && (pixel[i] <= upperBound)) ? pixel[i] : 0;

        MedImage result = medImage.clone();
        result.setImagePixels(pixels);

        return result;
    }
}
