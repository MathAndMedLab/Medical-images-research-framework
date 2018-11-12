package core.data.medImage;

/**
 * Stores a single tag that is part of metadata of medical images.
 */
public class MedImageAttribute {

    public final String name;

    public final String tag;
    public final AttributeTagType tagType;

    public final Object value;
    public final AttributeValueRepresentation valueRepresentation;

    public String description;

    public MedImageAttribute(String name, String tag, AttributeTagType tagType, Object value, AttributeValueRepresentation valueRepresentation) {
        this.name = name;
        this.tag = tag;
        this.tagType = tagType;
        this.value = value;
        this.valueRepresentation = valueRepresentation;
    }

    /**
     * Creates DICOM data attribute
     * @param tag attribute tag  - (XXXX,XXXX) with hexadecimal numbers
     * @param name attribute name
     * @param valueRepresentation  Value Representation (VR) that describes the data type and format of the attribute value (see DICOM documentation)
     * @param value attribute value
     * @return created attribute for Dicom format
     */
    public static MedImageAttribute CreateDicomAttribute(String tag, String name, AttributeValueRepresentation valueRepresentation, Object value)
    {
        if(!isDicomTag(tag))
            throw new IllegalArgumentException("tag");

        if(!isDicomVR(valueRepresentation))
            throw new IllegalArgumentException("valueRepresentation");

        return new MedImageAttribute(name, tag, AttributeTagType.Dicom, value, valueRepresentation);
    }

    /**
     * Create MIRF data attribute
     * @param tag attribute tag  - UUID
     * @param name attribute name
     * @param valueRepresentation  Value Representation (VR) that describes the data type and format of the attribute value (see MIRF documentation)
     * @param value attribute value
     * @return created attribute
     */
    public static MedImageAttribute CreateMirfAttribute(String tag, String name, AttributeValueRepresentation valueRepresentation, Object value)
    {
        if(!tag.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"))
            throw new IllegalArgumentException("tag");

        return new MedImageAttribute(name, tag, AttributeTagType.UUID, value, valueRepresentation);
    }

    public static MedImageAttribute createFromMock(MedImageAttributeMockup mockup, Object value){
        if(!isMockupValid(mockup))
            throw new IllegalArgumentException("mockup");

        return new MedImageAttribute(mockup.name, mockup.tag, mockup.attributeTagType, value, mockup.valueRepresentation);
    }

    private static boolean isMockupValid(MedImageAttributeMockup mockup) {
        //TODO: (avlomakin) implement
        return true;
    }

    private static boolean isDicomVR(AttributeValueRepresentation valueRepresentation) {
        switch (valueRepresentation) {
            case DicomPN:
            case DicomUN:
                return true;
            default:
                return false;
        }
    }

    private static boolean isDicomTag(String tag) {

        if (tag == null || tag.isEmpty())
            return false;

        String pattern = "^[(][0-9a-fA-F]{4}[,][0-9a-fA-F]{4}[)]$";
        return tag.matches(pattern);
    }

    @Override
    public String toString() {
        return tag + " " + name + ": " + value.toString() + " ( " + valueRepresentation.toString() + ") ";
    }
}

