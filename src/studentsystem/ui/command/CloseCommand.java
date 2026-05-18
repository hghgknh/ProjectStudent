package studentsystem.ui.command;

import studentsystem.service.FileManager;

public class CloseCommand extends BaseCommand {

    private final FileManager fileManager;

    public CloseCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean execute(String[] tokens) {
        fileManager.close();
        return true;
    }
}
