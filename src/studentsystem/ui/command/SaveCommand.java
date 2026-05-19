package studentsystem.ui.command;

import studentsystem.service.FileManager;

/**
 * Handles the {@code save} command.
 * Writes all in-memory changes back to the file that was originally opened.
 */
public class SaveCommand extends BaseCommand {

    private final FileManager fileManager;

    /**
     * @param fileManager file manager used to save the file
     */
    public SaveCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * @param tokens no arguments required
     */
    @Override
    public boolean execute(String[] tokens) {
        fileManager.save();
        return true;
    }
}
