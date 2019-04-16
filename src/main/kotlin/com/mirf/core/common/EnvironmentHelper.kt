package com.mirf.core.common

import java.io.File

object EnvironmentHelper {

    fun getCurrentJarDirectory(): File =
            File(ClassLoader.getSystemClassLoader().getResource(".").path)
}