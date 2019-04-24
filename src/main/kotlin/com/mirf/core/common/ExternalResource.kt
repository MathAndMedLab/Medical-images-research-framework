package com.mirf.core.common

import com.mirf.core.GlobalConfig
import java.io.File
import java.util.*

class ExternalResource(private val properties: Properties) {


    companion object {
        fun load(file: File): ExternalResource {
            val prop = Properties()

            prop.load(file.inputStream())
            prop.loadDataToConfig()

            return ExternalResource(prop)
        }

        private fun Properties.loadDataToConfig() {
            fun addToGlobal(propKey: String) {
                val value = this.getProperty(propKey)
                GlobalConfig.add(propKey.removePrefix("GLOBAL_"), value)
            }

            fun String.isGlobalConfigProp() = this.startsWith("GLOBAL_")

            for (name in this.propertyNames().toList()) {
                (name as String).let{
                    if (it.isGlobalConfigProp())
                        addToGlobal(it)
                }
            }
        }
    }
}
