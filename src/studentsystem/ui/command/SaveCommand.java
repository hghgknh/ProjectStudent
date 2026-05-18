package studentsystem.ui.command;

import studentsystem.service.FileManager;

public class SaveCommand extends BaseCommand {

    private final FileManager fileManager;

    public SaveCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean execute(String[] tokens) {
        fileManager.save();
        return true;
    }
}
