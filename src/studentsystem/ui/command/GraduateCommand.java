package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class GraduateCommand extends BaseCommand {

    private final StudentService studentService;

    public GraduateCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "graduate <fn>");
        studentService.graduate(tokens[1]);
        return true;
    }
}
