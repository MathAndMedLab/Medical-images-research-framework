package com.mirf.core.pipeline

import com.mirf.core.data.Data
import com.mirf.core.log.MirfLogFactory
import com.mirf.core.repository.RepositoryCommander
import com.mirf.features.repository.LocalRepositoryCommander
import org.slf4j.Logger
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

class Pipeline(val name: String,
               val creationTime: LocalDateTime = LocalDateTime.now(),
               val repositoryCommander: LocalRepositoryCommander = LocalRepositoryCommander()) : PipelineKeeper {

    private val subCommanders: ConcurrentHashMap<PipelineBlock<*, *>, LocalRepositoryCommander> = ConcurrentHashMap()
    private val log: Logger = MirfLogFactory.currentLogger

    override fun getRepositoryCommander(block: PipelineBlock<*, *>): LocalRepositoryCommander {
        if (subCommanders.containsKey(block))
            return subCommanders[block]!!

        log.info("creating sub commander for ${block.name}")
        val repo = repositoryCommander.createRepoCommanderForBlock(block)
        subCommanders[block] = repo
        log.info("sub commander created, $repo")

        return repo
    }

    private var _session: PipelineSession = PipelineSession()

    var rootBlock: PipelineBlock<*, *>? = null

    override val session: IPipelineSession
        get() = _session

    fun <T : Data> run(initialData: T) {
        if (rootBlock != null) {
            //TODO: (avlomakin) check if there is better way to call method of out-projected class
            (rootBlock as PipelineBlock<*, *>)::inputReady.call(this, initialData)
        } else
            throw PipelineException("root node is not initialized")
    }

    fun flush() {
        _session = PipelineSession()
    }

    override fun getCachedDataFor(sender: Any, request: PipelineKeeperRequest): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}