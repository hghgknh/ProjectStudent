package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code report <fn>} command.
 * Prints a full academic transcript for the student.
 */
public class ReportCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public ReportCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} faculty number of the student
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "report <fn>");
        studentService.report(tokens[1]);
        return true;
    }
}
