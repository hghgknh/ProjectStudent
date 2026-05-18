package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class AdvanceCommand extends BaseCommand {

    private final StudentService studentService;

    public AdvanceCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "advance <fn>");
        studentService.advance(tokens[1]);
        return true;
    }
}
