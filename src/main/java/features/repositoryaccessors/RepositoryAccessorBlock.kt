package features.repositoryaccessors

import core.algorithm.Algorithm
import core.data.Data
import core.pipeline.DummyPipeKeeper
import core.pipeline.PipelineBlock
import core.pipeline.PipelineException
import core.pipeline.PipelineKeeper
import core.repository.RepositoryCommander
import features.repositoryaccessors.data.RepoRequest

/**
 * [PipelineBlock] that stores all necessary information about repository such as [RepositoryCommander],
 * connection string and Repository accessor algorithm
 * @param <I> Input type
 * @param <O> Output type
</O></I> */
class RepositoryAccessorBlock<I : Data, O : Data>(
        private val repo: RepositoryCommander,
        private val algorithm: Algorithm<RepoRequest, O>,
        private val connectionString: String,
        name: String = "Repository accessor for $algorithm",
        pipelineKeeper: PipelineKeeper = DummyPipeKeeper()) : PipelineBlock<I, O>(name, pipelineKeeper){

    var enabled = true

    override fun inputReady(sender: Any, input: I) {
        if (enabled) {

            val record = pipelineKeeper.session.addNew("[$name]: request")

            try {
                val result = algorithm.execute(RepoRequest(connectionString, repo, input))
                onDataReady(this, result)
                record.setSuccess()
            } catch (e: PipelineException) {
                record.setError()
                throw e
            }
        }
    }

    override fun flush() { }
}
