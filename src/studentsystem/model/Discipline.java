package studentsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Discipline {
    private String name;
    private DisciplineType type;
    private int credits;
    private List<Integer> availableInYears;

    public Discipline(String name, DisciplineType type, int credits) {
        this.name = name;
        this.type = type;
        this.credits = credits;
        this.availableInYears = new ArrayList<>();
    }

    public Discipline(String name, DisciplineType type) {

        this(name, type, 0);
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public DisciplineType getType() {

        return type;
    }

    public void setType(DisciplineType type) {

        this.type = type;
    }

    public int getCredits() {

        return credits;
    }

    public void setCredits(int credits) {

        this.credits = credits;
    }

    public List<Integer> getAvailableInYears() {

        return availableInYears;
    }

    public void setAvailableInYears(List<Integer> availableInYears) {

        this.availableInYears = availableInYears;
    }

    public void addAvailableYear(int year) {

        this.availableInYears.add(year);
    }

    public boolean isMandatory() {

        return type == DisciplineType.MANDATORY;
    }
    public boolean isElective()  {

        return type == DisciplineType.ELECTIVE;
    }

    @Override
    public String toString() {

        return name + " (" + (isMandatory() ? "задължителна" : "избираема") + ")";
    }
}
