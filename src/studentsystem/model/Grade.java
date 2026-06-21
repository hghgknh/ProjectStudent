package studentsystem.model;

/**
 * Represents a student's result in a single discipline.
 *
 * <p>A {@code Grade} is created with no value (0) when the student enrols in a
 * discipline, and updated later via {@link #setValue(double)} when the exam is
 * sat. A grade with value 0 is treated as "not yet graded".</p>
 *
 * <p>The grading scale used by this system runs from 2.00 (fail) to 6.00 (excellent).
 * The minimum passing value is defined by {@link #MIN_PASSING_GRADE}.</p>
 */
public class Grade {

    /** The lowest value that counts as a passing result. */
    public static final double MIN_PASSING_GRADE = 3.0;

    /** The grade value automatically assigned to ungraded disciplines when computing GPA. */
    public static final double FAIL_GRADE = 2.0;

    /** The discipline this grade belongs to. */
    private Discipline discipline;

    /**
     * Numeric grade value in the range [2.00, 6.00].
     * A value of 0 means the student has not yet sat the exam.
     */
    private double value;

    /**
     * Constructs a grade with a known value.
     *
     * @param discipline the discipline this grade belongs to
     * @param value      numeric grade (2.00–6.00), or 0 if not yet graded
     */
    public Grade(Discipline discipline, double value) {
        this.discipline = discipline;
        this.value = value;
    }

    /**
     * Constructs a grade with no value (student enrolled but not yet graded).
     *
     * @param discipline the discipline this grade belongs to
     */
    public Grade(Discipline discipline) {
        this(discipline, 0);
    }

    /**
     * Returns the discipline this grade belongs to.
     *
     * @return the {@link Discipline} object
     */
    public Discipline getDiscipline() {
        return discipline;
    }

    /**
     * Sets the discipline this grade belongs to.
     *
     * @param discipline new discipline
     */
    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    /**
     * Returns the name of the discipline this grade belongs to.
     *
     * @return discipline name
     */
    public String getDisciplineName() {
        return discipline.getName();
    }

    /**
     * Returns the numeric grade value, or 0 if the exam has not been sat yet.
     *
     * @return grade value
     */
    public double getValue() { return value; }

    /**
     * Sets the numeric grade value.
     *
     * @param value grade in the range [2.00, 6.00]
     */
    public void setValue(double value) { this.value = value; }

    /**
     * Returns {@code true} if the student has passed this exam.
     * A grade is considered passing when its value is at least {@link #MIN_PASSING_GRADE}.
     *
     * @return {@code true} if the grade is a passing grade
     */
    public boolean isPassed() {
        return value >= MIN_PASSING_GRADE;
    }

    /**
     * Returns {@code true} if an actual grade value has been recorded
     * (i.e. the student has sat the exam).
     *
     * @return {@code true} if {@code value > 0}
     */
    public boolean hasGrade() {
        return value > 0;
    }

    /**
     * Returns a human-readable representation of this grade.
     * Shows {@code "(no grade)"} when the exam has not been sat yet.
     *
     * @return formatted string, e.g. {@code "Mathematics: 5.5"} or
     *         {@code "Mathematics: (no grade)"}
     */
    @Override
    public String toString() {
        if (!hasGrade()) return discipline.getName() + ": (no grade)";
        return discipline.getName() + ": " + value;
    }
}