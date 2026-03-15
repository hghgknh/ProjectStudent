package studentsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String facultyNumber;
    private String name;
    private String program;
    private String group;
    private int year;
    private StudentStatus status;
    private List<Grade> grades;

    public Student(String facultyNumber, String name, String program, String group, int year) {
        this.facultyNumber = facultyNumber;
        this.name = name;
        this.program = program;
        this.group = group;
        this.year = year;
        this.status = StudentStatus.ENROLLED;
        this.grades = new ArrayList<>();
    }

    public String getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(String facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    public Grade findGrade(String disciplineName) {
        for (Grade g : grades) {
            if (g.getDisciplineName().equalsIgnoreCase(disciplineName)) return g;
        }
        return null;
    }

    public boolean isEnrolledIn(String disciplineName) {
        return findGrade(disciplineName) != null;
    }

    public double calculateAverage(boolean includeUngraded) {
        if (grades.isEmpty()) return 0.0;

        double sum = 0;
        int count = 0;
        for (Grade g : grades) {
            if (g.hasGrade()) {
                sum += g.getValue();
                count++;
            } else if (includeUngraded) {
                sum += Grade.FAIL_GRADE;
                count++;
            }
        }
        if (count == 0) return 0.0;
        return Math.round((sum / count) * 100.0) / 100.0;
    }

    public List<Grade> getPassedGrades() {
        List<Grade> passed = new ArrayList<>();
        for (Grade g : grades) {
            if (g.isPassed()) passed.add(g);
        }
        return passed;
    }

    public List<Grade> getUngradedGrades() {
        List<Grade> ungraded = new ArrayList<>();
        for (Grade g : grades) {
            if (!g.hasGrade()) ungraded.add(g);
        }
        return ungraded;
    }

    public boolean isActive() { return status == StudentStatus.ENROLLED; }

    @Override
    public String toString() {
        return facultyNumber + " - " + name + " [" + program + ", гр." + group + ", курс " + year + ", " + status + "]";
    }
}
