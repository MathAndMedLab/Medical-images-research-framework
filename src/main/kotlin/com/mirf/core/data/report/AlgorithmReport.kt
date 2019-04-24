package com.mirf.core.data.report

import com.mirf.core.data.AttributeCollection
import com.mirf.core.data.MirfData

/**
 * Superclass for all reports in MIRF
 */
abstract class AlgorithmReport : MirfData {

    var mirfReportType: MirfReportType? = null

    constructor() : super()

    constructor(attributes: AttributeCollection) : super(attributes)
}
