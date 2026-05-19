package studentsystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student enrolled at the university.
 *
 * <p>Each student is identified by a unique faculty number and belongs to a
 * specialty, a study group, and a current year. The student accumulates
 * {@link Grade} records — one per discipline they enrol in — and their status
 * reflects whether they are actively studying, have interrupted, or have
 * graduated.</p>
 */
public class Student {

    /** Unique identifier assigned by the university. */
    private String facultyNumber;

    /** Full name of the student. */
    private String name;

    /** Name of the specialty (program) the student is enrolled in. */
    private String program;

    /** Study group identifier within the specialty. */
    private String group;

    /** Current year of study (1-based). */
    private int year;

    /** Current enrolment status of the student. */
    private StudentStatus status;

    /**
     * All disciplines the student has enrolled in, together with their grades.
     * A {@link Grade} with no value means the exam has not been sat yet.
     */
    private List<Grade> grades;

    /**
     * Constructs a new student. The initial status is {@link StudentStatus#ENROLLED}
     * and the grades list is empty.
     *
     * @param facultyNumber unique faculty number
     * @param name          full name
     * @param program       specialty name
     * @param group         study group identifier
     * @param year          current year of study
     */
    public Student(String facultyNumber, String name, String program, String group, int year) {
        this.facultyNumber = facultyNumber;
        this.name = name;
        this.program = program;
        this.group = group;
        this.year = year;
        this.status = StudentStatus.ENROLLED;
        this.grades = new ArrayList<>();
    }

    /**
     * Returns the student's unique faculty number.
     *
     * @return faculty number
     */
    public String getFacultyNumber() {
        return facultyNumber;
    }

    /**
     * Sets the student's faculty number.
     *
     * @param facultyNumber new faculty number
     */
    public void setFacultyNumber(String facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    /**
     * Returns the student's full name.
     *
     * @return full name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the student's full name.
     *
     * @param name new full name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the specialty the student is enrolled in.
     *
     * @return specialty name
     */
    public String getProgram() {
        return program;
    }

    /**
     * Sets the specialty the student is enrolled in.
     *
     * @param program new specialty name
     */
    public void setProgram(String program) {
        this.program = program;
    }

    /**
     * Returns the student's study group identifier.
     *
     * @return group identifier
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the student's study group identifier.
     *
     * @param group new group identifier
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Returns the student's current year of study.
     *
     * @return year (1-based)
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the student's current year of study.
     *
     * @param year new year (1-based)
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Returns the student's current enrolment status.
     *
     * @return {@link StudentStatus}
     */
    public StudentStatus getStatus() {
        return status;
    }

    /**
     * Sets the student's enrolment status.
     *
     * @param status new status
     */
    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    /**
     * Returns the full list of grades (enrolled disciplines) for this student.
     *
     * @return mutable list of grades
     */
    public List<Grade> getGrades() {
        return grades;
    }

    /**
     * Replaces the full list of grades for this student.
     *
     * @param grades new list of grades
     */
    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    /**
     * Adds a grade entry to this student's record.
     *
     * @param grade grade to add
     */
    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    /**
     * Finds and returns the grade for the given discipline name,
     * using a case-insensitive comparison.
     *
     * @param disciplineName name of the discipline to look up
     * @return the matching {@link Grade}, or {@code null} if not found
     */
    public Grade findGrade(String disciplineName) {
        for (Grade g : grades) {
            if (g.getDisciplineName().equalsIgnoreCase(disciplineName)) return g;
        }
        return null;
    }

    /**
     * Returns {@code true} if the student has an enrolment record for the
     * given discipline (regardless of whether a grade has been set).
     *
     * @param disciplineName name of the discipline
     * @return {@code true} if enrolled
     */
    public boolean isEnrolledIn(String disciplineName) {
        return findGrade(disciplineName) != null;
    }

    /**
     * Computes the student's GPA across all enrolled disciplines.
     *
     * <p>If {@code includeUngraded} is {@code true}, disciplines for which no
     * exam has been sat are counted as {@link Grade#FAIL_GRADE} (2.00).
     * Otherwise only disciplines with a recorded grade contribute to the
     * average.</p>
     *
     * @param includeUngraded {@code true} to count ungraded disciplines as 2.00
     * @return average grade rounded to two decimal places, or 0.0 if no grades
     */
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

    /**
     * Returns all grades for disciplines the student has passed
     * (grade &ge; {@link Grade#MIN_PASSING_GRADE}).
     *
     * @return list of passing grades; empty if none
     */
    public List<Grade> getPassedGrades() {
        List<Grade> passed = new ArrayList<>();
        for (Grade g : grades) {
            if (g.isPassed()) passed.add(g);
        }
        return passed;
    }

    /**
     * Returns all grades for disciplines the student is enrolled in but
     * has not yet sat the exam for (value == 0).
     *
     * @return list of ungraded entries; empty if none
     */
    public List<Grade> getUngradedGrades() {
        List<Grade> ungraded = new ArrayList<>();
        for (Grade g : grades) {
            if (!g.hasGrade()) ungraded.add(g);
        }
        return ungraded;
    }

    /**
     * Returns {@code true} if the student is currently active
     * (status is {@link StudentStatus#ENROLLED}).
     *
     * @return {@code true} when status is ENROLLED
     */
    public boolean isActive() {
        return status == StudentStatus.ENROLLED;
    }

    /**
     * Returns a concise summary of the student's key attributes.
     *
     * @return formatted string, e.g.
     *         {@code "123456 - John Smith [CS, gr.A1, year 2, ENROLLED]"}
     */
    @Override
    public String toString() {
        return facultyNumber + " - " + name + " [" + program + ", gr." + group + ", year " + year + ", " + status + "]";
    }
}
