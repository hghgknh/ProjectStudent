package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code advance <fn>} command.
 * Advances the student to the next year of study if they meet the exam requirements.
 */
public class AdvanceCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public AdvanceCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} faculty number of the student to advance
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "advance <fn>");
        studentService.advance(tokens[1]);
        return true;
    }
}
