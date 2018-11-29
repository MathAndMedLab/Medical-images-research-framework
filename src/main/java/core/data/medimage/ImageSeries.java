package core.data.medimage;

import core.data.Data;

import java.util.*;

/**
 * ImageSeries stores a list of {@link MedImage}
 */
public class ImageSeries extends Data implements Iterable<MedImage>, Cloneable {

    //TODO: manage access to the fields after image model is chosen
    public List<DataAttribute> attributes;
    public List<MedImage> images;

    public ImageSeries(List<DataAttribute> attributes, List<MedImage> images) {

        this.attributes = new ArrayList<>();

        if(attributes != null)
            this.attributes.addAll(attributes);

        this.images = images;
    }

    /**
     * Finds ImageSeries attribute by attributeTag
     * @param attributeTag tag of the requested attribute
     * @return found attribute or null
     */
    public DataAttribute findAttribute(String attributeTag) {
        Optional<DataAttribute> result = attributes.stream().filter(x -> x.tag.equals(attributeTag)).findFirst();
        return result.orElse(null);
    }

    public <T> T findAttributeValue(String attributeTag){
        DataAttribute attribute = findAttribute(attributeTag);
        if(attribute == null)
            return null;

        //TODO: (avlomakin) make safe cast or provide method with meaningful exception
        return (T)attribute.value;
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

    //TODO: (avlomakin) consider move method up to Data class
    public void addAttribute(DataAttribute attribute) {
        attributes.add(attribute);
    }
}
