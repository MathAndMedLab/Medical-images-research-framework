package core.data.medImage;

/**
 * Class to store information about {@link MedImageAttribute} signature, without actual value
 */
public class MedImageAttributeMockup {
    public final String name;
    public final String tag;
    public final AttributeTagType attributeTagType;
    public final AttributeValueRepresentation valueRepresentation;

    public MedImageAttributeMockup(String name, String tag, AttributeTagType attributeTagType, AttributeValueRepresentation valueRepresentation) {
        this.name = name;
        this.tag = tag;
        this.attributeTagType = attributeTagType;
        this.valueRepresentation = valueRepresentation;
    }
}