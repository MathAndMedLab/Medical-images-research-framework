package core.data.report

import core.data.AttributeCollection
import core.data.Data

/**
 * Superclass for all reports in MIRF
 */
abstract class AlgorithmReport : Data {

    var mirfReportType: MirfReportType? = null

    constructor() : super()

    constructor(attributes: AttributeCollection) : super(attributes)
}
