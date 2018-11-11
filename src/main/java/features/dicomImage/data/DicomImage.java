package features.dicomImage.data;


import core.data.medImage.MedImage;
import core.data.medImage.MedImageAttribute;

import java.util.List;

/**
 * Dummy implementation of dicom image
 */
//TODO: (avlomakin) move to core.data

public class DicomImage extends MedImage {

    private byte[][] pixelData;

    public DicomImage(List<MedImageAttribute> tags, byte[][] pixelData) {
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
    public DicomImage clone() {
        return new DicomImage(tags, pixelData);
    }

    @Override
    public void addAttribute(MedImageAttribute attribute) {

    }

    @Override
    public double getOnePixelVolume() {
        return 0;
    }

    @Override
    public boolean isThresholdApplied() {
        return false;
    }
}
