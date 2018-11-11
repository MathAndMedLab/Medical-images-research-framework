package core.data.medImage;

import java.util.UUID;

/**
 * Stores a single tag that is part of metadata of medical images.
 */
public class MedImageTag {
    public final String name;

    //TODO: (avlomakin) replace with a class that supports (xxxx;xxxx) medical tag ids
    public final UUID id;

    public final Object value;

    public MedImageTag(String name, UUID id, Object value) {
        this.name = name;
        this.id = id;
        this.value = value;
    }
}
