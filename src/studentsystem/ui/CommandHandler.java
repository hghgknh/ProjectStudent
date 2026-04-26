package studentsystem.ui;

import studentsystem.exception.FileException;
import studentsystem.exception.StudentException;
import studentsystem.service.FileManager;
import studentsystem.service.StudentService;

public class CommandHandler {
    private final FileManager fileManager;
    private final StudentService studentService;

    public CommandHandler(FileManager fileManager, StudentService studentService) {
        this.fileManager = fileManager;
        this.studentService = studentService;
    }

    public boolean handle(String[] tokens) {
        if (tokens.length == 0) return true;

        String command = tokens[0].toLowerCase();

        try {
            switch (command) {
                case "open":
                    requireArgs(tokens, 2, "open <file>");
                    fileManager.open(tokens[1]);
                    break;

                case "close":
                    fileManager.close();
                    break;

                case "save":
                    fileManager.save();
                    break;

                case "saveas":
                    requireArgs(tokens, 2, "saveas <file>");
                    fileManager.saveAs(tokens[1]);
                    break;

                case "help":
                    printHelp();
                    break;

                case "exit":
                    System.out.println("Exiting the program...");
                    return false;

                case "enroll":
                    requireArgs(tokens, 5, "enroll <fn> <program> <group> <name>");
                    studentService.enroll(tokens[1], tokens[2], tokens[3], tokens[4]);
                    break;

                case "advance":
                    requireArgs(tokens, 2, "advance <fn>");
                    studentService.advance(tokens[1]);
                    break;

                case "change":
                    requireArgs(tokens, 4, "change <fn> <option> <value>");
                    studentService.change(tokens[1], tokens[2], tokens[3]);
                    break;

                case "graduate":
                    requireArgs(tokens, 2, "graduate <fn>");
                    studentService.graduate(tokens[1]);
                    break;

                case "interrupt":
                    requireArgs(tokens, 2, "interrupt <fn>");
                    studentService.interrupt(tokens[1]);
                    break;

                case "resume":
                    requireArgs(tokens, 2, "resume <fn>");
                    studentService.resume(tokens[1]);
                    break;

                case "print":
                    requireArgs(tokens, 2, "print <fn>");
                    studentService.print(tokens[1]);
                    break;

                case "printall":
                    requireArgs(tokens, 3, "printall <program> <year>");
                    studentService.printAll(tokens[1], Integer.parseInt(tokens[2]));
                    break;

                case "enrollin":
                    requireArgs(tokens, 3, "enrollin <fn> <course>");
                    studentService.enrollIn(tokens[1], tokens[2]);
                    break;

                case "addgrade":
                    requireArgs(tokens, 4, "addgrade <fn> <course> <grade>");
                    studentService.addGrade(tokens[1], tokens[2], Double.parseDouble(tokens[3]));
                    break;

                case "protocol":
                    requireArgs(tokens, 2, "protocol <course>");
                    studentService.protocol(tokens[1]);
                    break;

                case "report":
                    requireArgs(tokens, 2, "report <fn>");
                    studentService.report(tokens[1]);
                    break;

                default:
                    System.out.println("Непозната команда: " + command + ". Въведете help за списък с команди.");
            }
        } catch (StudentException | FileException e) {
            System.out.println("Грешка: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Грешка: Невалиден числов аргумент.");
        }

        return true;
    }

    private void requireArgs(String[] tokens, int required, String usage) {
        if (tokens.length < required) {
            throw new StudentException("Недостатъчно аргументи. Използване: " + usage);
        }
    }

    private void printHelp() {
        System.out.println("The following commands are supported:");
        System.out.printf("%-30s %s%n", "open <file>",            "opens a file");
        System.out.printf("%-30s %s%n", "close",                  "closes the currently opened file");
        System.out.printf("%-30s %s%n", "save",                   "saves the currently open file");
        System.out.printf("%-30s %s%n", "saveas <file>",          "saves the currently open file in <file>");
        System.out.printf("%-30s %s%n", "help",                   "prints this information");
        System.out.printf("%-30s %s%n", "exit",                   "exits the program");
        System.out.printf("%-30s %s%n", "enroll <fn> <prog> <gr> <name>", "enrolls a student");
        System.out.printf("%-30s %s%n", "advance <fn>",           "advances student to next year");
        System.out.printf("%-30s %s%n", "change <fn> <opt> <val>","changes program, group or year");
        System.out.printf("%-30s %s%n", "graduate <fn>",          "graduates a student");
        System.out.printf("%-30s %s%n", "interrupt <fn>",         "interrupts a student");
        System.out.printf("%-30s %s%n", "resume <fn>",            "resumes an interrupted student");
        System.out.printf("%-30s %s%n", "print <fn>",             "prints student info");
        System.out.printf("%-30s %s%n", "printall <prog> <year>", "prints all students in program and year");
        System.out.printf("%-30s %s%n", "enrollin <fn> <course>", "enrolls student in a discipline");
        System.out.printf("%-30s %s%n", "addgrade <fn> <course> <grade>", "adds a grade");
        System.out.printf("%-30s %s%n", "protocol <course>",      "prints protocol for a discipline");
        System.out.printf("%-30s %s%n", "report <fn>",            "prints academic report for student");
    }
}
