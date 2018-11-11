package features.dicomImage.data;


import core.data.medImage.MedImage;
import core.data.medImage.MedImageTag;

import java.util.List;

/**
 * Dummy implementation of dicom image
 */
//TODO: (avlomakin) move to core.data

public class DicomImage extends MedImage {

    private byte[][] pixelData;

    public DicomImage(List<MedImageTag> tags, byte[][] pixelData) {
        super(tags);
        this.pixelData = pixelData;
    }

    @Override
    public byte[][] getImagePixels() {
        return pixelData;
    }

    @Override
    public void setImagePixels(byte[][] pixels) {
        pixelData = pixels;
    }

    @Override
    public DicomImage clone() throws CloneNotSupportedException {
        return new DicomImage(tags, pixelData);
    }
}
