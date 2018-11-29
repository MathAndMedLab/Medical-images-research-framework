package core.data.medimage;

/**
 * Manages framework attributes creation
 */
public final class DataAttributeCreator {

    private DataAttributeCreator() {
    }

    /**
     * Creates MIRF data attribute
     * @param tag attribute tag  - UUID
     * @param name attribute name
     * @param value attribute value
     * @return created attribute
     */
    public static DataAttribute createDataAttribute(String tag, String name, Object value)
    {
        if(!tag.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"))
            throw new IllegalArgumentException("tag");

        return new DataAttribute(name, tag, value);
    }

    /**
     * Creates {@link DataAttribute} from {@link DataAttributeMockup}
     * @param mockup mockup to be used
     * @param value attribute value
     * @return created attribute
     */
    public static DataAttribute createFromMock(DataAttributeMockup mockup, Object value){
        if(!isMockupValid(mockup))
            throw new IllegalArgumentException("mockup");

        return new DataAttribute(mockup.name, mockup.tag, value);
    }

    private static boolean isMockupValid(DataAttributeMockup mockup) {
        //TODO: (avlomakin) implement
        return true;
    }
}
