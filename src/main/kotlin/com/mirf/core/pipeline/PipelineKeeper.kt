package com.mirf.core.pipeline

interface PipelineKeeper {

    fun getCachedDataFor(sender: Any, request: PipelineKeeperRequest): Any?

    val session: IPipelineSession
}