package studentsystem.service;

import studentsystem.exception.StudentException;
import studentsystem.model.*;
import java.util.List;

public class StudentService {
    private final FileManager fileManager;

    public StudentService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void enroll(String facultyNumber, String program, String group, String name) {
        SystemData db = fileManager.getSystemData();

        if (db.findStudent(facultyNumber) != null) {
            throw new StudentException("Студент с факултетен номер " + facultyNumber + " вече съществува.");
        }

        Specialty specialty = db.findSpecialty(program);
        if (specialty == null) {
            throw new StudentException("Специалност '" + program + "' не съществува.");
        }

        Student student = new Student(facultyNumber, name, program, group, 1);
        db.addStudent(student);
        System.out.println("Студент " + name + " е записан успешно в специалност " + program + ", група " + group + ".");
    }

    public void advance(String facultyNumber) {
        Student student = getActiveStudent(facultyNumber);
        SystemData db = fileManager.getSystemData();
        Specialty specialty = db.findSpecialty(student.getProgram());

        if (student.getYear() >= specialty.getTotalYears()) {
            throw new StudentException("Студентът е вече в последен курс.");
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
            throw new StudentException("Студентът не е положил успешно задължителните изпити. Неположени: " + failCount + " (позволени максимум 2).");
        }

        student.setYear(student.getYear() + 1);
        System.out.println("Студент " + facultyNumber + " е преминал в курс " + student.getYear() + ".");
    }

    public void change(String facultyNumber, String option, String value) {
        Student student = getActiveStudent(facultyNumber);
        SystemData db = fileManager.getSystemData();

        switch (option.toLowerCase()) {
            case "group":
                student.setGroup(value);
                System.out.println("Групата на студент " + facultyNumber + " е променена на " + value + ".");
                break;

            case "program":
                Specialty newSpecialty = db.findSpecialty(value);
                if (newSpecialty == null) {
                    throw new StudentException("Специалност '" + value + "' не съществува.");
                }
                for (int y = 1; y < student.getYear(); y++) {
                    List<Discipline> mandatory = newSpecialty.getMandatoryForYear(y);
                    for (Discipline d : mandatory) {
                        Grade g = student.findGrade(d.getName());
                        if (g == null || !g.isPassed()) {
                            throw new StudentException("Студентът не е положил задължителен изпит '" + d.getName() + "' от новата специалност.");
                        }
                    }
                }
                student.setProgram(value);
                System.out.println("Специалността на студент " + facultyNumber + " е променена на " + value + ".");
                break;

            case "year":
                int newYear = parseYear(value);
                Specialty specialty = db.findSpecialty(student.getProgram());
                if (newYear > student.getYear() + 1 || newYear < student.getYear()) {
                    throw new StudentException("Може да се премине само в следващ курс.");
                }
                if (newYear == student.getYear() + 1) {
                    List<Discipline> mandatory = specialty.getMandatoryForYear(student.getYear());
                    int failCount = 0;
                    for (Discipline d : mandatory) {
                        Grade g = student.findGrade(d.getName());
                        if (g == null || !g.isPassed()) failCount++;
                    }
                    if (failCount > 2) {
                        throw new StudentException("Не може да се премине в следващ курс. Неположени задължителни изпити: " + failCount + ".");
                    }
                }
                student.setYear(newYear);
                System.out.println("Курсът на студент " + facultyNumber + " е променен на " + newYear + ".");
                break;

            default:
                throw new StudentException("Невалидна опция '" + option + "'. Използвайте: program, group, year.");
        }
    }

    public void graduate(String facultyNumber) {
        Student student = getActiveStudent(facultyNumber);
        SystemData db = fileManager.getSystemData();
        Specialty specialty = db.findSpecialty(student.getProgram());

        for (Grade g : student.getGrades()) {
            if (!g.isPassed()) {
                throw new StudentException("Студентът не е положил всички записани изпити. Неположен: " + g.getDisciplineName() + ".");
            }
        }

        if (specialty != null && specialty.getMinElectiveCredits() > 0) {
            int earned = calculateElectiveCredits(student, specialty);
            if (earned < specialty.getMinElectiveCredits()) {
                throw new StudentException("Недостатъчно кредити от избираеми дисциплини. Необходими: "
                        + specialty.getMinElectiveCredits() + ", събрани: " + earned + ".");
            }
        }

        student.setStatus(StudentStatus.GRADUATED);
        System.out.println("Студент " + facultyNumber + " е отбелязан като завършил.");
    }

    public void interrupt(String facultyNumber) {
        Student student = getActiveStudent(facultyNumber);
        student.setStatus(StudentStatus.INTERRUPTED);
        System.out.println("Студент " + facultyNumber + " е маркиран като прекъснал.");
    }

    public void resume(String facultyNumber) {
        Student student = getStudentByFn(facultyNumber);
        if (student.getStatus() != StudentStatus.INTERRUPTED) {
            throw new StudentException("Студент " + facultyNumber + " не е прекъснал.");
        }
        student.setStatus(StudentStatus.ENROLLED);
        System.out.println("Правата на студент " + facultyNumber + " са възстановени.");
    }

    public void enrollIn(String facultyNumber, String courseName) {
        Student student = getActiveStudent(facultyNumber);
        SystemData db = fileManager.getSystemData();
        Specialty specialty = db.findSpecialty(student.getProgram());

        if (specialty == null) {
            throw new StudentException("Специалността на студента не е намерена.");
        }

        Discipline discipline = specialty.findDiscipline(courseName);
        if (discipline == null) {
            throw new StudentException("Дисциплина '" + courseName + "' не съществува в специалност " + student.getProgram() + ".");
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
            throw new StudentException("Дисциплина '" + courseName + "' не може да се записва в курс " + student.getYear() + ".");
        }

        if (student.isEnrolledIn(courseName)) {
            throw new StudentException("Студентът вече е записан в дисциплина '" + courseName + "'.");
        }

        student.addGrade(new Grade(courseName));
        System.out.println("Студент " + facultyNumber + " е записан в дисциплина '" + courseName + "'.");
    }

    public void addGrade(String facultyNumber, String courseName, double gradeValue) {
        Student student = getActiveStudent(facultyNumber);

        if (gradeValue < 2.0 || gradeValue > 6.0) {
            throw new StudentException("Невалидна оценка " + gradeValue + ". Оценката трябва да е между 2.00 и 6.00.");
        }

        Grade grade = student.findGrade(courseName);
        if (grade == null) {
            throw new StudentException("Студентът не е записан в дисциплина '" + courseName + "'.");
        }

        grade.setValue(gradeValue);
        System.out.println("Добавена оценка " + gradeValue + " по '" + courseName + "' на студент " + facultyNumber + ".");
    }

    public void print(String facultyNumber) {
        Student s = getStudentByFn(facultyNumber);
        System.out.println("Факултетен номер : " + s.getFacultyNumber());
        System.out.println("Име              : " + s.getName());
        System.out.println("Специалност      : " + s.getProgram());
        System.out.println("Група            : " + s.getGroup());
        System.out.println("Курс             : " + s.getYear());
        System.out.println("Статус           : " + s.getStatus());
        System.out.println("Среден успех     : " + s.calculateAverage(true));
    }

    public void printAll(String program, int year) {
        SystemData db = fileManager.getSystemData();
        Specialty specialty = db.findSpecialty(program);
        if (specialty == null) {
            throw new StudentException("Специалност '" + program + "' не съществува.");
        }

        System.out.println("Студенти в специалност " + program + ", курс " + year + ":");
        System.out.println("--------------------------------------------------");

        boolean found = false;
        for (Student s : db.getStudents()) {
            if (s.getProgram().equalsIgnoreCase(program) && s.getYear() == year) {
                System.out.printf("%-10s %-25s гр.%-5s %s%n",
                        s.getFacultyNumber(), s.getName(), s.getGroup(), s.getStatus());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Няма студенти.");
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

                System.out.println("Протокол: " + courseName + " | " + specialty.getName() + " | Курс " + year);
                System.out.println("--------------------------------------------------");

                boolean found = false;
                for (Student s : db.getStudents()) {
                    if (!s.getProgram().equalsIgnoreCase(specialty.getName())) continue;
                    Grade g = s.findGrade(courseName);
                    if (g == null) continue;

                    String gradeStr = g.hasGrade() ? String.valueOf(g.getValue()) : "---";
                    System.out.printf("%-10s %-25s %s%n", s.getFacultyNumber(), s.getName(), gradeStr);
                    found = true;
                }

                if (!found) System.out.println("Няма записани студенти.");
                System.out.println();
            }
        }
    }

    public void report(String facultyNumber) {
        Student s = getStudentByFn(facultyNumber);
        SystemData db = fileManager.getSystemData();
        Specialty specialty = db.findSpecialty(s.getProgram());

        System.out.println("Академична справка за: " + s.getName() + " (" + s.getFacultyNumber() + ")");
        System.out.println("Специалност: " + s.getProgram() + " | Курс: " + s.getYear() + " | Група: " + s.getGroup());
        System.out.println("==================================================");

        List<Grade> passed = s.getPassedGrades();
        List<Grade> ungraded = s.getUngradedGrades();

        if (!passed.isEmpty()) {
            System.out.println("Положени изпити:");
            for (Grade g : passed) {
                System.out.printf("  %-35s %.2f%n", g.getDisciplineName(), g.getValue());
            }
        }

        if (!ungraded.isEmpty()) {
            System.out.println("Незавършени дисциплини (смятат се като 2.00):");
            for (Grade g : ungraded) {
                System.out.printf("  %-35s ---%n", g.getDisciplineName());
            }
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("Среден успех (с незавършени): %.2f%n", s.calculateAverage(true));
        System.out.printf("Среден успех (само положени): %.2f%n", s.calculateAverage(false));

        if (specialty != null && specialty.getMinElectiveCredits() > 0) {
            int earned = calculateElectiveCredits(s, specialty);
            int remaining = Math.max(0, specialty.getMinElectiveCredits() - earned);
            System.out.println("--------------------------------------------------");
            System.out.println("Кредити от избираеми дисциплини:");
            System.out.println("  Събрани  : " + earned);
            System.out.println("  Необходими: " + specialty.getMinElectiveCredits());
            System.out.println("  Оставащи : " + remaining);
        }
    }

    private Student getActiveStudent(String facultyNumber) {
        Student student = getStudentByFn(facultyNumber);
        if (!student.isActive()) {
            throw new StudentException("Студент " + facultyNumber + " е прекъснал или завършил и не може да извършва тази операция.");
        }
        return student;
    }

    private Student getStudentByFn(String facultyNumber) {
        SystemData db = fileManager.getSystemData();
        Student student = db.findStudent(facultyNumber);
        if (student == null) {
            throw new StudentException("Студент с факултетен номер " + facultyNumber + " не е намерен.");
        }
        return student;
    }

    private int parseYear(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new StudentException("Невалиден курс: " + value);
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
