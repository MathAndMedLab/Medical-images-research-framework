package core.pipeline

import core.common.Event

interface IPipelineSession {
    val newRecord: Event<PipelineSessionRecord>

    fun addSuccess(message: String)
    fun addWarning(message: String)
    fun addError(message: String)
    fun addNew(message: String): PipelineSessionRecord
}