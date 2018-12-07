package core.data;

import core.data.attribute.DataAttribute;
import core.data.attribute.DataAttributeMockup;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO: (avlomakin) implements Collection<DataAttribute>
public class AttributeCollection implements Cloneable{

    private ArrayList<DataAttribute> attributes;

    private AttributeCollection(ArrayList<DataAttribute> attributes) {
        this.attributes = attributes;
    }

    public AttributeCollection() {
        this(new ArrayList<>());
    }

    public boolean hasAttribute(DataAttributeMockup mockup) {
        return hasAttribute(mockup.tag);
    }

    public boolean hasAttribute(String attributeTag) {
        return attributes.stream().anyMatch(x -> x.tag.equals(attributeTag));
    }

    public void add(DataAttribute attribute) {
        attributes.add(attribute);
    }

    public <T> void add(DataAttributeMockup<T> attributeMockup, T value) {
        attributes.add(attributeMockup.createAttribute(value));
    }

    @Override
    public AttributeCollection clone() {
        ArrayList<DataAttribute> clonedAttributes = attributes.stream().map(DataAttribute::clone).collect(Collectors.toCollection(ArrayList::new));
        return new AttributeCollection(clonedAttributes);
    }

    /**
     * Finds ImageSeries attribute by attributeTag
     * @param attributeTag tag of the requested attribute
     * @return found attribute or null
     */
    public DataAttribute find(String attributeTag) {
        Optional<DataAttribute> result = attributes.stream().filter(x -> x.tag.equals(attributeTag)).findFirst();
        return result.orElse(null);
    }

    public <T> T findAttributeValue(String attributeTag){
        DataAttribute attribute = find(attributeTag);

        if(attribute == null)
            return null;

        //TODO: (avlomakin) make safe cast or provide method with meaningful exception
        return (T)attribute.value;
    }

    public <T> T findAttributeValue(DataAttributeMockup<T> attribute) {
        return findAttributeValue(attribute.tag);
    }
}
