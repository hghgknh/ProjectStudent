package studentsystem.ui;

import studentsystem.service.FileManager;
import studentsystem.service.StudentService;
import java.util.Scanner;

/**
 * Entry point for the Student Information System.
 *
 * <p>Initialises the application components and runs the main input loop,
 * which reads lines from standard input, parses them into command tokens,
 * and dispatches them to the {@link CommandHandler} until the user types
 * {@code exit}.</p>
 */
public class Main {

    /**
     * Starts the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        StudentService studentService = new StudentService(fileManager);
        CommandParser parser = new CommandParser();
        CommandHandler handler = new CommandHandler(fileManager, studentService);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Student Information System");
        System.out.println("Type 'help' for a list of commands.");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            String[] tokens = parser.parse(line);
            boolean running = handler.handle(tokens);
            if (!running) break;
        }

        scanner.close();
    }
}
