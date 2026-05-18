package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class PrintCommand extends BaseCommand {

    private final StudentService studentService;

    public PrintCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "print <fn>");
        studentService.print(tokens[1]);
        return true;
    }
}
