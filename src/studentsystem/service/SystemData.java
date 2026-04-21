package studentsystem.service;

import studentsystem.model.Specialty;
import studentsystem.model.Student;

import java.util.ArrayList;
import java.util.List;

public class SystemData {
    private List<Student> students;
    private List<Specialty> specialties;

    public SystemData() {
        this.students = new ArrayList<>();
        this.specialties = new ArrayList<>();
    }

    public List<Student> getStudents() { return students; }
    public List<Specialty> getSpecialties() { return specialties; }

    public void addStudent(Student s) { students.add(s); }
    public void addSpecialty(Specialty s) { specialties.add(s); }

    public Student findStudent(String facultyNumber) {
        for (Student s : students) {
            if (s.getFacultyNumber().equals(facultyNumber)) return s;
        }
        return null;
    }

    public Specialty findSpecialty(String name) {
        for (Specialty s : specialties) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }
}
