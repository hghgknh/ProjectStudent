package studentsystem.ui;

import studentsystem.exception.FileException;
import studentsystem.exception.StudentException;
import studentsystem.service.FileManager;
import studentsystem.service.StudentService;
import studentsystem.ui.command.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Dispatches user input to the appropriate {@link Command} implementation.
 *
 * <p>On construction, every supported command is registered in an internal
 * map keyed by its name (e.g. {@code "enroll"}, {@code "advance"}).
 * When {@link #handle(String[])} is called, it looks up the command by the
 * first token, delegates execution, and catches any exceptions thrown by the
 * business or file layer, printing them as human-readable error messages.</p>
 */
public class CommandHandler {

    /**
     * Registry mapping command names (lower-case) to their implementations.
     */
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Constructs the handler and registers all supported commands.
     *
     * @param fileManager    manager that provides access to the open file
     * @param studentService service containing all student business logic
     */
    public CommandHandler(FileManager fileManager, StudentService studentService) {
        commands.put("open",      new OpenCommand(fileManager));
        commands.put("close",     new CloseCommand(fileManager));
        commands.put("save",      new SaveCommand(fileManager));
        commands.put("saveas",    new SaveAsCommand(fileManager));
        commands.put("help",      new HelpCommand());
        commands.put("exit",      new ExitCommand());

        commands.put("enroll",    new EnrollCommand(studentService));
        commands.put("advance",   new AdvanceCommand(studentService));
        commands.put("change",    new ChangeCommand(studentService));
        commands.put("graduate",  new GraduateCommand(studentService));
        commands.put("interrupt", new InterruptCommand(studentService));
        commands.put("resume",    new ResumeCommand(studentService));
        commands.put("print",     new PrintCommand(studentService));
        commands.put("printall",  new PrintAllCommand(studentService));
        commands.put("enrollin",  new EnrollInCommand(studentService));
        commands.put("addgrade",  new AddGradeCommand(studentService));
        commands.put("protocol",  new ProtocolCommand(studentService));
        commands.put("report",    new ReportCommand(studentService));
    }

    /**
     * Looks up and executes the command represented by {@code tokens[0]}.
     *
     * <p>Any {@link StudentException}, {@link FileException}, or
     * {@link NumberFormatException} thrown during execution is caught and
     * printed as an error message rather than propagating to the caller.</p>
     *
     * @param tokens parsed tokens from the user's input; may be empty
     * @return {@code true} to keep the application running,
     *         {@code false} if the user typed {@code exit}
     */
    public boolean handle(String[] tokens) {
        if (tokens.length == 0) return true;

        String name = tokens[0].toLowerCase();
        Command command = commands.get(name);

        if (command == null) {
            System.out.println("Unknown command: " + name + ". Type help for a list of commands.");
            return true;
        }

        try {
            return command.execute(tokens);
        } catch (StudentException | FileException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid numeric argument.");
        }

        return true;
    }
}
