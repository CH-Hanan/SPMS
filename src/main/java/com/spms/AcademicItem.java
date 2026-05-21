package com.spms;

/**
 * Abstract Class AcademicItem
 * OOP Concept: Abstract Class, Inheritance, Encapsulation
 * Represents a generic academic item (like a Task or a Lecture).
 */
public abstract class AcademicItem implements Storable {
    protected String title;
    protected String subject;

    public AcademicItem() {
        this.title = "";
        this.subject = "General";
    }

    public AcademicItem(String title, String subject) {
        this.title = title;
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
