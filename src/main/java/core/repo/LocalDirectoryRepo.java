package core.repo;

import model.data.DicomImage;
import model.data.ImageSeries;
import model.data.MedImage;
import model.data.MedImageTag;
import model.repo.Repo;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Link = path on a filesystem
 */
public class LocalDirectoryRepo implements Repo {

    //TODO: (avlomakin) implement on next iteration
    @Override
    public ImageSeries GetImageSeries(String link) {
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
