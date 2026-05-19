package studentsystem.exception;

/**
 * Thrown when a file operation fails — for example, when trying to open
 * a file that cannot be parsed, or performing any operation without a file open.
 */
public class FileException extends RuntimeException {
    /**
     * Constructs a new FileException with the given detail message.
     *
     * @param message explanation of the error
     */
    public FileException(String message) {
        super(message);
    }
}
