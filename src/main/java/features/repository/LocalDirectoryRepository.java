package features.repository;

import core.data.medimage.ImageSeries;
import core.data.medimage.MedImage;
import core.data.medimage.MirfAttribute;
import core.data.medimage.MirfAttributeCreator;
import core.repository.Repository;
import features.dicomimage.data.DicomAttributes;
import features.dicomimage.data.DicomImage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Local file system commander, Link = path on a filesystem
 */
public class LocalDirectoryRepository implements Repository {

    private static final ImageSeries DummySeries;

    static {
        ArrayList<MirfAttribute> tags = new ArrayList<MirfAttribute>() {{
            add(MirfAttributeCreator.createFromMock(DicomAttributes.ONE_PIXEL_VOLUME, 2.0));
        }};

        ArrayList<MedImage> dicoms = new ArrayList<MedImage>() {{
            add(new DicomImage(tags, new byte[][]{
                    {1, 2},
                    {3, 4}
            }));
        }};

        DummySeries = new ImageSeries(null, dicoms);
    }
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

    @Override
    public void saveFile(byte[] file, String link, String filename) {
        try {
            FileOutputStream stream = new FileOutputStream(link + "/" + filename);
            stream.write(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
