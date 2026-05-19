package studentsystem.ui.command;

import studentsystem.exception.StudentException;

/**
 * Abstract base class for all command implementations.
 *
 * <p>Provides shared utility methods available to every concrete command,
 * keeping the individual command classes free of boilerplate.</p>
 */
public abstract class BaseCommand implements Command {

    /**
     * Validates that the token array contains at least the required number of
     * elements. Throws a {@link StudentException} with a usage hint if not.
     *
     * @param tokens   token array passed to {@link #execute(String[])}
     * @param required minimum number of tokens expected (including the command name)
     * @param usage    usage string shown in the error message, e.g. {@code "enroll <fn> <program> <group> <name>"}
     * @throws StudentException if {@code tokens.length < required}
     */
    protected void requireArgs(String[] tokens, int required, String usage) {
        if (tokens.length < required) {
            throw new StudentException("Insufficient arguments. Usage: " + usage);
        }
    }
}
