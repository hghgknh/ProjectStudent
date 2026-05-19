package studentsystem.ui.command;

import studentsystem.service.FileManager;

/**
 * Handles the {@code close} command.
 * Closes the currently open file and discards all unsaved changes.
 */
public class CloseCommand extends BaseCommand {

    private final FileManager fileManager;

    /**
     * @param fileManager file manager used to close the file
     */
    public CloseCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * @param tokens no arguments required
     */
    @Override
    public boolean execute(String[] tokens) {
        fileManager.close();
        return true;
    }
}
