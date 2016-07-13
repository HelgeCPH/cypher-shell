package org.neo4j.shell.commands;

import org.neo4j.shell.Command;
import org.neo4j.shell.Shell;
import org.neo4j.shell.cli.CliArgHelper;
import org.neo4j.shell.exception.CommandException;
import org.neo4j.shell.exception.ExitException;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Command to connect to an instance of Neo4j.
 */
public class Connect implements Command {
    private static final String COMMAND_NAME = ":connect";

    private final Shell shell;

    public Connect(@Nonnull final Shell shell) {
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
        return "Connect to a running instance of neo4j";
    }

    @Nonnull
    @Override
    public String getUsage() {
        return "[username:password@][host][:port]";
    }

    @Nonnull
    @Override
    public String getHelp() {
        return "Connect to a running instance of neo4j. Must be in disconnected state.";
    }

    @Nonnull
    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public void execute(@Nonnull List<String> args) throws ExitException, CommandException {
        // Default arguments
        String host = "localhost";
        int port = 7687;
        String username = "";
        String password = "";

        if (args.size() > 1) {
            throw new CommandException(
                    String.format(("Too many arguments. @|bold %s|@ accepts a single optional argument.\n"
                                    + "usage: @|bold %s|@ %s"),
                            COMMAND_NAME, COMMAND_NAME, getUsage()));
        } else if (args.size() == 1) {
            Matcher m = CliArgHelper.addressArgPattern.matcher(args.get(0));
            if (!m.matches()) {
                throw new CommandException(String.format("Could not parse @|bold %s|@\nusage: @|bold %s|@ %s",
                        args.get(0), COMMAND_NAME, getUsage()));
            }

            if (null != m.group("host")) {
                host = m.group("host");
            }
            if (null != m.group("port")) {
                // Safe, regex only matches integers
                port = Integer.parseInt(m.group("port"));
            }
            if (null != m.group("username")) {
                username = m.group("username");
            }
            if (null != m.group("password")) {
                password = m.group("password");
            }
        }

        shell.connect(host, port, username, password);
        shell.printOut("Connected to neo4j at @|bold " + host + ":" + port + "|@");
    }
}
