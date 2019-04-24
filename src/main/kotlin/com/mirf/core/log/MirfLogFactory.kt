package com.mirf.core.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MirfLogFactory {

    val currentLogger : Logger
        get() {
            return LoggerFactory.getLogger("FILE")
        }
}