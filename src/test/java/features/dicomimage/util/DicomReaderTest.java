package features.dicomimage.util;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.TagFromName;
import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.List;

public class DicomReaderTest {

    @Test
    public void readDicomImageAttributesFromLocalFile_localDicom_readsWithoutErrors() {
        String dicomInputFile = "src/test/resources/exampleDicom.dcm";
        DicomReader dicomReader = new DicomReader();
        AttributeList list = dicomReader.readDicomImageAttributesFromLocalFile(dicomInputFile);

        // Assert that tags are in correspondence with what is expected from file
        Assert.assertEquals("1.2.840.10008.1.2.1", getTagInformation(list, TagFromName.TransferSyntaxUID));
        Assert.assertEquals("1", getTagInformation(list, TagFromName.SamplesPerPixel));
        Assert.assertTrue(list.get(TagFromName.PixelData) != null);
    }

    @Test
    public void readDicomImagePixelDataFromLocalFile_localDicom_readsWithoutErrors() {
        String dicomInputFile = "src/test/resources/exampleDicom.dcm";
        DicomReader dicomReader = new DicomReader();
        List<BufferedImage> bufferedImages = DicomReader.readDicomImagePixelDataFromLocalFile(dicomInputFile);

        // Just check the size of images array
        Assert.assertEquals(16, bufferedImages.size());
    }

    private static String getTagInformation(AttributeList list, AttributeTag attrTag) {
        return Attribute.getDelimitedStringValuesOrEmptyString(list, attrTag);
    }
}