package com.mirf.core.data

/**
 * [Data] that represents collection elements
 * @param <T> element type
</T> */
//TODO: (avlomakin) consider removing 'extends Data' from T
class CollectionData<T : Data>(val collection: Collection<T>) : MirfData()
