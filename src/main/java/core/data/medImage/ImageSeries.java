package core.data.medImage;

import core.data.Data;

import java.util.*;

/**
 * ImageSeries stores a list of {@link MedImage}
 */
public class ImageSeries extends Data implements Iterable<MedImage>, Cloneable {

    //TODO: manage access to the fields after image model is chosen
    public List<MedImageTag> tags;
    public List<MedImage> images;

    public ImageSeries(List<MedImageTag> tags, List<MedImage> images) {
        this.tags = tags;
        this.images = images;
    }

    public MedImageTag findTag(UUID tagId) {
        //TODO: (avlomakin) read more about java streams (LINQ C#)
        Optional<MedImageTag> result = tags.stream().filter(x -> x.id.compareTo(tagId) == 0).findFirst();
        return result.orElse(null);
    }

    @Override
    public Iterator<MedImage> iterator() {
        return images.iterator();
    }

    @Override
    protected Object clone() {
        //TODO: (avlomakin) read about java deep copy (.clone() is protected)
        return new ImageSeries(new ArrayList<>(tags), new ArrayList<>(images));
    }
}
