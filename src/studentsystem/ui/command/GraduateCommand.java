package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code graduate <fn>} command.
 * Marks the student as graduated if they have met all requirements.
 */
public class GraduateCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public GraduateCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} faculty number of the student to graduate
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "graduate <fn>");
        studentService.graduate(tokens[1]);
        return true;
    }
}
