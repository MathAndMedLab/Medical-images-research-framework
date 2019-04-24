package com.mirf.features.console

import java.util.Scanner

object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        print("welcome to MIRF console interface" + System.lineSeparator())
        val keyboard = Scanner(System.`in`)
        var exit = false
        while (!exit) {
            val input = keyboard.nextLine() ?: continue

            if (input == "q")
                exit = true
            else
                CommandParser.tryExecute(input, null)

        }
        keyboard.close()
    }
}
