package com.mirf.features.console

import com.mirf.core.pipeline.PipelineBlock
import com.mirf.features.console.utils.asList
import org.xml.sax.SAXException

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import java.io.File
import java.io.IOException
import java.util.ArrayList

object CommandParser {

    internal var commands: List<Command>

    init {
        try {
            commands = readCommands()
        } catch (e: Exception) {
            throw ConsoleException("Error parsing config file")
        }

    }

    @Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
    private fun readCommands(): List<Command> {

        val inputFile = File("commands.xml")
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val doc = dBuilder.parse(inputFile)
        doc.documentElement.normalize()

        val commandNodes = doc.getElementsByTagName(Command.COMMAND_TAG)
        val result = ArrayList<Command>()

        for (commandNode in commandNodes.asList()) {
            result.add(Command.parse(commandNode))
        }

        return result
    }

    fun tryExecute(commandString: String, pipeline: PipelineBlock<*, *>?) {
        val args = commandString.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val command = commands.first { x -> x.name == args[0] }

        val ctx = ConsoleContext(args, pipeline)
        command.execute(ctx)
    }
}
