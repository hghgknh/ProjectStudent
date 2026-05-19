package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code change <fn> <option> <value>} command.
 * Changes one of the student's attributes: {@code program}, {@code group}, or {@code year}.
 */
public class ChangeCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public ChangeCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} faculty number, {@code tokens[2]} option (program/group/year), {@code tokens[3]} new value
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 4, "change <fn> <option> <value>");
        studentService.change(tokens[1], tokens[2], tokens[3]);
        return true;
    }
}
