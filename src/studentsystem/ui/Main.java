package studentsystem.ui;

import studentsystem.service.FileManager;
import studentsystem.service.StudentService;
import java.util.Scanner;

public class Main {
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
