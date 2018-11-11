package features.numInfoFromImage;

import core.algorithm.Algorithm;
import core.data.Data;
import core.data.medImage.ImageSeries;
import core.data.medImage.MedImage;

import java.util.Arrays;
import java.util.UUID;

/**
 * Calculates the volume of the given {@link ImageSeries}.
 */
public class ImageSeriesVoxelVolumeCalcAlg implements Algorithm<ImageSeries, Data> {

    private final String ONE_PIXEL_VOLUME_ID = "34091644-e39a-11e8-9f32-f2801f1b9fd1";

    @Override
    public Data execute(ImageSeries input) {

        //TODO: (avlomakin) create table of well-known GUIDS
        double onePixelVolume = (double)(int)input.findTag(ONE_PIXEL_VOLUME_ID).value;

        double result = 0;
        for (MedImage image: input.images) {
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
