package studentsystem.exception;

/**
 * Thrown when a student-related business rule is violated — for example,
 * enrolling in a discipline not available for the student's year, or
 * advancing a student who has too many failed mandatory exams.
 */
public class StudentException extends RuntimeException {
    /**
     * Constructs a new StudentException with the given detail message.
     *
     * @param message explanation of the rule violation
     */
    public StudentException(String message) {
        super(message);
    }
}
