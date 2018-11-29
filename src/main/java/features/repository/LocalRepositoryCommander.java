package features.repository;

import core.data.medimage.ImageSeries;
import core.data.medimage.MedImage;
import core.data.medimage.DataAttribute;
import core.data.medimage.DataAttributeCreator;
import core.repository.RepositoryCommander;
import features.dicomimage.data.DicomAttributes;
import features.dicomimage.data.DicomImage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Local file system commander, Link = path on a filesystem
 */
public class LocalRepositoryCommander implements RepositoryCommander {

    private static final ImageSeries DummySeries;

    static {
        ArrayList<DataAttribute> tags = new ArrayList<DataAttribute>() {{
            add(DataAttributeCreator.createFromMock(DicomAttributes.ONE_PIXEL_VOLUME, 2.0));
        }};

        ArrayList<MedImage> dicoms = new ArrayList<MedImage>() {{
            add(new DicomImage(tags, new byte[][]{
                    {1, 2},
                    {3, 4}
            }));
        }};

        DummySeries = new ImageSeries(null, dicoms);
    }

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
