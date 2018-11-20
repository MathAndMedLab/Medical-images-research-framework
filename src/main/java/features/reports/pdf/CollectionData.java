package features.reports.pdf;

import core.data.Data;

import java.util.Collection;

//TODO: (avlomakin) consider removing 'extends Data' from T
public class CollectionData<T extends Data> extends Data {

    public final Collection<T> collection;

    public CollectionData(Collection<T> collection) {
        this.collection = collection;
    }
}
