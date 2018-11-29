package core.data.medimage;

/**
 *
 * Class to store information about {@link DataAttribute} signature, define how the attribute looks like.
 * Can be used to create a concrete {@link DataAttribute} by calling {@link DataAttributeCreator#createFromMock(DataAttributeMockup, Object)}
 */
public class DataAttributeMockup<T> {
    public final String name;
    public final String tag;
    public final AttributeTagType attributeTagType;

    public DataAttributeMockup(String name, String tag, AttributeTagType attributeTagType) {
        this.name = name;
        this.tag = tag;
        this.attributeTagType = attributeTagType;
    }

    public DataAttribute createAttribute(T value)
    {
        return new DataAttribute<>(name, tag, value);
    }
}