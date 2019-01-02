package core.data.attribute

/**
 *
 * Class to store information about [DataAttribute] signature, define how the attribute looks like.
 * Can be used to create a concrete [DataAttribute] by calling [DataAttributeCreator.createFromMock]
 */
class DataAttributeMockup<T>(val name: String, val tag: String, val attributeTagType: AttributeTagType) {

    fun createAttribute(value: T): DataAttribute<*> {
        return DataAttribute(name, tag, value)
    }
}