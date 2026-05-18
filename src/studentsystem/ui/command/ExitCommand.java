package studentsystem.ui.command;

public class ExitCommand extends BaseCommand {

    @Override
    public boolean execute(String[] tokens) {
        System.out.println("Exiting the program...");
        return false;
    }
}
