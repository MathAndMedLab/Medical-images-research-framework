package com.mirf.features.dicomimage.util

import com.mirf.core.algorithm.Algorithm
import com.mirf.core.algorithm.SimpleAlg
import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.repository.RepositoryInfo
import com.mirf.features.repositoryaccessors.AlgorithmExecutionException
import com.mirf.features.repositoryaccessors.RepoAccessorsAttributes
import com.mirf.features.repositoryaccessors.RepositoryRequestInfo
import com.mirf.features.repositoryaccessors.RepositoryRequestType
import com.mirf.features.repositoryaccessors.data.RepoRequest

import java.io.ByteArrayInputStream
import java.util.Arrays

import com.github.nocatch.NoCatch.noCatch
import com.mirf.core.data.attribute.DataAttribute
import com.mirf.core.data.medimage.MirfImageSeries
import kotlin.streams.toList

/**
 * Class that stores all algorithms to interact with [core.repository.RepositoryCommander]
 */
object DicomRepoRequestProcessors {
    var readDicomImageSeriesAlg: Algorithm<RepoRequest, ImageSeries> = SimpleAlg { request: RepoRequest -> readDicomImageSeries(request) }

    //TODO: (avlomakin) rewrite it!
    private fun readDicomImageSeries(request: RepoRequest): ImageSeries {
        try {
            val files = request.repositoryCommander.getSeriesFileLinks(request.link)

            val images = Arrays.stream(files).parallel().map { file ->
                val bytes = noCatch<ByteArray> { request.repositoryCommander.getFile(file) }
                val stream = ByteArrayInputStream(bytes)

                DicomReader.readDicomImage(stream)
            }.toList()

            val result = MirfImageSeries(images)
            result.attributes.addRange(getMetadata(request))

            return result

        } catch (e: Exception) {
            throw AlgorithmExecutionException(e)
        }

    }

    private fun getMetadata(request: RepoRequest): Collection<DataAttribute<*>> {
        val result = ArrayList<DataAttribute<*>>()

        val repositoryInfo = RepositoryInfo(request.repositoryCommander.javaClass.simpleName, "TODO: (avlomakin)")
        result.add(MirfAttributes.REPO_INFO.new(repositoryInfo))

        val requestInfo = RepositoryRequestInfo(request.link, RepositoryRequestType.GET)
        result.add(RepoAccessorsAttributes.REPOSITORY_REQUEST_INFO.new(requestInfo))

        return result;
    }
}
