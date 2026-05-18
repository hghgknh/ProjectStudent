package studentsystem.ui.command;

import studentsystem.service.FileManager;

public class OpenCommand extends BaseCommand {

    private final FileManager fileManager;

    public OpenCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "open <file>");
        fileManager.open(tokens[1]);
        return true;
    }
}
