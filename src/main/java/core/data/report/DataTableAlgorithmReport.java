package core.data.report;

import core.data.DataTable;

public class DataTableAlgorithmReport extends AlgorithmReport {

    public DataTable table;

    public DataTableAlgorithmReport(DataTable table) {
        this.table = table;
        mirfReportType = MirfReportType.DataTable;
    }
}
