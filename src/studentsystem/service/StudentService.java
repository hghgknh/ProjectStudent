package studentsystem.service;

import studentsystem.exception.StudentException;
import studentsystem.model.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains all business logic for student management operations.
 *
 * <p>Every public method in this class corresponds to a user command. Each
 * method fetches the necessary data from the {@link FileManager}, validates
 * business rules, mutates the in-memory {@link SystemData}, and prints a
 * confirmation or error message to standard output.</p>
 *
 * <p>Methods that operate on a specific student require that student to be
 * active (status {@link studentsystem.model.StudentStatus#ENROLLED}) unless
 * documented otherwise.</p>
 */
public class StudentService {

    /** Source of the in-memory data for the currently open file. */
    private final FileManager fileManager;

    /**
     * Constructs a StudentService backed by the given file manager.
     *
     * @param fileManager manager that provides access to the open file's data
     */
    public StudentService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Enrols a new student in year 1 of the given specialty and group.
     *
     * @param facultyNumber unique faculty number for the new student
     * @param program       name of the specialty to enrol in
     * @param group         study group identifier
     * @param name          full name of the student
     * @throws StudentException if a student with that faculty number already exists
     * @throws StudentException if the specialty does not exist
     */
    public void enroll(String facultyNumber, String program, String group, String name) {
        SystemData db = fileManager.getSystemData();

        if (db.findStudent(facultyNumber) != null) {
            throw new StudentException("Student with faculty number " + facultyNumber + " already exists.");
        }

        Specialty specialty = db.findSpecialty(program);
        if (specialty == null) {
            throw new StudentException("Specialty '" + program + "' does not exist.");
        }

        Student student = new Student(facultyNumber, name, specialty, group, 1);
        db.addStudent(student);
        System.out.println("Student " + name + " successfully enrolled in specialty " + program + ", group " + group + ".");
    }

    /**
     * Advances the student to the next year of study.
     * The student must have passed all mandatory disciplines of their current
     * year, with at most 2 failures allowed.
     *
     * @param facultyNumber faculty number of the student to advance
     * @throws StudentException if the student is not active
     * @throws StudentException if the student is already in the final year
     * @throws StudentException if the student has more than 2 failed mandatory exams
     */
    public void advance(String facultyNumber) {
        Student student = getActiveStudent(facultyNumber);
        Specialty specialty = student.getSpecialty();

        if (student.getYear() >= specialty.getTotalYears()) {
            throw new StudentException("The student is already in the final year.");
        }

        List<Discipline> mandatory = specialty.getMandatoryForYear(student.getYear());
        int failCount = 0;
        for (Discipline d : mandatory) {
            Grade g = student.findGrade(d.getName());
            if (g == null || !g.isPassed()) {
                failCount++;
            }
        }

        if (failCount > 2) {
            throw new StudentException("Student has not passed the mandatory exams. Failed: " + failCount + " (maximum 2 allowed).");
        }

        student.setYear(student.getYear() + 1);
        System.out.println("Student " + facultyNumber + " has advanced to year " + student.getYear() + ".");
    }

    /**
     * Changes one of the student's attributes: specialty (program), study group,
     * or year of study.
     *
     * <ul>
     *   <li><b>group</b> — always allowed for active students.</li>
     *   <li><b>program</b> — allowed only if the student has passed all mandatory
     *       disciplines from previous years of the new specialty.</li>
     *   <li><b>year</b> — can only advance by one; subject to the same exam
     *       pass requirement as {@link #advance(String)}.</li>
     * </ul>
     *
     * @param facultyNumber faculty number of the student to modify
     * @param option        attribute to change: {@code "program"}, {@code "group"}, or {@code "year"}
     * @param value         new value for the attribute
     * @throws StudentException if the student is not active
     * @throws StudentException if a business rule for the selected option is violated
     * @throws StudentException if {@code option} is not one of the three accepted values
     */
    public void change(String facultyNumber, String option, String value) {
        Student student = getActiveStudent(facultyNumber);
        SystemData db = fileManager.getSystemData();

        switch (option.toLowerCase()) {
            case "group":
                student.setGroup(value);
                System.out.println("Group of student " + facultyNumber + " changed to " + value + ".");
                break;

            case "program":
                Specialty newSpecialty = db.findSpecialty(value);
                if (newSpecialty == null) {
                    throw new StudentException("Specialty '" + value + "' does not exist.");
                }
                for (int y = 1; y < student.getYear(); y++) {
                    List<Discipline> mandatory = newSpecialty.getMandatoryForYear(y);
                    for (Discipline d : mandatory) {
                        Grade g = student.findGrade(d.getName());
                        if (g == null || !g.isPassed()) {
                            throw new StudentException("Student has not passed mandatory exam '" + d.getName() + "' from the new specialty.");
                        }
                    }
                }
                student.setSpecialty(newSpecialty);
                System.out.println("Specialty of student " + facultyNumber + " changed to " + value + ".");
                break;

            case "year":
                int newYear = parseYear(value);
                Specialty specialty = student.getSpecialty();
                if (newYear > student.getYear() + 1 || newYear < student.getYear()) {
                    throw new StudentException("Can only advance to the next year.");
                }
                if (newYear == student.getYear() + 1) {
                    List<Discipline> mandatory = specialty.getMandatoryForYear(student.getYear());
                    int failCount = 0;
                    for (Discipline d : mandatory) {
                        Grade g = student.findGrade(d.getName());
                        if (g == null || !g.isPassed()) failCount++;
                    }
                    if (failCount > 2) {
                        throw new StudentException("Cannot advance to the next year. Failed mandatory exams: " + failCount + ".");
                    }
                }
                student.setYear(newYear);
                System.out.println("Year of student " + facultyNumber + " changed to " + newYear + ".");
                break;

            default:
                throw new StudentException("Invalid option '" + option + "'. Use: program, group, year.");
        }
    }

    /**
     * Marks the student as graduated, provided they have passed all enrolled
     * disciplines and accumulated the required elective credits.
     *
     * @param facultyNumber faculty number of the student to graduate
     * @throws StudentException if the student is not active
     * @throws StudentException if any enrolled discipline has not been passed
     * @throws StudentException if the student has not earned enough elective credits
     */
    public void graduate(String facultyNumber) {
        Student student = getActiveStudent(facultyNumber);
        Specialty specialty = student.getSpecialty();

        for (Grade g : student.getGrades()) {
            if (!g.isPassed()) {
                throw new StudentException("Student has not passed all enrolled exams. Failed: " + g.getDisciplineName() + ".");
            }
        }

        if (specialty.getMinElectiveCredits() > 0) {
            int earned = calculateElectiveCredits(student);
            if (earned < specialty.getMinElectiveCredits()) {
                throw new StudentException("Insufficient elective credits. Required: "
                        + specialty.getMinElectiveCredits() + ", earned: " + earned + ".");
            }
        }

        student.setStatus(StudentStatus.GRADUATED);
        System.out.println("Student " + facultyNumber + " has been marked as graduated.");
    }

    /**
     * Marks the student as interrupted. An interrupted student cannot enrol in
     * disciplines, sit exams, or change their program, group, or year until
     * {@link #resume(String)} is called.
     *
     * @param facultyNumber faculty number of the student to interrupt
     * @throws StudentException if the student is not active
     */
    public void interrupt(String facultyNumber) {
        Student student = getActiveStudent(facultyNumber);
        student.setStatus(StudentStatus.INTERRUPTED);
        System.out.println("Student " + facultyNumber + " has been marked as interrupted.");
    }

    /**
     * Restores the enrolment rights of a previously interrupted student,
     * setting their status back to {@link StudentStatus#ENROLLED}.
     *
     * @param facultyNumber faculty number of the student to resume
     * @throws StudentException if the student does not exist
     * @throws StudentException if the student is not currently interrupted
     */
    public void resume(String facultyNumber) {
        Student student = getStudentByFn(facultyNumber);
        if (student.getStatus() != StudentStatus.INTERRUPTED) {
            throw new StudentException("Student " + facultyNumber + " has not interrupted.");
        }
        student.setStatus(StudentStatus.ENROLLED);
        System.out.println("Rights of student " + facultyNumber + " have been restored.");
    }

    /**
     * Enrols the student in a discipline, creating an ungraded record.
     * The discipline must exist in the student's specialty and be available
     * for their current year of study.
     *
     * @param facultyNumber faculty number of the student
     * @param courseName    name of the discipline to enrol in
     * @throws StudentException if the student is not active
     * @throws StudentException if the discipline does not exist in the student's specialty
     * @throws StudentException if the discipline is not available in the student's current year
     * @throws StudentException if the student is already enrolled in that discipline
     */
    public void enrollIn(String facultyNumber, String courseName) {
        Student student = getActiveStudent(facultyNumber);
        Specialty specialty = student.getSpecialty();

        Discipline discipline = specialty.findDiscipline(courseName);
        if (discipline == null) {
            throw new StudentException("Discipline '" + courseName + "' does not exist in specialty " + student.getProgram() + ".");
        }

        List<Discipline> currentYearDisciplines = specialty.getDisciplinesForYear(student.getYear());
        boolean availableThisYear = false;
        for (Discipline d : currentYearDisciplines) {
            if (d.getName().equalsIgnoreCase(courseName)) {
                availableThisYear = true;
                break;
            }
        }
        if (discipline.isElective() && !discipline.getAvailableInYears().isEmpty()) {
            availableThisYear = discipline.getAvailableInYears().contains(student.getYear());
        }

        if (!availableThisYear) {
            throw new StudentException("Discipline '" + courseName + "' cannot be enrolled in year " + student.getYear() + ".");
        }

        if (student.isEnrolledIn(courseName)) {
            throw new StudentException("Student is already enrolled in discipline '" + courseName + "'.");
        }

        student.addGrade(new Grade(discipline));
        System.out.println("Student " + facultyNumber + " has been enrolled in discipline '" + courseName + "'.");
    }

    /**
     * Records a grade for the student in the given discipline.
     * The student must already be enrolled in the discipline, and the grade
     * value must be in the range [2.00, 6.00].
     *
     * @param facultyNumber faculty number of the student
     * @param courseName    name of the discipline
     * @param gradeValue    numeric grade between 2.00 and 6.00
     * @throws StudentException if the student is not active
     * @throws StudentException if the grade value is outside the allowed range
     * @throws StudentException if the student is not enrolled in that discipline
     */
    public void addGrade(String facultyNumber, String courseName, double gradeValue) {
        Student student = getActiveStudent(facultyNumber);

        if (gradeValue < 2.0 || gradeValue > 6.0) {
            throw new StudentException("Invalid grade " + gradeValue + ". Grade must be between 2.00 and 6.00.");
        }

        Grade grade = student.findGrade(courseName);
        if (grade == null) {
            throw new StudentException("Student is not enrolled in discipline '" + courseName + "'.");
        }

        grade.setValue(gradeValue);
        System.out.println("Added grade " + gradeValue + " for '" + courseName + "' to student " + facultyNumber + ".");
    }

    /**
     * Prints a short summary of the student's personal data and current GPA
     * to standard output.
     *
     * @param facultyNumber faculty number of the student to print
     * @throws StudentException if the student does not exist
     */
    public void print(String facultyNumber) {
        Student s = getStudentByFn(facultyNumber);
        System.out.println("Faculty number : " + s.getFacultyNumber());
        System.out.println("Name           : " + s.getName());
        System.out.println("Specialty      : " + s.getProgram());
        System.out.println("Group          : " + s.getGroup());
        System.out.println("Year           : " + s.getYear());
        System.out.println("Status         : " + s.getStatus());
        System.out.println("GPA            : " + s.calculateAverage(true));
    }

    /**
     * Prints a list of all students in the given specialty and year to
     * standard output.
     *
     * @param program name of the specialty
     * @param year    study year to filter by
     * @throws StudentException if the specialty does not exist
     */
    public void printAll(String program, int year) {
        SystemData db = fileManager.getSystemData();
        Specialty specialty = db.findSpecialty(program);
        if (specialty == null) {
            throw new StudentException("Specialty '" + program + "' does not exist.");
        }

        System.out.println("Students in specialty " + program + ", year " + year + ":");
        System.out.println("--------------------------------------------------");

        boolean found = false;
        for (Student s : db.getStudents()) {
            if (s.getProgram().equalsIgnoreCase(program) && s.getYear() == year) {
                System.out.printf("%-10s %-25s gr.%-5s %s%n",
                        s.getFacultyNumber(), s.getName(), s.getGroup(), s.getStatus());
                found = true;
            }
        }

        if (!found) System.out.println("No students found.");
    }

    /**
     * Prints an exam protocol for the given discipline — one section per
     * specialty and year that offers the discipline. Within each section,
     * students are listed in ascending faculty-number order.
     *
     * @param courseName name of the discipline
     */
    public void protocol(String courseName) {
        SystemData db = fileManager.getSystemData();

        for (Specialty specialty : db.getSpecialties()) {
            for (int year = 1; year <= specialty.getTotalYears(); year++) {
                List<Discipline> disciplines = specialty.getDisciplinesForYear(year);
                boolean hasDiscipline = false;
                for (Discipline d : disciplines) {
                    if (d.getName().equalsIgnoreCase(courseName)) {
                        hasDiscipline = true;
                        break;
                    }
                }
                if (!hasDiscipline) continue;

                System.out.println("Protocol: " + courseName + " | " + specialty.getName() + " | Year " + year);
                System.out.println("--------------------------------------------------");

                List<Student> enrolled = db.getStudents().stream()
                        .filter(s -> s.getSpecialty() == specialty)
                        .filter(s -> s.findGrade(courseName) != null)
                        .sorted(Comparator.comparing(Student::getFacultyNumber))
                        .collect(Collectors.toList());

                if (enrolled.isEmpty()) {
                    System.out.println("No enrolled students.");
                } else {
                    for (Student s : enrolled) {
                        Grade g = s.findGrade(courseName);
                        String gradeStr = g.hasGrade() ? String.valueOf(g.getValue()) : "---";
                        System.out.printf("%-10s %-25s %s%n", s.getFacultyNumber(), s.getName(), gradeStr);
                    }
                }
                System.out.println();
            }
        }
    }

    /**
     * Prints a full academic transcript for the given student, including
     * passed exams, ungraded disciplines, both GPA variants, and elective
     * credit progress (if the specialty has a credit requirement).
     *
     * @param facultyNumber faculty number of the student
     * @throws StudentException if the student does not exist
     */
    public void report(String facultyNumber) {
        Student s = getStudentByFn(facultyNumber);
        Specialty specialty = s.getSpecialty();

        System.out.println("Academic report for: " + s.getName() + " (" + s.getFacultyNumber() + ")");
        System.out.println("Specialty: " + s.getProgram() + " | Year: " + s.getYear() + " | Group: " + s.getGroup());
        System.out.println("==================================================");

        List<Grade> passed   = s.getPassedGrades();
        List<Grade> ungraded = s.getUngradedGrades();

        if (!passed.isEmpty()) {
            System.out.println("Passed exams:");
            for (Grade g : passed) {
                System.out.printf("  %-35s %.2f%n", g.getDisciplineName(), g.getValue());
            }
        }

        if (!ungraded.isEmpty()) {
            System.out.println("Incomplete disciplines (counted as 2.00):");
            for (Grade g : ungraded) {
                System.out.printf("  %-35s ---%n", g.getDisciplineName());
            }
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("GPA (with incomplete) : %.2f%n", s.calculateAverage(true));
        System.out.printf("GPA (passed only)     : %.2f%n", s.calculateAverage(false));

        if (specialty.getMinElectiveCredits() > 0) {
            int earned    = calculateElectiveCredits(s);
            int remaining = Math.max(0, specialty.getMinElectiveCredits() - earned);
            System.out.println("--------------------------------------------------");
            System.out.println("Elective discipline credits:");
            System.out.println("  Earned   : " + earned);
            System.out.println("  Required : " + specialty.getMinElectiveCredits());
            System.out.println("  Remaining: " + remaining);
        }
    }

    /**
     * Returns the student with the given faculty number, asserting that they
     * are currently active (status {@link StudentStatus#ENROLLED}).
     *
     * @param facultyNumber faculty number to look up
     * @return the active student
     * @throws StudentException if the student does not exist or is not active
     */
    private Student getActiveStudent(String facultyNumber) {
        Student student = getStudentByFn(facultyNumber);
        if (!student.isActive()) {
            throw new StudentException("Student " + facultyNumber + " has interrupted or graduated and cannot perform this operation.");
        }
        return student;
    }

    /**
     * Returns the student with the given faculty number.
     *
     * @param facultyNumber faculty number to look up
     * @return the student
     * @throws StudentException if no student with that faculty number exists
     */
    private Student getStudentByFn(String facultyNumber) {
        SystemData db = fileManager.getSystemData();
        Student student = db.findStudent(facultyNumber);
        if (student == null) {
            throw new StudentException("Student with faculty number " + facultyNumber + " not found.");
        }
        return student;
    }

    /**
     * Parses a year value from a string.
     *
     * @param value string representation of the year
     * @return parsed integer year
     * @throws StudentException if the string is not a valid integer
     */
    private int parseYear(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new StudentException("Invalid year: " + value);
        }
    }

    /**
     * Calculates the total ECTS credits the student has earned from passed
     * elective disciplines in their specialty.
     *
     * @param student student whose credits to calculate
     * @return total elective credits earned
     */
    private int calculateElectiveCredits(Student student) {
        int total = 0;
        for (Grade g : student.getPassedGrades()) {
            Discipline d = g.getDiscipline();
            if (d.isElective()) {
                total += d.getCredits();
            }
        }
        return total;
    }
}