package core.data.report;

import core.data.DataTable;

public class DataTableReport extends Report {

    public DataTable table;

    public DataTableReport(DataTable table) {
        this.table = table;
        mirfReportType = MirfReportType.DataTable;
    }
}
