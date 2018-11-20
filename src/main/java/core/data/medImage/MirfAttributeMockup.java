package core.data.medImage;

/**
 *
 * Class to store information about {@link MirfAttribute} signature, define how the attribute looks like.
 * Can be used to create a concrete {@link MirfAttribute} by calling {@link MirfAttributeCreator#createFromMock(MirfAttributeMockup, Object)}
 */
public class MirfAttributeMockup<ValueType> {
    public final String name;
    public final String tag;
    public final AttributeTagType attributeTagType;

    public MirfAttributeMockup(String name, String tag, AttributeTagType attributeTagType) {
        this.name = name;
        this.tag = tag;
        this.attributeTagType = attributeTagType;
    }

    public MirfAttribute createAttribute(ValueType value)
    {
        return new MirfAttribute(name, tag, value);
    }
}