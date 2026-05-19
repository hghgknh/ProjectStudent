package studentsystem.ui.command;

/**
 * Handles the {@code exit} command.
 * Terminates the application by returning {@code false} from
 * {@link #execute(String[])}, which signals the main loop to stop.
 */
public class ExitCommand extends BaseCommand {

    /**
     * @param tokens no arguments required
     * @return {@code false} to signal the application to exit
     */
    @Override
    public boolean execute(String[] tokens) {
        System.out.println("Exiting the program...");
        return false;
    }
}
