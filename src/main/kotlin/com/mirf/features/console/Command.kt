package com.mirf.features.console

import org.w3c.dom.Element
import org.w3c.dom.Node

import java.util.function.Consumer

class Command private constructor(action: CommandAction, val name: String, val description: String) {

    private val action: Consumer<ConsoleContext> = ActionResolver.getAction(action)

    fun execute(args: ConsoleContext) {
        action.accept(args)
    }

    companion object {
        internal const val COMMAND_TAG = "Command"
        private const val NAME_TAG = "Name"
        private const val DESCRIPTION_TAG = "Description"
        private const val ACTION_TAG = "Action"

        internal fun parse(commandNode: Node): Command {
            val element = commandNode as Element
            val name = element.getAttribute(NAME_TAG)
            val description = element.getAttribute(DESCRIPTION_TAG)
            val action = CommandAction.valueOf(element.getAttribute(ACTION_TAG))

            return Command(action, name, description)
        }
    }
}
