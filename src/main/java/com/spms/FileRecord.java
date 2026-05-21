package com.spms;

/**
 * FileRecord Class
 * Purpose: Tracks information about a locally stored file (PDF, notes, etc).
 * OOP Concept: Encapsulation, Interface Implementation (Storable).
 */
public class FileRecord implements Storable {
    private String fileName;
    private String filePath;
    private String subject;
    private String fileType;

    public FileRecord() {
        this.fileName = "";
        this.filePath = "";
        this.subject = "";
        this.fileType = "";
    }

    public FileRecord(String fileName, String filePath, String subject, String fileType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.subject = subject;
        this.fileType = fileType;
    }

    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public String getSubject() { return subject; }
    public String getFileType() { return fileType; }

    @Override
    public String toStorageString() {
        return fileName + "|" + filePath + "|" + subject + "|" + fileType;
    }

    @Override
    public void fromStorageString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 4) {
            this.fileName = parts[0];
            this.filePath = parts[1];
            this.subject = parts[2];
            this.fileType = parts[3];
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", subject, fileName, fileType);
    }
}
