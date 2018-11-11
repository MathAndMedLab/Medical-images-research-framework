package core.repository;

import core.data.medImage.MedImageAttribute;
import features.dicomImage.data.DicomImage;
import core.data.medImage.ImageSeries;
import core.data.medImage.MedImage;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Local file system commander, Link = path on a filesystem
 */
public class LocalDirectoryRepository implements Repository {

    /**
     * Reads all medical images from directory
     * @param link path to the directory
     * @return collected ImageSeries
     */
    //TODO: (avlomakin) implement on next iteration
    @Override
    public ImageSeries getImageSeries(String link) {
        return DummySeries;
    }

    private static final ImageSeries DummySeries = CreateDummy();

    private static ImageSeries CreateDummy() {
        ArrayList<MedImageAttribute> tags = new ArrayList<MedImageAttribute>() {{
            //TODO: (avlomakin) uncomment and modify
           // add(new MedImageAttribute("onepixelvolume", UUID.fromString("34091644-e39a-11e8-9f32-f2801f1b9fd1"), tagType, 2, valueRepresentation));
        }};
        ArrayList<MedImage> dicoms = new ArrayList<MedImage>() {{
            add(new DicomImage(tags, new byte[][]{
                    {1, 2},
                    {3, 4}
            }));
        }};

        return new ImageSeries(tags, dicoms);
    }

}
