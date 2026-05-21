package com.spms;

/**
 * StudySession Class
 * Purpose: Records a completed study session with duration.
 * OOP Concept: Encapsulation, Interface Implementation (Storable).
 */
public class StudySession implements Storable {
    private String subject;
    private int durationMinutes;
    private String date;

    public StudySession() {
        this.subject = "";
        this.durationMinutes = 0;
        this.date = "";
    }

    public StudySession(String subject, int durationMinutes, String date) {
        this.subject = subject;
        this.durationMinutes = durationMinutes;
        this.date = date;
    }

    public String getSubject() { return subject; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getDate() { return date; }

    @Override
    public String toStorageString() {
        return subject + "|" + durationMinutes + "|" + date;
    }

    @Override
    public void fromStorageString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 3) {
            this.subject = parts[0];
            this.durationMinutes = Integer.parseInt(parts[1]);
            this.date = parts[2];
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %d minutes on %s", subject, durationMinutes, date);
    }
}
