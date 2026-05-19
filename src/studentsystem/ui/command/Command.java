package studentsystem.ui.command;

/**
 * Represents a single executable command in the application's
 * command-line interface.
 *
 * <p>Each implementation handles one command (e.g. {@code enroll},
 * {@code advance}, {@code print}) and is registered in
 * {@link studentsystem.ui.CommandHandler} by its keyword. When the user
 * types a command, the handler looks it up in the registry and delegates
 * to {@link #execute(String[])}.</p>
 */
public interface Command {

    /**
     * Executes this command with the given token array.
     *
     * <p>{@code tokens[0]} is always the command name itself; arguments
     * start at index 1.</p>
     *
     * @param tokens parsed tokens from the user's input line,
     *               where {@code tokens[0]} is the command name
     * @return {@code true} to keep the application running,
     *         {@code false} to signal that the application should exit
     *         (used only by {@link ExitCommand})
     */
    boolean execute(String[] tokens);
}
