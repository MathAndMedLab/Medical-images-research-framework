package features.repositoryAccessors;

import core.algorithm.ReportableAlgorithm;
import core.data.DataTable;
import core.data.medImage.ImageSeries;
import core.data.medImage.MedImage;
import core.data.report.DataTableReport;
import core.data.report.Report;
import features.repositoryAccessors.data.RepoRequest;
import core.pipeline.impl.AlgorithmHostBlock;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Class to inject in pipeline via {@link AlgorithmHostBlock}, retrieves ImageSeries using provided RepoRequest
 */
public class RepoImageSeriesAccessor implements ReportableAlgorithm<RepoRequest, ImageSeries> {

    //TODO: (avlomakin) replace constants with resource variables
    private static final String REPOSITORY_HEADER = "Repository";
    private static final String LINK_HEADER = "Link";
    private static final String TOTAL_LOADED = "Total loaded";
    private static final String IMAGE_TYPE_HEADER = "Image type";

    @Override
    public ImageSeries execute(RepoRequest input) {
        return input.getRepository().getImageSeries(input.getLink());
    }

    @Override
    public Report getReport(RepoRequest input, ImageSeries executionResult) {

        DataTable reportTable = new DataTable();
        reportTable.columns.addAll(getHeaders());
        reportTable.rows.addAll(getRows(input, executionResult.images));

        DataTableReport report = new DataTableReport(reportTable);
        return report;
    }

    private List<Dictionary<String, String>> getRows(RepoRequest input, List<MedImage> images) {
        //TODO: (avlomakin) create metachecker of ImageSeries

        String repo = input.getRepository().getClass().getName();
        String totalLoaded = String.valueOf(images.size());

        Dictionary<String, String> row = new Hashtable<>();
        row.put(REPOSITORY_HEADER, repo);
        row.put(LINK_HEADER, input.getLink());
        row.put(TOTAL_LOADED, totalLoaded);
        row.put(IMAGE_TYPE_HEADER, images.get(0).getExtension());

        return new ArrayList<Dictionary<String, String>>(){{add(row);}};
    }

    private ArrayList<String> getHeaders() {
        return new ArrayList<String>(){{
            add(REPOSITORY_HEADER);
            add(LINK_HEADER);
            add(TOTAL_LOADED);
            add(IMAGE_TYPE_HEADER);
        }};
    }
}
