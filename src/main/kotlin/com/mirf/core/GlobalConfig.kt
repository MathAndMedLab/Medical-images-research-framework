package com.mirf.core

import com.mirf.core.log.MirfLogFactory
import org.slf4j.Logger
import java.util.*


object GlobalConfig {

    private val log: Logger = MirfLogFactory.currentLogger

    private val options: HashMap<String, Any> = hashMapOf()

    init {
        val mirfConfig = javaClass.getResourceAsStream("/mirf.properties")
        val props = Properties()
        props.load(mirfConfig)
        for (name in props.propertyNames().toList()) {
            val value = props.getProperty(name as String)
            options[name] = value
        }
    }

    fun <T> get(name: String): T {
        val value = options[name] ?: throw ConfigException("no value for $name provided")
        return value as T
    }

    fun add(key: String, value: String) {
        options[key] = value
        log.info("[Global config] new property has been added, $key : $value")
    }
}