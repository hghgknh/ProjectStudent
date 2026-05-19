package studentsystem.ui.command;

import studentsystem.service.FileManager;

/**
 * Handles the {@code open <file>} command.
 * Loads the specified XML file into memory. If the file does not exist,
 * a new empty file is created at that path.
 */
public class OpenCommand extends BaseCommand {

    private final FileManager fileManager;

    /**
     * @param fileManager file manager used to open the file
     */
    public OpenCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * @param tokens {@code tokens[1]} — path to the file to open
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "open <file>");
        fileManager.open(tokens[1]);
        return true;
    }
}
