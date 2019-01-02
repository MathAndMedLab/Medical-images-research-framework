package core.data

import core.data.attribute.DataAttribute
import core.data.attribute.DataAttributeMockup

import kotlin.collections.ArrayList

//TODO: (avlomakin) implements Collection<DataAttribute>
class AttributeCollection constructor(list : Collection<DataAttribute<*>> = ArrayList()) : Cloneable {

    private val attributes: ArrayList<DataAttribute<*>> = ArrayList(list)

    fun hasAttribute(mockup: DataAttributeMockup<*>): Boolean {
        return hasAttribute(mockup.tag)
    }

    fun hasAttribute(attributeTag: String): Boolean {
        return attributes.any { x -> x.tag == attributeTag }
    }

    fun add(attribute: DataAttribute<*>) {
        attributes.add(attribute)
    }

    fun <T> add(attributeMockup: DataAttributeMockup<T>, value: T) {
        attributes.add(attributeMockup.createAttribute(value))
    }

    public override fun clone(): AttributeCollection {
        val clonedAttributes = attributes.map {it -> it.clone()}
        return AttributeCollection(clonedAttributes)
    }

    /**
     * Finds ImageSeries attribute by attributeTag
     * @param attributeTag tag of the requested attribute
     * @return found attribute or null
     */
    fun find(attributeTag: String): DataAttribute<*>? {
        return attributes.first { x -> x.tag == attributeTag }
    }

    fun <T> findAttributeValue(attributeTag: String): T? {
        val attribute = find(attributeTag) ?: return null


//TODO: (avlomakin) make safe cast or provide method with meaningful exception
        return attribute.value as T
    }

    fun <T> findAttributeValue(attribute: DataAttributeMockup<T>): T? {
        return findAttributeValue<T>(attribute.tag)
    }
}
