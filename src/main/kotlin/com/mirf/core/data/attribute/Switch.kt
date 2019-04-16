package com.mirf.core.data.attribute

/**
 * Switch class is used to indicate [DataAttribute] that holds no value: it's either declared in attributes or no
 */
//TODO: (avlomakin) consider creation superclass for attributes with and without inner values
object Switch{
    fun get() : Instance = Instance()

    class Instance internal constructor()
}
