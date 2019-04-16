package com.mirf.features.repositoryaccessors

import com.mirf.core.data.attribute.AttributeTagType
import com.mirf.core.data.attribute.DataAttributeMockup

/**
 * Stores [DataAttributeMockup] for all well-known repository accessors data attributes
 */
object RepoAccessorsAttributes {

    val REPOSITORY_REQUEST_INFO = DataAttributeMockup<RepositoryRequestInfo>("Repository request", "8285b554-ecf3-11e8-8eb2-f2801f1b9fd1", AttributeTagType.UUID)
}
