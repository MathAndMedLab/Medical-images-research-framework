package core.data.medimage;

import core.data.Data;
import java.util.List;

/**
 * ImageSeries stores a list of {@link MedImage}
 */
public class ImageSeriesData extends Data{

    //TODO: manage access to the fields after image model is chosen
    public List<MedImage> images;

    public ImageSeriesData(List<MedImage> images) {
        this.images = images;
    }
}
