package com.spms;

/**
 * Task Class
 * Purpose: Represents a single study task with due date and priority.
 * OOP Concept: Inheritance (extends AcademicItem), Interfaces (implements Trackable).
 */
public class Task extends AcademicItem implements Trackable {
    private boolean isCompleted;
    private String dueDate;
    private String priority; // High, Medium, Low
    
    // Empty constructor for serialization
    public Task() {
        super();
        this.isCompleted = false;
        this.dueDate = "";
        this.priority = "Medium";
    }

    public Task(String title, String subject, String dueDate, String priority) { 
        super(title, subject);
        this.isCompleted = false; 
        this.dueDate = dueDate;
        this.priority = priority;
    }

    @Override
    public boolean isCompleted() { 
        return isCompleted; 
    }

    @Override
    public void markCompleted() { 
        this.isCompleted = true; 
    }

    public String getDueDate() { return dueDate; }
    public String getPriority() { return priority; }

    @Override
    public String toStorageString() {
        // Simple delimiter-based persistence (Pipe separated)
        return title + "|" + subject + "|" + dueDate + "|" + priority + "|" + isCompleted;
    }

    @Override
    public void fromStorageString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 5) {
            this.title = parts[0];
            this.subject = parts[1];
            this.dueDate = parts[2];
            this.priority = parts[3];
            this.isCompleted = Boolean.parseBoolean(parts[4]);
        }
    }

    // Overriding the toString method for easy display in ListView (Polymorphism)
    @Override
    public String toString() { 
        String status = isCompleted ? "[Done]" : "[Pending]";
        return String.format("%s [%s] %s (Due: %s) - %s Priority", status, subject, title, dueDate, priority);
    }
}
