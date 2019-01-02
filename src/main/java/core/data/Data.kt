package core.data

/**
 * Data is storing some piece of information that is used and transmitted throughout framework.
 */
abstract class Data constructor(val attributes: AttributeCollection = AttributeCollection()) {

    companion object {

        val empty: Data = object : Data() {

        }
    }
}

