package core.data.medimage;

import core.data.Data;

import java.util.*;

/**
 * ImageSeries stores a list of {@link MedImage}
 */
public class ImageSeries extends Data implements Iterable<MedImage>, Cloneable {

    //TODO: manage access to the fields after image model is chosen
    public List<MedImageAttribute> attributes;
    public List<MedImage> images;

    public ImageSeries(List<MedImageAttribute> attributes, List<MedImage> images) {

        this.attributes = new ArrayList<>();

        if(attributes != null)
            this.attributes.addAll(attributes);

        this.images = images;
    }

    /**
     * Finds ImageSeries attribute by tag
     * @param tag tag of the requested attribute
     * @return found attribute or null
     */
    public MedImageAttribute findTag(String tag) {
        Optional<MedImageAttribute> result = attributes.stream().filter(x -> x.tag.equals(tag)).findFirst();
        return result.orElse(null);
    }

    @Override
    public Iterator<MedImage> iterator() {
        return images.iterator();
    }

    @Override
    protected Object clone() {
        //TODO: (avlomakin) read about java deep copy (.clone() is protected)
        return new ImageSeries(new ArrayList<>(attributes), new ArrayList<>(images));
    }
}
