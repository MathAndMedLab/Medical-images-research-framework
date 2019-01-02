package features.repositoryaccessors

import core.algorithm.Algorithm
import core.data.Data
import core.data.FileData
import features.repositoryaccessors.data.RepoRequest

/**
 * [Algorithm] that saves file, presented in [FileData] using provided [RepoRequest]
 */
class RepoFileSaver : Algorithm<RepoRequest, Data> {

    override fun execute(input: RepoRequest): Data {

        if (input.bundle !is FileData)
            throw RuntimeException("invalid request: FileData parse error")

        val data = input.bundle as FileData?

        try {
            input.repositoryCommander.saveFile(data!!.fileBytes, input.link, data.name + data.extension)
        } catch (e: Exception) {
            throw AlgorithmExecutionException("Unable to save file", e)
        }

        return Data.empty
    }
}
