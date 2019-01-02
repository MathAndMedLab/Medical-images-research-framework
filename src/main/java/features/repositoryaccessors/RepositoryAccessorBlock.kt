package features.repositoryaccessors

import core.algorithm.Algorithm
import core.data.Data
import core.pipeline.PipelineBlock
import core.repository.RepositoryCommander
import features.repositoryaccessors.data.RepoRequest

/**
 * [PipelineBlock] that stores all necessary information about repository such as [RepositoryCommander],
 * connection string and Repository accessor algorithm
 * @param <I> Input type
 * @param <O> Output type
</O></I> */
class RepositoryAccessorBlock<I : Data, O : Data>
(private val repo: RepositoryCommander, private val algorithm: Algorithm<RepoRequest, O>, private val connectionString: String) : PipelineBlock<I, O>() {

    var enabled = true

    override fun inputDataReady(sender: PipelineBlock<*, I>?, input: I) {
        if (enabled) {
            val result = algorithm.execute(object : RepoRequest(connectionString, repo) {
                init {
                    bundle = input
                }
            })
            notifyListeners(this, result)
        }
    }

    override fun flush() { }
}
