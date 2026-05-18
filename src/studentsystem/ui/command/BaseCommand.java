package studentsystem.ui.command;

import studentsystem.exception.StudentException;

public abstract class BaseCommand implements Command {

    protected void requireArgs(String[] tokens, int required, String usage) {
        if (tokens.length < required) {
            throw new StudentException("Недостатъчно аргументи. Използване: " + usage);
        }
    }
}
