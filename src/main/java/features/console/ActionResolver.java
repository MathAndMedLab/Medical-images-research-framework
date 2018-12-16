package features.console;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ActionResolver {

    public static Consumer<ConsoleContext> getAction(CommandAction action) {
        switch (action) {
            case PrintHelp:
                return ActionResolver::printHelp;
            case AddBlockToPipeline:
                return ActionResolver::addBlock;
            default:
                throw new ConsoleException(String.format("Action: %s not supported", action));
        }
    }

    private static void addBlock(ConsoleContext consoleContext) {

    }

    public static void printHelp(ConsoleContext ctx)
    {
        var result = CommandParser.commands.stream().map(x -> x.name + " - " + x.description + System.lineSeparator()).collect(Collectors.joining());
        System.out.print(result);
    }
}