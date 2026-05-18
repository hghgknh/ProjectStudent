package studentsystem.service;

import studentsystem.exception.StudentException;
import studentsystem.model.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudentService {
    private final FileManager fileManager;

    public StudentService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void enroll(String facultyNumber, String program, String group, String name) {
        SystemData db = fileManager.getSystemData();

        if (db.findStudent(facultyNumber) != null) {
            throw new StudentException("Student with faculty number " + facultyNumber + " already exists.");
        }

        Specialty specialty = db.findSpecialty(program);
        if (specialty == null) {
            throw new StudentException("Specialty '" + program + "' does not exist.");
        }

        Student student = new Student(facultyNumber, name, program, group, 1);
        db.addStudent(student);
        System.out.println("Student " + name + " successfully enrolled in specialty " + program + ", group " + group + ".");
    }

    public void advance(String facultyNumber) {
        Student student = getActiveStudent(facultyNumber);
        SystemData db = fileManager.getSystemData();
        Specialty specialty = db.findSpecialty(student.getProgram());

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
                student.setProgram(value);
                System.out.println("Specialty of student " + facultyNumber + " changed to " + value + ".");
                break;

            case "year":
                int newYear = parseYear(value);
                Specialty specialty = db.findSpecialty(student.getProgram());
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

    public void graduate(String facultyNumber) {
        Student student = getActiveStudent(facultyNumber);
        SystemData db = fileManager.getSystemData();
        Specialty specialty = db.findSpecialty(student.getProgram());

        for (Grade g : student.getGrades()) {
            if (!g.isPassed()) {
                throw new StudentException("Student has not passed all enrolled exams. Failed: " + g.getDisciplineName() + ".");
            }
        }

        if (specialty != null && specialty.getMinElectiveCredits() > 0) {
            int earned = calculateElectiveCredits(student, specialty);
            if (earned < specialty.getMinElectiveCredits()) {
                throw new StudentException("Insufficient elective credits. Required: "
                        + specialty.getMinElectiveCredits() + ", earned: " + earned + ".");
            }
        }

        student.setStatus(StudentStatus.GRADUATED);
        System.out.println("Student " + facultyNumber + " has been marked as graduated.");
    }

    public void interrupt(String facultyNumber) {
        Student student = getActiveStudent(facultyNumber);
        student.setStatus(StudentStatus.INTERRUPTED);
        System.out.println("Student " + facultyNumber + " has been marked as interrupted.");
    }

    public void resume(String facultyNumber) {
        Student student = getStudentByFn(facultyNumber);
        if (student.getStatus() != StudentStatus.INTERRUPTED) {
            throw new StudentException("Student " + facultyNumber + " has not interrupted.");
        }
        student.setStatus(StudentStatus.ENROLLED);
        System.out.println("Rights of student " + facultyNumber + " have been restored.");
    }

    public void enrollIn(String facultyNumber, String courseName) {
        Student student = getActiveStudent(facultyNumber);
        SystemData db = fileManager.getSystemData();
        Specialty specialty = db.findSpecialty(student.getProgram());

        if (specialty == null) {
            throw new StudentException("Student's specialty not found.");
        }

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

        student.addGrade(new Grade(courseName));
        System.out.println("Student " + facultyNumber + " has been enrolled in discipline '" + courseName + "'.");
    }

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

        if (!found) {
            System.out.println("No students found.");
        }
    }

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
                        .filter(s -> s.getProgram().equalsIgnoreCase(specialty.getName()))
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

    public void report(String facultyNumber) {
        Student s = getStudentByFn(facultyNumber);
        SystemData db = fileManager.getSystemData();
        Specialty specialty = db.findSpecialty(s.getProgram());

        System.out.println("Academic report for: " + s.getName() + " (" + s.getFacultyNumber() + ")");
        System.out.println("Specialty: " + s.getProgram() + " | Year: " + s.getYear() + " | Group: " + s.getGroup());
        System.out.println("==================================================");

        List<Grade> passed = s.getPassedGrades();
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

        if (specialty != null && specialty.getMinElectiveCredits() > 0) {
            int earned = calculateElectiveCredits(s, specialty);
            int remaining = Math.max(0, specialty.getMinElectiveCredits() - earned);
            System.out.println("--------------------------------------------------");
            System.out.println("Elective discipline credits:");
            System.out.println("  Earned   : " + earned);
            System.out.println("  Required : " + specialty.getMinElectiveCredits());
            System.out.println("  Remaining: " + remaining);
        }
    }

    private Student getActiveStudent(String facultyNumber) {
        Student student = getStudentByFn(facultyNumber);
        if (!student.isActive()) {
            throw new StudentException("Student " + facultyNumber + " has interrupted or graduated and cannot perform this operation.");
        }
        return student;
    }

    private Student getStudentByFn(String facultyNumber) {
        SystemData db = fileManager.getSystemData();
        Student student = db.findStudent(facultyNumber);
        if (student == null) {
            throw new StudentException("Student with faculty number " + facultyNumber + " not found.");
        }
        return student;
    }

    private int parseYear(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new StudentException("Invalid year: " + value);
        }
    }

    private int calculateElectiveCredits(Student student, Specialty specialty) {
        int total = 0;
        for (Grade g : student.getPassedGrades()) {
            Discipline d = specialty.findDiscipline(g.getDisciplineName());
            if (d != null && d.isElective()) {
                total += d.getCredits();
            }
        }
        return total;
    }
}
