package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code interrupt <fn>} command.
 * Suspends the student's enrolment rights.
 */
public class InterruptCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public InterruptCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} faculty number of the student to interrupt
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "interrupt <fn>");
        studentService.interrupt(tokens[1]);
        return true;
    }
}
