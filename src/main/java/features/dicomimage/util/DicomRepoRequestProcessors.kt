package features.dicomimage.util

import core.algorithm.Algorithm
import core.algorithm.SimpleAlg
import core.data.attribute.MirfAttributes
import core.data.medimage.ImageSeriesData
import core.data.medimage.MedImage
import core.repository.RepositoryInfo
import features.repositoryaccessors.AlgorithmExecutionException
import features.repositoryaccessors.RepoAccessorsAttributes
import features.repositoryaccessors.RepositoryRequestInfo
import features.repositoryaccessors.RepositoryRequestType
import features.repositoryaccessors.data.RepoRequest

import java.io.ByteArrayInputStream
import java.util.Arrays

import com.github.nocatch.NoCatch.noCatch
import core.data.attribute.DataAttribute
import kotlin.streams.toList

/**
 * Class that stores all algorithms to interact with [core.repository.RepositoryCommander]
 */
object DicomRepoRequestProcessors {
    var ReadDicomImageSeriesAlg: Algorithm<RepoRequest, ImageSeriesData> = SimpleAlg { request: RepoRequest -> readDicomImageSeries(request) }

    //TODO: (avlomakin) rewrite it!
    private fun readDicomImageSeries(request: RepoRequest): ImageSeriesData {
        try {
            val files = request.repositoryCommander.getSeriesFileLinks(request.link)

            val images = Arrays.stream(files).parallel().map { file ->
                val bytes = noCatch<ByteArray> { request.repositoryCommander.getFile(file) }
                val stream = ByteArrayInputStream(bytes)

                DicomReader.readDicomImage(stream)
            }.toList()

            val result = ImageSeriesData(images)
            result.attributes.addRange(getMetadata(request))

            return result

        } catch (e: Exception) {
            throw AlgorithmExecutionException(e)
        }

    }

    private fun getMetadata(request: RepoRequest): Collection<DataAttribute<*>> {
        val result = ArrayList<DataAttribute<*>>()

        val repositoryInfo = RepositoryInfo(request.repositoryCommander.javaClass.simpleName, "TODO: (avlomakin)")
        result.add(MirfAttributes.REPO_INFO.createAttribute(repositoryInfo))

        val requestInfo = RepositoryRequestInfo(request.link, RepositoryRequestType.GET)
        result.add(RepoAccessorsAttributes.REPOSITORY_REQUEST_INFO.createAttribute(requestInfo))

        return result;
    }
}
