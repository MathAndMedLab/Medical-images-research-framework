package core.data;

import java.util.Collection;

/**
 * {@link Data} that represents collection elements
 * @param <T> element type
 */
//TODO: (avlomakin) consider removing 'extends Data' from T
public class CollectionData<T extends Data> extends Data {

    public final Collection<T> collection;

    public CollectionData(Collection<T> collection) {
        super();
        this.collection = collection;
    }
}
