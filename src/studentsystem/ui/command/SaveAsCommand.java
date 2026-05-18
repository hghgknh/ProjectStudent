package studentsystem.ui.command;

import studentsystem.service.FileManager;

public class SaveAsCommand extends BaseCommand {

    private final FileManager fileManager;

    public SaveAsCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "saveas <file>");
        fileManager.saveAs(tokens[1]);
        return true;
    }
}
