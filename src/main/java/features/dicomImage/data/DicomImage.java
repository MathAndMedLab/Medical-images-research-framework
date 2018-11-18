package features.dicomImage.data;


import core.data.medImage.MedImage;
import core.data.medImage.MedImageAttribute;
import core.data.medImage.MirfAttributeCreator;
import core.data.medImage.MirfAttributes;

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
        if(!tags.contains(attribute))
            tags.add(attribute);
    }

    @Override
    public double getOnePixelVolume() {
        return (double)findTag(DicomAttributes.ONE_PIXEL_VOLUME.tag).value;
    }

    @Override
    public boolean isThresholdApplied() {
        MedImageAttribute threshold = findTag(MirfAttributes.THRESHOLDED.tag);
        return threshold != null && (boolean) threshold.value;
    }

    @Override
    public void setThresholded(boolean value) {
        MedImageAttribute thresholded = MirfAttributeCreator.createFromMock(MirfAttributes.THRESHOLDED, value);
        addAttribute(thresholded);
    }

    @Override
    public String getExtension() {
        return "DICOM";
    }
}
