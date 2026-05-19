package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code enrollin <fn> <course>} command.
 * Enrols the student in a discipline for their current year.
 */
public class EnrollInCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public EnrollInCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} faculty number, {@code tokens[2]} discipline name
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 3, "enrollin <fn> <course>");
        studentService.enrollIn(tokens[1], tokens[2]);
        return true;
    }
}
