package com.mirf.core.pipeline

import com.mirf.core.common.EventManager

open class PipelineSession : IPipelineSession {

    private val _records = arrayListOf<PipelineSessionRecord>()

    val records
        get() = _records as List<PipelineSessionRecord>

    private  val onNewRecord: EventManager<PipelineSessionRecord> = EventManager()

    override val newRecord
        get() = onNewRecord.event

    override fun addSuccess(message: String) = addInternal(PipelineSessionRecord(PipelineSessionRecord.RecordLevel.Success, message))

    override fun addWarning(message: String) = addInternal(PipelineSessionRecord(PipelineSessionRecord.RecordLevel.Warning, message))

    override fun addError(message: String) = addInternal(PipelineSessionRecord(PipelineSessionRecord.RecordLevel.Error, message))

    override fun addNew(message: String) : PipelineSessionRecord {
        val result = PipelineSessionRecord(PipelineSessionRecord.RecordLevel.InProgress, message)
        addInternal(result)
        return result
    }

    private fun addInternal(record: PipelineSessionRecord) {
        //TODO: (avlomakin) add concurrent write
        _records.add(record)
        onNewRecord(this, record)
    }
}