package studentsystem.ui.command;

import studentsystem.service.FileManager;

/**
 * Handles the {@code saveas <file>} command.
 * Writes all in-memory changes to the specified file path.
 * Subsequent {@code save} calls will write to the new path.
 */
public class SaveAsCommand extends BaseCommand {

    private final FileManager fileManager;

    /**
     * @param fileManager file manager used to save the file
     */
    public SaveAsCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * @param tokens {@code tokens[1]} — destination file path
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "saveas <file>");
        fileManager.saveAs(tokens[1]);
        return true;
    }
}
