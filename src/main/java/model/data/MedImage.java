package model.data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class MedImage extends Data implements Cloneable{

    protected List<MedImageTag> tags;

    public MedImage(List<MedImageTag> tags) {
        this.tags = tags;
    }

    public abstract byte[][] getImagePixels();

    public abstract void setImagePixels(byte[][] pixels);

    public MedImageTag findTag(UUID tagId) {

        //TODO: (avlomakin) read more about java streams (LINQ C#)
        Optional<MedImageTag> result = tags.stream().filter(x -> x.id == tagId).findFirst();
        return result.orElse(null);
    }

    //TODO: (avlomakin) check clone patterns in java
    @Override
    public abstract MedImage clone() throws CloneNotSupportedException;

    //TODO: (avlomakin) create MedImage wrapper for tags access( IsThresholdApplied() {return findTag(MedImageTag.THRESHOLD))
}
