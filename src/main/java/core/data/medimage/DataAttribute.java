package core.data.medimage;

/**
 * Stores a single tag that is part of metadata of medical images.
 */
public class DataAttribute<T> {

    public final String name;

    //TODO: (avlomakin) string for now, mb change to UUID
    public final String tag;

    public final T value;

    public String description;

    public DataAttribute(String name, String tag, T value) {
        this.name = name;
        this.tag = tag;
        this.value = value;
    }

    @Override
    public String toString() {
        return tag + " " + name + ": " + value.toString()  + ") ";
    }
}

