package com.mirf.features.repositoryaccessors.data

import com.mirf.core.data.MirfData
import com.mirf.core.repository.RepositoryCommander

/**
 * Class used by Repository accessors. Contains information about repository and link for request
 */
open class RepoRequest(val link: String,
                       val repositoryCommander: RepositoryCommander,
                       var bundle: Any? = null) : MirfData()
