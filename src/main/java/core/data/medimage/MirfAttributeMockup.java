package core.data.medimage;

/**
 *
 * Class to store information about {@link MirfAttribute} signature, define how the attribute looks like.
 * Can be used to create a concrete {@link MirfAttribute} by calling {@link MirfAttributeCreator#createFromMock(MirfAttributeMockup, Object)}
 */
public class MirfAttributeMockup<T> {
    public final String name;
    public final String tag;
    public final AttributeTagType attributeTagType;

    public MirfAttributeMockup(String name, String tag, AttributeTagType attributeTagType) {
        this.name = name;
        this.tag = tag;
        this.attributeTagType = attributeTagType;
    }

    public MirfAttribute createAttribute(T value)
    {
        return new MirfAttribute<>(name, tag, value);
    }
}