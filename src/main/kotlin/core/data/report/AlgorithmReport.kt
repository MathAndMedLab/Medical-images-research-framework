package core.data.report

import core.data.AttributeCollection
import core.data.Data
import core.data.MirfData

/**
 * Superclass for all reports in MIRF
 */
abstract class AlgorithmReport : MirfData {

    var mirfReportType: MirfReportType? = null

    constructor() : super()

    constructor(attributes: AttributeCollection) : super(attributes)
}
