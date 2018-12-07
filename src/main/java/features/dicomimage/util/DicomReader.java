package features.dicomimage.util;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomInputStream;
import com.pixelmed.display.ConsumerFormatImageMaker;
import features.dicomimage.data.DicomAttributes;
import features.dicomimage.data.DicomImage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Holds different methods for reading Dicom images.
 */
public class DicomReader {

    // TODO(sabrinamusatian): migrate from pixelmed.dicom.AttributeList to MedImageAttribute

    /**
     * Reads Dicom image attributes from a given local path.
     *
     * @param dicomInputFile a path to the dicom image on disk.
     * @return list with dicom attributes;
     */
    public static AttributeList readDicomImageAttributesFromLocalFile(String dicomInputFile) {
        AttributeList dicomAttributes = new AttributeList();
        try {
            dicomAttributes.read(dicomInputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dicomAttributes;
    }

    public static DicomImage readDicomImage(InputStream input)
    {
        //TODO: (avlomakin) rewrite it!!!!!!!!!!!!!!!!!!
        try {
            DicomInputStream dicomStream = new DicomInputStream(input);
            AttributeList dicomAttributes = new AttributeList();

            dicomAttributes.read(dicomStream);
            List<BufferedImage> images = readDicomImagePixelDataFromAttributeList(dicomAttributes);

            DicomImage image = new DicomImage(images.get(0));
            image.attributes.add(DicomAttributes.ONE_PIXEL_VOLUME, 2.0);

            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads only pixel data of a Dicom image from a given {@link AttributeList}.
     *
     * @param attributeList a list of already read dicom attributes.
     * @return list with pixel data, e.g. list of slices in dicom image.
     */
    public static List<BufferedImage> readDicomImagePixelDataFromAttributeList(AttributeList attributeList) {
        ArrayList<BufferedImage> pixelData = new ArrayList<>();
        try {
            BufferedImage[] imgs = ConsumerFormatImageMaker.makeEightBitImages(attributeList);
            pixelData = new ArrayList<>(Arrays.asList(imgs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pixelData;
    }

    /**
     * Reads only pixel data of a Dicom image from a from a given local path.
     *
     * @param dicomInputFile a path to the dicom image on disk.
     * @return list with pixel data, e.g. list of slices in dicom image.
     */
    public static List<BufferedImage> readDicomImagePixelDataFromLocalFile(String dicomInputFile) {
        AttributeList attributeList = readDicomImageAttributesFromLocalFile(dicomInputFile);
        return readDicomImagePixelDataFromAttributeList(attributeList);
    }

}
