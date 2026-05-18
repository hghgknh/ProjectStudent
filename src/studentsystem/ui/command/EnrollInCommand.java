package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class EnrollInCommand extends BaseCommand {

    private final StudentService studentService;

    public EnrollInCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 3, "enrollin <fn> <course>");
        studentService.enrollIn(tokens[1], tokens[2]);
        return true;
    }
}
