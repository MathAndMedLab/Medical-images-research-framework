package core.data;

/**
 * Data is storing some piece of information that is used and transmitted throughout framework.
 */
public class Data {

    public static final Data empty = new Data();

    public final AttributeCollection attributes;

    public Data() {
        this(new AttributeCollection());
    }

    public Data(AttributeCollection attributes)
    {
        this.attributes = attributes;
    }
}

