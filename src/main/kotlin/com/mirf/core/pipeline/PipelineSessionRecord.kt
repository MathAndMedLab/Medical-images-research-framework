package com.mirf.core.pipeline

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class PipelineSessionRecord(var level: RecordLevel, val message: String, val creationTime: LocalDateTime = LocalDateTime.now()) {

    enum class RecordLevel {
        InProgress,
        Success,
        Warning,
        Error;

        override fun toString(): String {
            return when{
                this == InProgress -> "In progress"
                this == Success -> "  Success  "
                this == Warning -> "  Warning  "
                this == Error -> "   Error   "
                else               -> super.toString()
            }
        }
    }

    fun setSuccess() {
        checkState()
        level = RecordLevel.Success
    }

    fun setWarning() {
        checkState()
        level = RecordLevel.Warning
    }

    fun setError() {
        checkState()
        level = RecordLevel.Error
    }

    private fun checkState() {
        if(level != RecordLevel.InProgress)
            throw PipelineException("Only ${RecordLevel.InProgress} record is allowed to change state")
    }

    override fun toString(): String {
        return "[${creationTime.format(timeFormatter)}]  :  $level   $message"
    }

    companion object {
        val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")!!
    }
}

