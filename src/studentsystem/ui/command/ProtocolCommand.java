package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class ProtocolCommand extends BaseCommand {

    private final StudentService studentService;

    public ProtocolCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "protocol <course>");
        studentService.protocol(tokens[1]);
        return true;
    }
}
