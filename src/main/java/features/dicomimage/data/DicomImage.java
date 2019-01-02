package features.dicomimage.data;


import core.data.AttributeCollection;
import core.data.medimage.MedImage;

import java.awt.image.BufferedImage;

/**
 * Dummy implementation of dicom image
 */
//TODO: (avlomakin) move to core.data
public class DicomImage extends MedImage {

    private BufferedImage pixelData;

    public DicomImage(BufferedImage pixelData) {
        this.pixelData = pixelData;
    }

    private DicomImage(AttributeCollection attributes, BufferedImage pixelData)
    {
        super(attributes);
        this.pixelData = pixelData;
    }

    @Override
    public BufferedImage getImage() {
        return pixelData;
    }

    @Override
    public void setImage(BufferedImage pixels) {
        pixelData = pixels;
    }

    @Override
    public DicomImage clone() {
        return new DicomImage(getAttributes().clone(), pixelData);
    }

    @Override
    public String getExtension() {
        return "DICOM";
    }
}
