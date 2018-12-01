package core.data.report;

import core.data.DataTable;

/**
 * MIRF report of {@link core.algorithm.Algorithm} presented as data table
 */
public class DataTableAlgorithmReport extends AlgorithmReport {

    public DataTable table;

    public DataTableAlgorithmReport(DataTable table) {
        this.table = table;
        mirfReportType = MirfReportType.DataTable;
    }
}
