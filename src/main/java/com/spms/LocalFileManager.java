package com.spms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * LocalFileManager Class
 * Purpose: Manages actual physical files (PDFs, images) by copying them into subject folders.
 * OOP Concept: Encapsulation.
 */
public class LocalFileManager {
    private static final String BASE_DIR = "StudentData/";

    public LocalFileManager() {
        File baseDir = new File(BASE_DIR);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    /**
     * Copies a file from the user's computer into the structured StudentData directory.
     * @param sourceFile The file selected by the user.
     * @param subject The subject category (e.g., "OOP").
     * @return FileRecord representing the saved file, or null if failed.
     */
    public FileRecord importFile(File sourceFile, String subject) {
        if (sourceFile == null || !sourceFile.exists()) {
            return null;
        }

        // Create the subject directory if it doesn't exist
        File subjectDir = new File(BASE_DIR + subject);
        if (!subjectDir.exists()) {
            subjectDir.mkdirs();
        }

        // Prevent exact duplicates by appending timestamp if needed, or just overwriting for simplicity.
        // For a beginner project, simple overwrite or keep unique name is good.
        File destFile = new File(subjectDir, sourceFile.getName());
        int counter = 1;
        while (destFile.exists()) {
            String nameWithoutExt = sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf('.'));
            String ext = sourceFile.getName().substring(sourceFile.getName().lastIndexOf('.'));
            destFile = new File(subjectDir, nameWithoutExt + "_" + counter + ext);
            counter++;
        }

        try {
            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            String extension = sourceFile.getName().substring(sourceFile.getName().lastIndexOf('.') + 1).toUpperCase();
            return new FileRecord(destFile.getName(), destFile.getPath(), subject, extension);
        } catch (IOException e) {
            System.err.println("Failed to copy file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Helper to get a file path for opening it later.
     */
    public void openFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                // Uses the operating system's default application to open the file
                java.awt.Desktop.getDesktop().open(file);
            } catch (IOException e) {
                System.err.println("Could not open file: " + e.getMessage());
            }
        } else {
            System.err.println("File not found: " + filePath);
        }
    }
}
