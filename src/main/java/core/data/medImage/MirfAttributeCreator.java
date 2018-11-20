package core.data.medImage;

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
    public static MirfAttribute createMirfAttribute(String tag, String name, Object value)
    {
        if(!tag.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"))
            throw new IllegalArgumentException("tag");

        return new MirfAttribute(name, tag, value);
    }

    /**
     * Creates {@link MirfAttribute} from {@link MirfAttributeMockup}
     * @param mockup mockup to be used
     * @param value attribute value
     * @return created attribute
     */
    public static MirfAttribute createFromMock(MirfAttributeMockup mockup, Object value){
        if(!isMockupValid(mockup))
            throw new IllegalArgumentException("mockup");

        return new MirfAttribute(mockup.name, mockup.tag, value);
    }

    private static boolean isMockupValid(MirfAttributeMockup mockup) {
        //TODO: (avlomakin) implement
        return true;
    }
}
