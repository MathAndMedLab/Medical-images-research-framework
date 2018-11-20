package core.data.medimage;

/**
 * Manages framework attributes creation
 */
public final class MirfAttributeCreator {

    private MirfAttributeCreator() {
    }

    /**
     * Creates MIRF data attribute
     * @param tag attribute tag  - UUID
     * @param name attribute name
     * @param value attribute value
     * @return created attribute
     */
    public static MedImageAttribute createMirfAttribute(String tag, String name, Object value)
    {
        if(!tag.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"))
            throw new IllegalArgumentException("tag");

        return new MedImageAttribute(name, tag, value);
    }

    /**
     * Creates {@link MedImageAttribute} from {@link MedImageAttributeMockup}
     * @param mockup mockup to be used
     * @param value attribute value
     * @return created attribute
     */
    public static MedImageAttribute createFromMock(MedImageAttributeMockup mockup, Object value){
        if(!isMockupValid(mockup))
            throw new IllegalArgumentException("mockup");

        return new MedImageAttribute(mockup.name, mockup.tag, value);
    }

    private static boolean isMockupValid(MedImageAttributeMockup mockup) {
        //TODO: (avlomakin) implement
        return true;
    }
}
