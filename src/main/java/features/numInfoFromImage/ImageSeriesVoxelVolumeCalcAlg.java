package features.numInfoFromImage;

import core.algorithm.Algorithm;
import core.data.Data;
import core.data.medImage.ImageSeries;
import core.data.medImage.MedImage;
import features.dicomImage.data.DicomAttributes;

import java.util.Arrays;
import java.util.UUID;

/**
 * Calculates the volume of the given {@link ImageSeries}.
 */
public class ImageSeriesVoxelVolumeCalcAlg implements Algorithm<ImageSeries, Data> {

    @Override
    public Data execute(ImageSeries input) {

        double result = 0;

        for (MedImage image: input.images) {

            //TODO: (avlomakin) replace with Log.Warning
            if(!image.isThresholdApplied())
                System.out.println("Warning, performing volume calculation on non-thresholded image, possible unexpected result");

            double onePixelVolume = image.getOnePixelVolume();
            // TODO(sabrinamusatian): rewrite it using Arrays.stream
            for (byte[] line: image.getImagePixels()) {
                for (byte aLine : line) result += (aLine != 0) ? onePixelVolume : 0;
            }
        }

        Data data = new Data();
        data.data = result;

        return data;
    }
}
