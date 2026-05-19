package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code resume <fn>} command.
 * Restores the enrolment rights of an interrupted student.
 */
public class ResumeCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public ResumeCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} faculty number of the student to resume
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "resume <fn>");
        studentService.resume(tokens[1]);
        return true;
    }
}
