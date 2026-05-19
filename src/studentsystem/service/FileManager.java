package studentsystem.service;

import studentsystem.exception.FileException;

/**
 * Manages the lifecycle of the single XML data file that the application
 * works with at any given time.
 *
 * <p>Only one file may be open at a time. All student-related commands
 * require an open file; attempting them without one causes a
 * {@link FileException} to be thrown.</p>
 *
 * <p>When a file is opened its contents are loaded into a {@link SystemData}
 * instance held in memory. Changes are not written back to disk until
 * {@link #save()} or {@link #saveAs(String)} is explicitly called.</p>
 */
public class FileManager {

    /** In-memory representation of the currently open file's data. */
    private SystemData systemData;

    /** Absolute or relative path of the currently open file. */
    private String currentFilePath;

    /** Whether a file is currently open. */
    private boolean isOpen;

    /** Parser used to read from and write to XML files. */
    private final XmlParser parser;

    /**
     * Constructs a FileManager with no file open.
     */
    public FileManager() {
        this.parser = new XmlParser();
        this.isOpen = false;
    }

    /**
     * Opens the file at the given path and loads its contents into memory.
     * If the file does not exist, a new empty data store is created and the
     * file will be created on the next {@link #save()} call.
     *
     * @param filePath path to the XML file to open
     * @throws FileException if another file is already open
     * @throws FileException if the file exists but cannot be read or parsed
     */
    public void open(String filePath) {
        if (isOpen) {
            throw new FileException("A file is already open. Close it first with close.");
        }

        java.io.File file = new java.io.File(filePath);
        if (!file.exists()) {
            systemData = new SystemData();
            currentFilePath = filePath;
            isOpen = true;
            System.out.println("File does not exist. Created new file: " + filePath);
            return;
        }

        systemData = parser.load(filePath);
        currentFilePath = filePath;
        isOpen = true;
        System.out.println("Successfully opened " + file.getName());
    }

    /**
     * Saves the current in-memory data back to the file it was loaded from.
     *
     * @throws FileException if no file is currently open
     * @throws FileException if the file cannot be written
     */
    public void save() {
        requireOpenFile();
        parser.save(systemData, currentFilePath);
        System.out.println("Successfully saved " + new java.io.File(currentFilePath).getName());
    }

    /**
     * Saves the current in-memory data to a different file path.
     * Subsequent {@link #save()} calls will write to the new path.
     *
     * @param filePath destination file path
     * @throws FileException if no file is currently open
     * @throws FileException if the destination file cannot be written
     */
    public void saveAs(String filePath) {
        requireOpenFile();
        parser.save(systemData, filePath);
        currentFilePath = filePath;
        System.out.println("Successfully saved " + new java.io.File(filePath).getName());
    }

    /**
     * Closes the currently open file and clears all in-memory data.
     * Any unsaved changes are discarded.
     *
     * @throws FileException if no file is currently open
     */
    public void close() {
        requireOpenFile();
        String name = new java.io.File(currentFilePath).getName();
        systemData = null;
        currentFilePath = null;
        isOpen = false;
        System.out.println("Successfully closed " + name);
    }

    /**
     * Returns the in-memory data store of the currently open file.
     *
     * @return the {@link SystemData} for the open file
     * @throws FileException if no file is currently open
     */
    public SystemData getSystemData() {
        requireOpenFile();
        return systemData;
    }

    /**
     * Returns whether a file is currently open.
     *
     * @return {@code true} if a file is open
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Returns the path of the currently open file, or {@code null} if no
     * file is open.
     *
     * @return file path, or {@code null}
     */
    public String getCurrentFilePath() {
        return currentFilePath;
    }

    /**
     * Throws a {@link FileException} if no file is currently open.
     * Used as a guard at the start of every operation that requires an open file.
     *
     * @throws FileException if no file is open
     */
    private void requireOpenFile() {
        if (!isOpen) {
            throw new FileException("No file is open. Use open <file>.");
        }
    }
}
