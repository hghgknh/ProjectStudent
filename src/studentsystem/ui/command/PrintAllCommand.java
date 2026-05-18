package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class PrintAllCommand extends BaseCommand {

    private final StudentService studentService;

    public PrintAllCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 3, "printall <program> <year>");
        studentService.printAll(tokens[1], Integer.parseInt(tokens[2]));
        return true;
    }
}
