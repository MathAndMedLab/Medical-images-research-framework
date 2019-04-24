package com.mirf.features.repositoryaccessors

import com.mirf.core.algorithm.Algorithm
import com.mirf.core.data.Data
import com.mirf.core.pipeline.DummyPipeKeeper
import com.mirf.core.pipeline.PipelineBlock
import com.mirf.core.pipeline.PipelineException
import com.mirf.core.pipeline.PipelineKeeper
import com.mirf.core.repository.RepositoryCommander
import com.mirf.features.repositoryaccessors.data.RepoRequest

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
