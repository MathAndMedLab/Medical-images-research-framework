package features.dicomimage.data;


import core.data.medimage.MedImage;
import core.data.medimage.MedImageAttribute;
import core.data.medimage.MirfAttributeCreator;
import core.data.medimage.MirfAttributes;

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
}
