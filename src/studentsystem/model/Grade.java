package studentsystem.model;

public class Grade {
    public static final double MIN_PASSING_GRADE = 3.0;
    public static final double FAIL_GRADE = 2.0;

    private String disciplineName;
    private double value;

    public Grade(String disciplineName, double value) {
        this.disciplineName = disciplineName;
        this.value = value;
    }

    public Grade(String disciplineName) {
        this(disciplineName, 0);
    }

    public String getDisciplineName() { return disciplineName; }
    public void setDisciplineName(String name) { this.disciplineName = name; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public boolean isPassed() {
        return value >= MIN_PASSING_GRADE;
    }

    public boolean hasGrade() {
        return value > 0;
    }

    @Override
    public String toString() {
        if (!hasGrade()) return disciplineName + ": (без оценка)";
        return disciplineName + ": " + value;
    }
}
