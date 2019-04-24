package com.mirf.core.data.attribute

import com.mirf.core.log.MirfLogFactory

/**
 * Stores a single tag that is part of metadata of medical images.
 */
open class DataAttribute<T>(val name: String, val tag: String, val value: T, private val cloneMethod: ((T) -> T)? = null)  {

    var description: String? = null

    protected open val log = MirfLogFactory.currentLogger

    fun copy(): DataAttribute<T> {

        if(cloneMethod == null) {
            log.warn("No clone method provided for $name, new attribute will share the same value object")
            val result = DataAttribute(name, tag, value)
            result.description = this.description
            return result
        }

        val clonedValue = cloneMethod.invoke(value)
        val result = DataAttribute(name, tag, clonedValue)
        result.description = this.description

        return result
    }

    override fun toString(): String {
        return tag + " " + name + ": " + value.toString() + ") "
    }
}

