package studentsystem.ui;

import studentsystem.exception.FileException;
import studentsystem.exception.StudentException;
import studentsystem.service.FileManager;
import studentsystem.service.StudentService;
import studentsystem.ui.command.*;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

    private final Map<String, Command> commands = new HashMap<>();

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

    public boolean handle(String[] tokens) {
        if (tokens.length == 0) return true;

        String name = tokens[0].toLowerCase();
        Command command = commands.get(name);

        if (command == null) {
            System.out.println("Непозната команда: " + name + ". Въведете help за списък с команди.");
            return true;
        }

        try {
            return command.execute(tokens);
        } catch (StudentException | FileException e) {
            System.out.println("Грешка: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Грешка: Невалиден числов аргумент.");
        }

        return true;
    }
}
