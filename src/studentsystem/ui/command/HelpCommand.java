package studentsystem.ui.command;

/**
 * Handles the {@code help} command.
 * Prints a formatted list of all supported commands and their usage to
 * standard output.
 */
public class HelpCommand extends BaseCommand {

    /**
     * @param tokens no arguments required
     */
    @Override
    public boolean execute(String[] tokens) {
        System.out.println("The following commands are supported:");
        System.out.printf("%-38s %s%n", "open <file>",                       "opens a file");
        System.out.printf("%-38s %s%n", "close",                             "closes the currently opened file");
        System.out.printf("%-38s %s%n", "save",                              "saves the currently open file");
        System.out.printf("%-38s %s%n", "saveas <file>",                     "saves the currently open file in <file>");
        System.out.printf("%-38s %s%n", "help",                              "prints this information");
        System.out.printf("%-38s %s%n", "exit",                              "exits the program");
        System.out.printf("%-38s %s%n", "enroll <fn> <prog> <gr> <name>",   "enrolls a student");
        System.out.printf("%-38s %s%n", "advance <fn>",                      "advances student to next year");
        System.out.printf("%-38s %s%n", "change <fn> <opt> <val>",           "changes program, group or year");
        System.out.printf("%-38s %s%n", "graduate <fn>",                     "graduates a student");
        System.out.printf("%-38s %s%n", "interrupt <fn>",                    "interrupts a student");
        System.out.printf("%-38s %s%n", "resume <fn>",                       "resumes an interrupted student");
        System.out.printf("%-38s %s%n", "print <fn>",                        "prints student info");
        System.out.printf("%-38s %s%n", "printall <prog> <year>",            "prints all students in program and year");
        System.out.printf("%-38s %s%n", "enrollin <fn> <course>",            "enrolls student in a discipline");
        System.out.printf("%-38s %s%n", "addgrade <fn> <course> <grade>",    "adds a grade");
        System.out.printf("%-38s %s%n", "protocol <course>",                 "prints protocol for a discipline");
        System.out.printf("%-38s %s%n", "report <fn>",                       "prints academic report for student");
        return true;
    }
}
