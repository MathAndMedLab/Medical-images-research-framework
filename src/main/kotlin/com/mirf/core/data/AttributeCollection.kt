package com.mirf.core.data

import com.mirf.core.data.attribute.DataAttribute
import com.mirf.core.data.attribute.DataAttributeMockup
import com.mirf.core.log.MirfLogFactory

//TODO: (avlomakin) implements Collection<DataAttribute>
open class AttributeCollection constructor(list : Collection<DataAttribute<*>> = ArrayList()) : Cloneable {

    protected open val log = MirfLogFactory.currentLogger

    private var _version = 0
    open val version = _version

    protected open val attributes: ArrayList<DataAttribute<*>> = ArrayList(list)

    fun hasAttribute(mockup: DataAttributeMockup<*>) = hasAttribute(mockup.tag)

    fun hasAttribute(attributeTag: String) = attributes.any { x -> x.tag == attributeTag }

    fun add(attribute: DataAttribute<*>) = this.internalAdd(attribute)

    fun <T> add(attributeMockup: DataAttributeMockup<T>, value: T) {
        val attribute =attributeMockup.new(value)
        internalAdd(attribute)
    }

    private fun internalAdd(attribute: DataAttribute<*>){
        _version++
        attributes.add(attribute)
    }

    public override fun clone(): AttributeCollection {
        val clonedAttributes = attributes.map { it.copy()}
        return AttributeCollection(clonedAttributes)
    }

    /**
     * Finds ImageSeries attribute by attributeTag
     * @param attributeTag tag of the requested attribute
     * @return found attribute or null
     */
    open fun find(attributeTag: String): DataAttribute<*>? {
        return attributes.firstOrNull { x -> x.tag == attributeTag }
    }

    open fun <T> findAttributeValue(attributeTag: String): T? {
        val attribute = find(attributeTag) ?: return null


//TODO: (avlomakin) make safe cast or provide method with meaningful exception
        return attribute.value as T
    }

    fun <T> findAttributeValue(attribute: DataAttributeMockup<T>): T? {
        return findAttributeValue<T>(attribute.tag)
    }

    fun <T> getAttributeValue(attribute: DataAttributeMockup<T>): T {
        return findAttributeValue<T>(attribute.tag) ?: throw AttributeException("Attribute ${attribute.name} not found")
    }

    fun addRange(range: Collection<DataAttribute<*>>) {
        range.forEach { x -> add(x) }
    }

    open operator fun <R> get(mockup: DataAttributeMockup<R>): R {
        val resultGen = find(mockup.tag) ?: throw AttributeException("attribute ${mockup.name} doesn't presented in the attribute collection")
        return (resultGen as DataAttribute<R>).value
    }
}
