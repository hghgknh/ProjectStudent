package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class ResumeCommand extends BaseCommand {

    private final StudentService studentService;

    public ResumeCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "resume <fn>");
        studentService.resume(tokens[1]);
        return true;
    }
}
