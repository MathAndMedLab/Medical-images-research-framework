package core.data.medImage;

/**
 * Stores a single tag that is part of metadata of medical images.
 */
public class MirfAttribute {

    public final String name;

    //TODO: (avlomakin) string for now, mb change to UUID
    public final String tag;

    public final Object value;

    public String description;

    public MirfAttribute(String name, String tag, Object value) {
        this.name = name;
        this.tag = tag;
        this.value = value;
    }

    @Override
    public String toString() {
        return tag + " " + name + ": " + value.toString()  + ") ";
    }
}

