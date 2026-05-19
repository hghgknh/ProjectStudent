package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code addgrade <fn> <course> <grade>} command.
 * Records a numeric grade for the student in the given discipline.
 */
public class AddGradeCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public AddGradeCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} faculty number, {@code tokens[2]} discipline name, {@code tokens[3]} numeric grade (2.00-6.00)
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 4, "addgrade <fn> <course> <grade>");
        studentService.addGrade(tokens[1], tokens[2], Double.parseDouble(tokens[3]));
        return true;
    }
}
