package core.algorithm;

import core.data.report.Report;

public interface ReportableAlgorithm<A, B> extends Algorithm<A, B> {

    Report getReport(A input, B executionResult);
}
