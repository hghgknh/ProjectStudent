package studentsystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a university discipline (course) that belongs to a {@link Specialty}.
 *
 * <p>Each discipline has a name, a type (mandatory or elective), a credit value,
 * and a list of years in which students are allowed to enrol in it. The
 * available-years list is only meaningful for elective disciplines that can be
 * taken across multiple years; for mandatory disciplines the year is determined
 * by the specialty's curriculum structure.</p>
 */
public class Discipline {

    /** Display name of the discipline. */
    private String name;

    /** Whether the discipline is mandatory or elective. */
    private DisciplineType type;

    /** Number of ECTS credits awarded upon passing this discipline. */
    private int credits;

    /**
     * Years in which this discipline can be enrolled in.
     * Primarily used for elective disciplines that span more than one year.
     */
    private List<Integer> availableInYears;

    /**
     * Constructs a discipline with a name, type and credit value.
     *
     * @param name    display name of the discipline
     * @param type    {@link DisciplineType#MANDATORY} or {@link DisciplineType#ELECTIVE}
     * @param credits number of ECTS credits
     */
    public Discipline(String name, DisciplineType type, int credits) {
        this.name = name;
        this.type = type;
        this.credits = credits;
        this.availableInYears = new ArrayList<>();
    }

    /**
     * Constructs a discipline with a name and type, defaulting credits to 0.
     *
     * @param name display name of the discipline
     * @param type {@link DisciplineType#MANDATORY} or {@link DisciplineType#ELECTIVE}
     */
    public Discipline(String name, DisciplineType type) {
        this(name, type, 0);
    }

    /**
     * Returns the display name of the discipline.
     *
     * @return discipline name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display name of the discipline.
     *
     * @param name new discipline name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of the discipline.
     *
     * @return {@link DisciplineType#MANDATORY} or {@link DisciplineType#ELECTIVE}
     */
    public DisciplineType getType() {
        return type;
    }

    /**
     * Sets the type of the discipline.
     *
     * @param type new discipline type
     */
    public void setType(DisciplineType type) {
        this.type = type;
    }

    /**
     * Returns the number of ECTS credits awarded for this discipline.
     *
     * @return credit count
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the number of ECTS credits awarded for this discipline.
     *
     * @param credits new credit count
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Returns the list of years in which this discipline can be enrolled in.
     *
     * @return mutable list of year numbers; empty if no restriction is defined
     */
    public List<Integer> getAvailableInYears() {
        return availableInYears;
    }

    /**
     * Replaces the list of years in which this discipline can be enrolled in.
     *
     * @param availableInYears new list of year numbers
     */
    public void setAvailableInYears(List<Integer> availableInYears) {
        this.availableInYears = availableInYears;
    }

    /**
     * Adds a year to the list of years in which this discipline can be enrolled in.
     *
     * @param year year number to add
     */
    public void addAvailableYear(int year) {
        this.availableInYears.add(year);
    }

    /**
     * Returns {@code true} if this discipline is mandatory.
     *
     * @return {@code true} when type is {@link DisciplineType#MANDATORY}
     */
    public boolean isMandatory() {
        return type == DisciplineType.MANDATORY;
    }

    /**
     * Returns {@code true} if this discipline is elective.
     *
     * @return {@code true} when type is {@link DisciplineType#ELECTIVE}
     */
    public boolean isElective() {
        return type == DisciplineType.ELECTIVE;
    }

    /**
     * Returns a human-readable representation of this discipline,
     * including its name and whether it is mandatory or elective.
     *
     * @return formatted string, e.g. {@code "Mathematics (mandatory)"}
     */
    @Override
    public String toString() {
        return name + " (" + (isMandatory() ? "mandatory" : "elective") + ")";
    }
}
