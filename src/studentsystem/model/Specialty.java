package studentsystem.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a university specialty (degree program).
 *
 * <p>A specialty has a fixed number of years and organises its
 * {@link Discipline disciplines} by year of study. It also tracks the minimum
 * number of elective credits a student must accumulate before they can
 * graduate.</p>
 */
public class Specialty {

    /** Name of the specialty, e.g. "Computer Science". */
    private String name;

    /** Total number of study years in this specialty. */
    private int totalYears;

    /**
     * Minimum ECTS credits from elective disciplines required for graduation.
     * A value of 0 means no elective credit requirement is enforced.
     */
    private int minElectiveCredits;

    /**
     * Disciplines organised by year number (1-based key).
     * Each year maps to the list of disciplines taught in that year.
     */
    private Map<Integer, List<Discipline>> disciplinesByYear;

    /**
     * Constructs a specialty with a name, total years, and a minimum elective
     * credit requirement.
     *
     * @param name               name of the specialty
     * @param totalYears         total number of study years
     * @param minElectiveCredits minimum elective credits required to graduate
     */
    public Specialty(String name, int totalYears, int minElectiveCredits) {
        this.name = name;
        this.totalYears = totalYears;
        this.minElectiveCredits = minElectiveCredits;
        this.disciplinesByYear = new HashMap<>();
        for (int y = 1; y <= totalYears; y++) {
            disciplinesByYear.put(y, new ArrayList<>());
        }
    }

    /**
     * Constructs a specialty with a name and total years, defaulting the
     * minimum elective credit requirement to 0.
     *
     * @param name       name of the specialty
     * @param totalYears total number of study years
     */
    public Specialty(String name, int totalYears) {
        this(name, totalYears, 0);
    }

    /**
     * Returns the name of the specialty.
     *
     * @return specialty name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the specialty.
     *
     * @param name new specialty name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the total number of study years in this specialty.
     *
     * @return number of years
     */
    public int getTotalYears() {
        return totalYears;
    }

    /**
     * Sets the total number of study years in this specialty.
     *
     * @param totalYears new number of years
     */
    public void setTotalYears(int totalYears) {
        this.totalYears = totalYears;
    }

    /**
     * Returns the minimum number of elective ECTS credits required to graduate.
     *
     * @return minimum elective credits (0 if no requirement)
     */
    public int getMinElectiveCredits() {
        return minElectiveCredits;
    }

    /**
     * Sets the minimum number of elective ECTS credits required to graduate.
     *
     * @param minElectiveCredits new minimum credit count
     */
    public void setMinElectiveCredits(int minElectiveCredits) {
        this.minElectiveCredits = minElectiveCredits;
    }

    /**
     * Returns the full disciplines-by-year map.
     *
     * @return map from year number to list of disciplines
     */
    public Map<Integer, List<Discipline>> getDisciplinesByYear() {
        return disciplinesByYear;
    }

    /**
     * Returns the list of disciplines assigned to the given year.
     *
     * @param year study year (1-based)
     * @return list of disciplines for that year; empty list if year is not found
     */
    public List<Discipline> getDisciplinesForYear(int year) {
        return disciplinesByYear.getOrDefault(year, new ArrayList<>());
    }

    /**
     * Adds a discipline to the curriculum of the given study year.
     *
     * @param year       study year (1-based) to add the discipline to
     * @param discipline discipline to add
     */
    public void addDiscipline(int year, Discipline discipline) {
        disciplinesByYear.computeIfAbsent(year, k -> new ArrayList<>()).add(discipline);
    }

    /**
     * Returns {@code true} if a discipline with the given name exists in any
     * year of this specialty (case-insensitive).
     *
     * @param disciplineName name to look up
     * @return {@code true} if found
     */
    public boolean hasDiscipline(String disciplineName) {
        for (List<Discipline> list : disciplinesByYear.values()) {
            for (Discipline d : list) {
                if (d.getName().equalsIgnoreCase(disciplineName)) return true;
            }
        }
        return false;
    }

    /**
     * Finds and returns the discipline with the given name from any year
     * of this specialty (case-insensitive).
     *
     * @param disciplineName name to look up
     * @return the matching {@link Discipline}, or {@code null} if not found
     */
    public Discipline findDiscipline(String disciplineName) {
        for (List<Discipline> list : disciplinesByYear.values()) {
            for (Discipline d : list) {
                if (d.getName().equalsIgnoreCase(disciplineName)) return d;
            }
        }
        return null;
    }

    /**
     * Returns all mandatory disciplines assigned to the given study year.
     *
     * @param year study year (1-based)
     * @return list of mandatory disciplines; empty if none
     */
    public List<Discipline> getMandatoryForYear(int year) {
        List<Discipline> result = new ArrayList<>();
        for (Discipline d : getDisciplinesForYear(year)) {
            if (d.isMandatory()) result.add(d);
        }
        return result;
    }

    /**
     * Returns a concise human-readable description of this specialty.
     *
     * @return formatted string, e.g. {@code "Computer Science (4 years)"}
     */
    @Override
    public String toString() {
        return name + " (" + totalYears + " years)";
    }
}
