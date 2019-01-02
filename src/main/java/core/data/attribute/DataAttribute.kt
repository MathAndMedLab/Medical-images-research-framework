package core.data.attribute

/**
 * Stores a single tag that is part of metadata of medical images.
 */
class DataAttribute<T>(val name: String, val tag: String, val value: T) : Cloneable {

    var description: String? = null

    public override fun clone(): DataAttribute<T> {
        return DataAttribute(name, tag, value)
    }

    override fun toString(): String {
        return tag + " " + name + ": " + value.toString() + ") "
    }
}

