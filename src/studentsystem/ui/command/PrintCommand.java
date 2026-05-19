package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code print <fn>} command.
 * Prints a short summary of the student's personal data and GPA.
 */
public class PrintCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public PrintCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} faculty number of the student to print
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "print <fn>");
        studentService.print(tokens[1]);
        return true;
    }
}
