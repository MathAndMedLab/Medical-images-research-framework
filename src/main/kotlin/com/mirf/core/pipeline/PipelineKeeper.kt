package com.mirf.core.pipeline

import com.mirf.core.repository.RepositoryCommander
import com.mirf.features.repository.LocalRepositoryCommander

interface PipelineKeeper {

    fun getCachedDataFor(sender: Any, request: PipelineKeeperRequest): Any?

    fun getRepositoryCommander(block: PipelineBlock<*, *>): LocalRepositoryCommander

    val session: IPipelineSession
}