package features.reports.creators;

import core.algorithm.Algorithm;
import core.data.DataTable;
import core.data.medimage.ImageSeries;
import core.data.medimage.MirfAttributes;
import core.data.report.AlgorithmReport;
import core.data.report.DataTableAlgorithmReport;
import core.repository.RepositoryInfo;
import features.repositoryaccessors.RepoAccessorsAttributes;
import features.repositoryaccessors.RepositoryRequestInfo;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class RepoAccessorReportCreator implements Algorithm<ImageSeries, AlgorithmReport> {

    //TODO: (avlomakin) replace constants with resource variables
    private static final String REPOSITORY_HEADER = "Repository";
    private static final String LINK_HEADER = "Link";
    private static final String TOTAL_LOADED = "Total loaded";
    private static final String IMAGE_TYPE_HEADER = "Image type";


    private List<Dictionary<String, String>> getRows(ImageSeries medImages) {

        RepositoryInfo repositoryInfo = medImages.findAttributeValue(MirfAttributes.REPO_INFO.tag);
        RepositoryRequestInfo requestInfo = medImages.findAttributeValue(RepoAccessorsAttributes.REPOSITORY_REQUEST_INFO.tag);

        String totalLoaded = String.valueOf(medImages.images.size());

        Dictionary<String, String> row = new Hashtable<>();
        row.put(REPOSITORY_HEADER, repositoryInfo.repositoryName);
        row.put(LINK_HEADER, requestInfo.link);
        row.put(TOTAL_LOADED, totalLoaded);
        row.put(IMAGE_TYPE_HEADER, medImages.images.get(0).getExtension());

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

    @Override
    public AlgorithmReport execute(ImageSeries medImages) {
        DataTable reportTable = new DataTable();
        reportTable.columns.addAll(getHeaders());
        reportTable.rows.addAll(getRows(medImages));

        DataTableAlgorithmReport report = new DataTableAlgorithmReport(reportTable);
        return report;
    }
}
