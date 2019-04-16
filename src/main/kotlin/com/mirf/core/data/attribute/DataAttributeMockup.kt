package com.mirf.core.data.attribute

/**
 *
 * Class to store information about [DataAttribute] signature, define how the attribute looks like.
 * Can be used to create a concrete [DataAttribute] by calling [DataAttributeCreator.createFromMock]
 */
data class DataAttributeMockup<T>(val name: String, val tag: String, val attributeTagType: AttributeTagType, val cloneMethod: ((T) -> T)? = null) {

    fun new(value: T): DataAttribute<T> {
        return DataAttribute(name, tag, value, cloneMethod)
    }
}