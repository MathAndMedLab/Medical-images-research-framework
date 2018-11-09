package core.algorithm.impl.num;

import core.algorithm.Algorithm;
import model.data.Data;
import model.data.ImageSeries;
import model.data.MedImage;

import java.util.UUID;

public class ImageSeriesVoxelVolumeCalcAlg implements Algorithm<ImageSeries, Data> {

    private final UUID ONE_PIXEL_VOLUME_ID = UUID.fromString("34091644-e39a-11e8-9f32-f2801f1b9fd1");

    @Override
    public Data execute(ImageSeries input) {

        //TODO: (avlomakin) create table of well-known GUIDS
        double onePixelVolume = (double)(int)input.findTag(ONE_PIXEL_VOLUME_ID).value;

        double result = 0;
        for (MedImage image: input.images) {
            //how can i do it properly?
            for (byte[] line: image.getImagePixels()) {
                for (byte aLine : line) result += (aLine != 0) ? onePixelVolume : 0;
            }
        }
        Data data = new Data();
        data.data = result;

        return data;
    }
}
