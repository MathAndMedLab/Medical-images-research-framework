package core.data.medimage;

import core.data.AttributeCollection;
import core.data.Data;

import java.awt.image.BufferedImage;

/**
 * Data about medical image, including both pixels data and metadata.
 *
 * <p>Any particular type of medical image should extend MedImage
 */
public abstract class MedImage extends Data implements Cloneable {

    public abstract BufferedImage getImage();

    protected MedImage() {
    }

    protected MedImage(AttributeCollection attributes) {
        super(attributes);
    }

    public abstract void setImage(BufferedImage pixels);

    /**
     * Retrieves file extension of MedImage
     * @return extension
     */
    public abstract String getExtension();

    public abstract MedImage clone();
}
