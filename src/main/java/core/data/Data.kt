package core.data

import core.log.MirfLogFactory

/**
 * Data is storing some piece of information that is used and transmitted throughout framework.
 */
abstract class Data constructor(open val attributes: AttributeCollection = AttributeCollection()) {

    protected open val log = MirfLogFactory.currentLogger

    companion object {

        val empty: Data = object : Data() {

        }
    }
}

