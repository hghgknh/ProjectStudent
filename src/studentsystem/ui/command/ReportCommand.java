package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class ReportCommand extends BaseCommand {

    private final StudentService studentService;

    public ReportCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "report <fn>");
        studentService.report(tokens[1]);
        return true;
    }
}
