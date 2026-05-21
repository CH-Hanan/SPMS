package com.spms;

/**
 * Lecture Class
 * Purpose: Represents a scheduled class or lecture.
 * OOP Concept: Inheritance, Interfaces
 */
public class Lecture extends AcademicItem implements Schedulable, Trackable, Storable {
    private String dayOfWeek;
    private String startTime;
    private String room;
    private boolean isCompleted;

    public Lecture() {
        super();
        this.isCompleted = false;
    }

    public Lecture(String title, String subject, String dayOfWeek, String startTime, String room) {
        super(title, subject);
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.room = room;
        this.isCompleted = false;
    }

    @Override
    public String getScheduleTime() {
        return dayOfWeek + " at " + startTime;
    }

    @Override
    public void markCompleted() {
        this.isCompleted = true;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    public String getRoom() { return room; }

    @Override
    public String toStorageString() {
        return title + "|" + subject + "|" + dayOfWeek + "|" + startTime + "|" + room + "|" + isCompleted;
    }

    @Override
    public void fromStorageString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 6) {
            this.title = parts[0];
            this.subject = parts[1];
            this.dayOfWeek = parts[2];
            this.startTime = parts[3];
            this.room = parts[4];
            this.isCompleted = Boolean.parseBoolean(parts[5]);
        }
    }

    @Override
    public String toString() {
        String status = isCompleted ? "[Attended]" : "[Upcoming]";
        return String.format("%s [%s] %s - %s (Room: %s)", status, subject, title, getScheduleTime(), room);
    }
}
