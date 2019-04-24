package com.mirf.core.data.report

import com.mirf.core.data.DataTable

/**
 * MIRF report of [core.algorithm.Algorithm] presented as data table
 */
class DataTableAlgorithmReport(var table: DataTable) : AlgorithmReport() {

    init {
        mirfReportType = MirfReportType.DataTable
    }
}
