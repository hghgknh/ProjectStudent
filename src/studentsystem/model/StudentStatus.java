package studentsystem.model;

/**
 * Represents the current enrolment status of a {@link Student}.
 */
public enum StudentStatus {

    /** The student is actively enrolled and can sit exams and change their data. */
    ENROLLED,

    /**
     * The student has temporarily interrupted their studies.
     * An interrupted student cannot enrol in disciplines, sit exams,
     * or change their program, group, or year until {@code resume} is called.
     */
    INTERRUPTED,

    /** The student has successfully completed all requirements and graduated. */
    GRADUATED
}
