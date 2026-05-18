package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class EnrollCommand extends BaseCommand {

    private final StudentService studentService;

    public EnrollCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 5, "enroll <fn> <program> <group> <name>");
        studentService.enroll(tokens[1], tokens[2], tokens[3], tokens[4]);
        return true;
    }
}
