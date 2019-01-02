package features.dicomimage.util;

import core.algorithm.Algorithm;
import core.algorithm.SimpleAlg;
import core.data.attribute.MirfAttributes;
import core.data.medimage.ImageSeriesData;
import core.data.medimage.MedImage;
import core.repository.RepositoryInfo;
import features.repositoryaccessors.AlgorithmExecutionException;
import features.repositoryaccessors.RepoAccessorsAttributes;
import features.repositoryaccessors.RepositoryRequestInfo;
import features.repositoryaccessors.RepositoryRequestType;
import features.repositoryaccessors.data.RepoRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.nocatch.NoCatch.noCatch;

/**
 * Class that stores all algorithms to interact with {@link core.repository.RepositoryCommander}
 */
public final class DicomRepoRequestProcessors {
    public static Algorithm<RepoRequest, ImageSeriesData> ReadDicomImageSeriesAlg = new SimpleAlg<>(DicomRepoRequestProcessors::readDicomImageSeries);

    //TODO: (avlomakin) rewrite it!
    private static ImageSeriesData readDicomImageSeries(RepoRequest request){
        try {
            var files = request.getRepositoryCommander().getSeriesFileLinks(request.getLink());

            List<MedImage> images = Arrays.stream(files).parallel().map(file ->
            {
                var bytes = noCatch(() -> request.getRepositoryCommander().getFile(file));
                InputStream stream = new ByteArrayInputStream(bytes);

                return DicomReader.readDicomImage(stream);
            }).collect(Collectors.toList());

            var result = new ImageSeriesData(images);

            var repositoryInfo = new RepositoryInfo(request.getRepositoryCommander().getClass().getSimpleName(), "TODO: (avlomakin)");
            result.getAttributes().add(MirfAttributes.INSTANCE.getREPO_INFO(), repositoryInfo);

            var requestInfo = new RepositoryRequestInfo(request.getLink(), RepositoryRequestType.GET);
            result.getAttributes().add(RepoAccessorsAttributes.REPOSITORY_REQUEST_INFO, requestInfo);

            return result;

        } catch (Exception e) {
            throw new AlgorithmExecutionException(e);
        }
    }
}
