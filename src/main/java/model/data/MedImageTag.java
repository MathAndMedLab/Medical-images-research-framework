package model.data;

import java.util.UUID;

public class MedImageTag  {
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
