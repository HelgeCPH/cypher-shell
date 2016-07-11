package org.neo4j.shell.commands;

import org.neo4j.shell.Command;
import org.neo4j.shell.CypherShell;
import org.neo4j.shell.exception.CommandException;
import org.neo4j.shell.exception.ExitException;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Command to connect to an instance of Neo4j.
 */
public class Disconnect implements Command {
    public static final String COMMAND_NAME = ":disconnect";

    private final CypherShell shell;

    public Disconnect(@Nonnull final CypherShell shell) {
        this.shell = shell;
    }

    @Nonnull
    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Disconnect from neo4j";
    }

    @Nonnull
    @Override
    public String getUsage() {
        return "";
    }

    @Nonnull
    @Override
    public String getHelp() {
        return "Disconnect from neo4j without quitting the shell.";
    }

    @Nonnull
    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public void execute(@Nonnull List<String> args) throws ExitException, CommandException {
        if (!args.isEmpty()) {
            throw new CommandException(
                    String.format(("Too many arguments. @|bold %s|@ does not accept any arguments"),
                            COMMAND_NAME));
        }

        shell.disconnect();
        shell.printOut("Disconnected");
    }
}
