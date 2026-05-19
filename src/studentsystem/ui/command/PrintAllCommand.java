package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code printall <program> <year>} command.
 * Lists all students in the given specialty and year.
 */
public class PrintAllCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public PrintAllCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} specialty name, {@code tokens[2]} study year
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 3, "printall <program> <year>");
        studentService.printAll(tokens[1], Integer.parseInt(tokens[2]));
        return true;
    }
}
