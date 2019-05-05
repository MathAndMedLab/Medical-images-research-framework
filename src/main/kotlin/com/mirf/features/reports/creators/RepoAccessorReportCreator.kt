package com.mirf.features.reports.creators

import com.mirf.core.algorithm.Algorithm
import com.mirf.core.data.DataTable
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.report.AlgorithmReport
import com.mirf.core.data.report.DataTableAlgorithmReport
import com.mirf.features.repositoryaccessors.RepoAccessorsAttributes

import java.util.ArrayList
import java.util.Dictionary
import java.util.Hashtable

/**
 * [AlgorithmReport] creator for RepositoryAccessors
 */
class RepoAccessorReportCreator : Algorithm<ImageSeries, AlgorithmReport> {

    private val headers: ArrayList<String>
        get() = object : ArrayList<String>() {
            init {
                add(REPOSITORY_HEADER)
                add(LINK_HEADER)
                add(TOTAL_LOADED)
                add(IMAGE_TYPE_HEADER)
            }
        }

    private fun getRows(medImages: ImageSeries): List<HashMap<String, String>> {

        val repositoryInfo = medImages.attributes.findAttributeValue(MirfAttributes.REPO_INFO)

        val requestInfo = medImages.attributes.findAttributeValue(RepoAccessorsAttributes.REPOSITORY_REQUEST_INFO)

        val totalLoaded = medImages.images.size.toString()

        val row = hashMapOf<String, String>()
        row[REPOSITORY_HEADER] = repositoryInfo!!.repositoryName
        row[LINK_HEADER] = requestInfo!!.link
        row[TOTAL_LOADED] = totalLoaded
        row[IMAGE_TYPE_HEADER] = medImages.images[0].extension

        return object : ArrayList<HashMap<String, String>>() {
            init {
                add(row)
            }
        }
    }

    override fun execute(input: ImageSeries): DataTableAlgorithmReport {
        val reportTable = DataTable()
        reportTable.columns.addAll(headers)
        reportTable.rows.addAll(getRows(input))

        return DataTableAlgorithmReport(reportTable)
    }

    companion object {

        //TODO: (avlomakin) replace constants with resource variables
        private const val REPOSITORY_HEADER = "Repository"
        private const val LINK_HEADER = "Link"
        private const val TOTAL_LOADED = "Total loaded"
        private const val IMAGE_TYPE_HEADER = "Image type"
    }
}
