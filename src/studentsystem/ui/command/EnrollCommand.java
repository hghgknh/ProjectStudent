package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code enroll <fn> <program> <group> <name>} command.
 * Enrols a new student in year 1 of the given specialty and group.
 */
public class EnrollCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public EnrollCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} faculty number, {@code tokens[2]} specialty, {@code tokens[3]} group, {@code tokens[4]} full name
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 5, "enroll <fn> <program> <group> <name>");
        studentService.enroll(tokens[1], tokens[2], tokens[3], tokens[4]);
        return true;
    }
}
