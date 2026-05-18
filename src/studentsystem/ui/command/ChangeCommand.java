package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class ChangeCommand extends BaseCommand {

    private final StudentService studentService;

    public ChangeCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 4, "change <fn> <option> <value>");
        studentService.change(tokens[1], tokens[2], tokens[3]);
        return true;
    }
}
