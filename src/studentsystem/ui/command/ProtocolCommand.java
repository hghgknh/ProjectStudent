package studentsystem.ui.command;

import studentsystem.service.StudentService;

/**
 * Handles the {@code protocol <course>} command.
 * Prints an exam protocol for the given discipline, sorted by faculty number.
 */
public class ProtocolCommand extends BaseCommand {

    private final StudentService studentService;

    /**
     * @param studentService service used to execute the command logic
     */
    public ProtocolCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param tokens {@code tokens[1]} discipline name
     */
    @Override
    public boolean execute(String[] tokens) {
        requireArgs(tokens, 2, "protocol <course>");
        studentService.protocol(tokens[1]);
        return true;
    }
}
