package features.dicomImage.algo;

import com.pixelmed.display.BufferedImageUtilities;
import core.algorithm.Algorithm;
import com.pixelmed.display.SourceImage;
import com.pixelmed.dicom.AttributeList;

import java.awt.image.BufferedImage;

/**
 * Reads DICOM image
 */
// TODO(musatian): make it an algorithm implementation, so it will return instance of DICOM image
public class DicomReader {
    private BufferedImage dicomPixelData;
    private AttributeList dicomAttributes = new AttributeList();

    private void readDicomImage(String dicomInputFile) {
        try {
            dicomAttributes.read(dicomInputFile);
            SourceImage sImg = new SourceImage(dicomAttributes);
            // TODO(musatian): implement reading of all buffered images in case of multiple stack
            dicomPixelData = sImg.getBufferedImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
