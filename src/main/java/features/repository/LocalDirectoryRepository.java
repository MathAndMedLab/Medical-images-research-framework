package features.repository;

import core.data.medimage.ImageSeries;
import core.data.medimage.MedImage;
import core.data.medimage.MedImageAttribute;
import core.data.medimage.MirfAttributeCreator;
import core.repository.Repository;
import features.dicomimage.data.DicomAttributes;
import features.dicomimage.data.DicomImage;

import java.util.ArrayList;

/**
 * Local file system commander, Link = path on a filesystem
 */
public class LocalDirectoryRepository implements Repository {

    private static final ImageSeries DummySeries = createDummy();

    /**
     * Reads all medical images from directory
     *
     * @param link path to the directory
     * @return collected ImageSeries
     */
    //TODO: (avlomakin) implement on next iteration
    @Override
    public ImageSeries getImageSeries(String link) {
        return DummySeries;
    }

    private static ImageSeries createDummy() {
        ArrayList<MedImageAttribute> tags = new ArrayList<MedImageAttribute>() {{
            add(MirfAttributeCreator.createFromMock(DicomAttributes.ONE_PIXEL_VOLUME, 2.0));
        }};

        ArrayList<MedImage> dicoms = new ArrayList<MedImage>() {{
            add(new DicomImage(tags, new byte[][]{
                    {1, 2},
                    {3, 4}
            }));
        }};

        return new ImageSeries(null, dicoms);
    }

}
