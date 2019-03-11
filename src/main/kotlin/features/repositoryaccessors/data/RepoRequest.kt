package features.repositoryaccessors.data

import core.data.Data
import core.data.MirfData
import core.repository.RepositoryCommander

/**
 * Class used by Repository accessors. Contains information about repository and link for request
 */
open class RepoRequest(val link: String,
                       val repositoryCommander: RepositoryCommander,
                       var bundle: Any? = null) : MirfData()
