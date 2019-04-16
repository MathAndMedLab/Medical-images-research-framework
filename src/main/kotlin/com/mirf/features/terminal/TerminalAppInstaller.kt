package com.mirf.features.terminal

interface TerminalAppInstaller {
    fun checkVersion()
    fun installOrUpdate()
    fun getCommand() : String
}