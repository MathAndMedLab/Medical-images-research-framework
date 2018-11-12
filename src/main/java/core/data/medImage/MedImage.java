package core.data.medImage;

import core.data.Data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Data about medical image, including both pixels data and metadata.
 *
 * <p>Any particular type of medical image should extend MedImage
 */
public abstract class MedImage extends Data implements Cloneable{

    protected List<MedImageAttribute> tags;

    public MedImage(List<MedImageAttribute> tags) {
        this.tags = tags;
    }

    public abstract byte[][] getImagePixels();

    public abstract void setImagePixels(byte[][] pixels);

    protected MedImageAttribute findTag(String tag) {

        //TODO: (avlomakin) read more about java streams (LINQ C#)
        Optional<MedImageAttribute> result = tags.stream().filter(x -> x.tag.equals(tag)).findFirst();
        return result.orElse(null);
    }

    //TODO: (avlomakin) check clone patterns in java
    @Override
    public abstract MedImage clone();

    public abstract void addAttribute(MedImageAttribute attribute);

    public abstract double getOnePixelVolume();

    public abstract boolean isThresholdApplied();

    public abstract void setThresholded(boolean value);
}
