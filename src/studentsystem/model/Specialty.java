package studentsystem.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Specialty {
    private String name;
    private int totalYears;
    private int minElectiveCredits;
    private Map<Integer, List<Discipline>> disciplinesByYear;

    public Specialty(String name, int totalYears, int minElectiveCredits) {
        this.name = name;
        this.totalYears = totalYears;
        this.minElectiveCredits = minElectiveCredits;
        this.disciplinesByYear = new HashMap<>();
        for (int y = 1; y <= totalYears; y++) {
            disciplinesByYear.put(y, new ArrayList<>());
        }
    }

    public Specialty(String name, int totalYears) {
        this(name, totalYears, 0);
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getTotalYears() {

        return totalYears;
    }

    public void setTotalYears(int totalYears) {

        this.totalYears = totalYears;
    }

    public int getMinElectiveCredits() {

        return minElectiveCredits;
    }

    public void setMinElectiveCredits(int minElectiveCredits) {

        this.minElectiveCredits = minElectiveCredits;
    }

    public Map<Integer, List<Discipline>> getDisciplinesByYear() {

        return disciplinesByYear;
    }

    public List<Discipline> getDisciplinesForYear(int year) {
        return disciplinesByYear.getOrDefault(year, new ArrayList<>());
    }

    public void addDiscipline(int year, Discipline discipline) {
        disciplinesByYear.computeIfAbsent(year, k -> new ArrayList<>()).add(discipline);
    }

    public boolean hasDiscipline(String disciplineName) {
        for (List<Discipline> list : disciplinesByYear.values()) {
            for (Discipline d : list) {
                if (d.getName().equalsIgnoreCase(disciplineName)) return true;
            }
        }
        return false;
    }

    public Discipline findDiscipline(String disciplineName) {
        for (List<Discipline> list : disciplinesByYear.values()) {
            for (Discipline d : list) {
                if (d.getName().equalsIgnoreCase(disciplineName)) return d;
            }
        }
        return null;
    }

    public List<Discipline> getMandatoryForYear(int year) {
        List<Discipline> result = new ArrayList<>();
        for (Discipline d : getDisciplinesForYear(year)) {
            if (d.isMandatory()) result.add(d);
        }
        return result;
    }

    @Override
    public String toString() {
        return name + " (" + totalYears + " курса)";
    }
}
