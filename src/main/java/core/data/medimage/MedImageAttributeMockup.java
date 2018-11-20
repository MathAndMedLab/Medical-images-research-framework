package core.data.medimage;

/**
 *
 * Class to store information about {@link MedImageAttribute} signature, define how the attribute looks like.
 * Can be used to create a concrete {@link MedImageAttribute} by calling {@link MirfAttributeCreator#createFromMock(MedImageAttributeMockup, Object)}
 */
public class MedImageAttributeMockup {
    public final String name;
    public final String tag;
    public final AttributeTagType attributeTagType;

    public MedImageAttributeMockup(String name, String tag, AttributeTagType attributeTagType) {
        this.name = name;
        this.tag = tag;
        this.attributeTagType = attributeTagType;
    }
}