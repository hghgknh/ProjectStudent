package studentsystem.model;

/**
 * Classifies a {@link Discipline} as either required for all students
 * or optional.
 */
public enum DisciplineType {

    /** The discipline is compulsory for every student in the specialty. */
    MANDATORY,

    /** The discipline is optional; students choose whether to enrol. */
    ELECTIVE
}
