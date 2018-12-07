package features.dicomimage.data;


import core.data.AttributeCollection;
import core.data.medimage.MedImage;
import core.data.attribute.DataAttribute;
import core.data.attribute.DataAttributeCreator;
import core.data.attribute.MirfAttributes;

import java.awt.image.BufferedImage;
import java.util.List;

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
        return new DicomImage(attributes.clone(), pixelData);
    }

    @Override
    public String getExtension() {
        return "DICOM";
    }
}
