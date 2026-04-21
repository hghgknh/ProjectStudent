package studentsystem.service;

import studentsystem.exception.FileException;

public class FileManager {
    private SystemData systemData;
    private String currentFilePath;
    private boolean isOpen;
    private final XmlParser parser;

    public FileManager() {
        this.parser = new XmlParser();
        this.isOpen = false;
    }

    public void open(String filePath) {
        if (isOpen) {
            throw new FileException("Вече има отворен файл. Затворете го първо с close.");
        }

        java.io.File file = new java.io.File(filePath);
        if (!file.exists()) {
            systemData = new SystemData();
            currentFilePath = filePath;
            isOpen = true;
            System.out.println("Файлът не съществува. Създаден е нов файл: " + filePath);
            return;
        }

        systemData = parser.load(filePath);
        currentFilePath = filePath;
        isOpen = true;
        System.out.println("Successfully opened " + file.getName());
    }

    public void save() {
        requireOpenFile();
        parser.save(systemData, currentFilePath);
        System.out.println("Successfully saved " + new java.io.File(currentFilePath).getName());
    }

    public void saveAs(String filePath) {
        requireOpenFile();
        parser.save(systemData, filePath);
        currentFilePath = filePath;
        System.out.println("Successfully saved " + new java.io.File(filePath).getName());
    }

    public void close() {
        requireOpenFile();
        String name = new java.io.File(currentFilePath).getName();
        systemData = null;
        currentFilePath = null;
        isOpen = false;
        System.out.println("Successfully closed " + name);
    }

    public SystemData getSystemData() {
        requireOpenFile();
        return systemData;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    private void requireOpenFile() {
        if (!isOpen) {
            throw new FileException("Няма отворен файл. Използвайте open <файл>.");
        }
    }
}
