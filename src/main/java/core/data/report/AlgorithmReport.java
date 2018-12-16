package core.data.report;

import core.data.AttributeCollection;
import core.data.Data;

/**
 * Superclass for all reports in MIRF
 */
public abstract class AlgorithmReport extends Data {

    public MirfReportType mirfReportType;

    public AlgorithmReport() {
        super();
    }

    public AlgorithmReport(AttributeCollection attributes) {
        super(attributes);
    }
}
