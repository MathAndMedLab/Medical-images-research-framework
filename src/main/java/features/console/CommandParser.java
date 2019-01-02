package features.console;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static features.console.utils.XmlUtil.asList;

public final class CommandParser {

    static List<Command> commands;

    static {
        try {
            commands = readCommands();
        } catch (Exception e) {
            throw new ConsoleException("Error parsing config file");
        }
    }

    private static List<Command> readCommands() throws ParserConfigurationException, IOException, SAXException {

        File inputFile = new File("commands.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList commandNodes = doc.getElementsByTagName(Command.COMMAND_TAG);
        var result = new ArrayList<Command>();

        for (var commandNode : asList(commandNodes)){
            result.add(Command.parse(commandNode));
        }

        return result;
    }

    public static void tryExecute(String commandString, CustomPipeline pipeline){
        var args = commandString.split(" ");
        Optional<Command> first = commands.stream().filter(x -> x.name.equals(args[0])).findFirst();

        if(first.isEmpty())
            throw new ConsoleException("unable to find command");

        ConsoleContext ctx = new ConsoleContext(Arrays.copyOfRange(args, 1, args.length), pipeline);
        first.get().execute(ctx);
    }
}
