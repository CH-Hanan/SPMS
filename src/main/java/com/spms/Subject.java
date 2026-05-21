package com.spms;

/**
 * Subject Abstract Class
 * Purpose: Base class for academic subjects, used in GPA calculation.
 * OOP Concept: Abstraction, Encapsulation.
 */
public abstract class Subject implements Storable {
    protected String name;
    protected int creditHours;
    
    public Subject() {
        this.name = "";
        this.creditHours = 3;
    }

    public Subject(String name, int creditHours) {
        this.name = name;
        this.creditHours = creditHours;
    }

    public String getName() { return name; }
    public int getCreditHours() { return creditHours; }

    // Abstract methods to be implemented by subclasses
    public abstract double calculateTotalPercentage();
    
    public String calculateGrade() {
        double percentage = calculateTotalPercentage();
        if (percentage >= 85) return "A";
        else if (percentage >= 80) return "A-";
        else if (percentage >= 75) return "B+";
        else if (percentage >= 71) return "B";
        else if (percentage >= 68) return "B-";
        else if (percentage >= 64) return "C+";
        else if (percentage >= 61) return "C";
        else if (percentage >= 50) return "D";
        else return "F";
    }

    public double calculateGradePoints() {
        String grade = calculateGrade();
        switch (grade) {
            case "A": return 4.0;
            case "A-": return 3.66;
            case "B+": return 3.33;
            case "B": return 3.0;
            case "B-": return 2.66;
            case "C+": return 2.33;
            case "C": return 2.0;
            case "D": return 1.5;
            default: return 0.0;
        }
    }
}
