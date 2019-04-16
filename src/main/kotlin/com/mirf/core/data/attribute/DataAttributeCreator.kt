package com.mirf.core.data.attribute

/**
 * Manages framework attributes creation
 */
object DataAttributeCreator {

    /**
     * Creates MIRF data attribute
     * @param tag attribute tag  - UUID
     * @param name attribute name
     * @param value attribute value
     * @return created attribute
     */
    @Throws(AttributeCreationException::class)
    fun <T> createDataAttribute(tag: String, name: String, value: T): DataAttribute<*> {

        if (!tag.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}".toRegex()))
            throw AttributeCreationException("Invalid tag shape: UUID required")

        return DataAttribute(name, tag, value)
    }

    /**
     * Creates [DataAttribute] from [DataAttributeMockup]
     * @param mockup mockup to be used
     * @param value attribute value
     * @return created attribute
     */
    fun <T> createFromMock(mockup: DataAttributeMockup<*>, value: T): DataAttribute<*> {
        if (!isMockupValid(mockup))
            throw IllegalArgumentException("Invalid mockup")

        return DataAttribute(mockup.name, mockup.tag, value)
    }

    private fun isMockupValid(mockup: DataAttributeMockup<*>): Boolean {
        //TODO: (avlomakin) implement
        return true
    }
}
