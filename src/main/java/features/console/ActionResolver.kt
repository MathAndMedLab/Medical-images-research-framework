package features.console

import java.util.function.Consumer

object ActionResolver {

    fun getAction(action: CommandAction): Consumer<ConsoleContext> {
        when (action) {
            CommandAction.PrintHelp -> return Consumer { printHelp(it) }
            CommandAction.AddBlockToPipeline -> return Consumer { addBlock(it) }
            else -> throw ConsoleException(String.format("Action: %s not supported", action))
        }
    }

    private fun addBlock(consoleContext: ConsoleContext) {

    }

    fun printHelp(ctx: ConsoleContext) {
        val result = CommandParser.commands.map { x -> x.name + " - " + x.description + System.lineSeparator() }.joinToString { x -> x }
        print(result)
    }
}