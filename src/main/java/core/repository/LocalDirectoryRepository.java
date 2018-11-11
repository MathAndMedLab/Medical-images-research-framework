package core.repository;

import features.dicomImage.data.DicomImage;
import core.data.medImage.ImageSeries;
import core.data.medImage.MedImage;
import core.data.medImage.MedImageTag;

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
        ArrayList<MedImageTag> tags = new ArrayList<MedImageTag>() {{
            add(new MedImageTag("onepixelvolume", UUID.fromString("34091644-e39a-11e8-9f32-f2801f1b9fd1"), 2));
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
