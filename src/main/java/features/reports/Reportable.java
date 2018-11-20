package features.reports;

import core.data.Data;
import core.data.report.AlgorithmReport;

public interface Reportable<T extends Data> {
    AlgorithmReport getReportFromCache();

    AlgorithmReport getReport(T blockOutput);
}
