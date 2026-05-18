package studentsystem.ui.command;

import studentsystem.service.StudentService;

public class AddGradeCommand extends BaseCommand {

    private final StudentService studentService;

    public AddGradeCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 4, "addgrade <fn> <course> <grade>");
        studentService.addGrade(tokens[1], tokens[2], Double.parseDouble(tokens[3]));
        return true;
    }
}
