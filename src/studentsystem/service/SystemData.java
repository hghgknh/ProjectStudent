package studentsystem.service;

import studentsystem.model.Specialty;
import studentsystem.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory data store holding all students and specialties loaded from the
 * currently open file.
 *
 * <p>An instance of this class is created when a file is opened and discarded
 * when the file is closed. All changes made through the application live here
 * until the user explicitly saves them back to disk.</p>
 */
public class SystemData {

    /** All students currently loaded in memory. */
    private List<Student> students;

    /** All specialties (degree programs) currently loaded in memory. */
    private List<Specialty> specialties;

    /**
     * Constructs an empty data store with no students or specialties.
     */
    public SystemData() {
        this.students = new ArrayList<>();
        this.specialties = new ArrayList<>();
    }

    /**
     * Returns the list of all students in the system.
     *
     * @return mutable list of students
     */
    public List<Student> getStudents() { return students; }

    /**
     * Returns the list of all specialties in the system.
     *
     * @return mutable list of specialties
     */
    public List<Specialty> getSpecialties() { return specialties; }

    /**
     * Adds a student to the data store.
     *
     * @param s student to add
     */
    public void addStudent(Student s) { students.add(s); }

    /**
     * Adds a specialty to the data store.
     *
     * @param s specialty to add
     */
    public void addSpecialty(Specialty s) { specialties.add(s); }

    /**
     * Finds a student by their exact faculty number.
     *
     * @param facultyNumber faculty number to search for
     * @return the matching {@link Student}, or {@code null} if not found
     */
    public Student findStudent(String facultyNumber) {
        for (Student s : students) {
            if (s.getFacultyNumber().equals(facultyNumber)) return s;
        }
        return null;
    }

    /**
     * Finds a specialty by name (case-insensitive).
     *
     * @param name specialty name to search for
     * @return the matching {@link Specialty}, or {@code null} if not found
     */
    public Specialty findSpecialty(String name) {
        for (Specialty s : specialties) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }
}
