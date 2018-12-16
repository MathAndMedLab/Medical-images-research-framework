package features.console;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.function.Consumer;

public class Command {
    static final String COMMAND_TAG = "Command";
    private static final String NAME_TAG = "Name";
    public static final String SHORTCUTS_TAG = "Shortcuts";
    private static final String DESCRIPTION_TAG = "Description";
    private static final String ACTION_TAG = "Action";

    public final String name;
    public final String description;
    public final String[] shortcuts;
    public final Consumer<ConsoleContext> action;

    private Command(CommandAction action, String name, String description, String[] shortcuts) {
        this.name = name;
        this.description = description;
        this.shortcuts = shortcuts;
        this.action = ActionResolver.getAction(action);
    }

    static Command parse(Node commandNode) {
        var element = (Element)commandNode;
        String name = element.getAttribute(NAME_TAG);
        String descpiton = element.getAttribute(DESCRIPTION_TAG);
        CommandAction action = CommandAction.valueOf(element.getAttribute(ACTION_TAG));

        return new Command(action, name, descpiton, new String[0]);
    }

    public void execute(ConsoleContext args)
    {
        action.accept(args);
    }
}
