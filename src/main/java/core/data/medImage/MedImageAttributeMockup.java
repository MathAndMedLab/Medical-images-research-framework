package core.data.medImage;

/**
 *
 * Class to store information about {@link MedImageAttribute} signature, without actual value
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