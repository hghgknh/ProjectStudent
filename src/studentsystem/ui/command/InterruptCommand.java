package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class InterruptCommand extends BaseCommand {

    private final StudentService studentService;

    public InterruptCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "interrupt <fn>");
        studentService.interrupt(tokens[1]);
        return true;
    }
}
