package com.mirf.core.data

import com.mirf.core.log.MirfLogFactory

/**
 * Data is storing some piece of information that is used and transmitted throughout framework.
 */
abstract class MirfData constructor(override val attributes: AttributeCollection = AttributeCollection()) : Data {

    protected open val log = MirfLogFactory.currentLogger

    companion object {

        val empty: Data = object : MirfData() {

        }
    }
}