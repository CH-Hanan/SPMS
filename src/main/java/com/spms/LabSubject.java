package com.spms;

/**
 * LabSubject Class
 * Purpose: Calculates grades for lab-based subjects (no quizzes/midterms).
 * OOP Concept: Inheritance, Polymorphism.
 */
public class LabSubject extends Subject {
    private double assignmentMarks;
    private double finalMarks;

    private double totalAssignment;
    private double totalFinal;

    public LabSubject() {
        super();
    }

    public LabSubject(String name, int creditHours, 
                      double assignmentMarks, double totalAssignment,
                      double finalMarks, double totalFinal) {
        super(name, creditHours);
        this.assignmentMarks = assignmentMarks;
        this.totalAssignment = totalAssignment;
        this.finalMarks = finalMarks;
        this.totalFinal = totalFinal;
    }

    @Override
    public double calculateTotalPercentage() {
        // Lab formula: 40% assignments, 60% final
        double assignmentWeight = (totalAssignment > 0) ? (assignmentMarks / totalAssignment) * 40 : 0;
        double finalWeight = (totalFinal > 0) ? (finalMarks / totalFinal) * 60 : 0;

        return assignmentWeight + finalWeight;
    }

    @Override
    public String toStorageString() {
        return "LAB|" + name + "|" + creditHours + "|" + 
               assignmentMarks + "|" + totalAssignment + "|" +
               finalMarks + "|" + totalFinal;
    }

    @Override
    public void fromStorageString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 7 && parts[0].equals("LAB")) {
            this.name = parts[1];
            this.creditHours = Integer.parseInt(parts[2]);
            this.assignmentMarks = Double.parseDouble(parts[3]);
            this.totalAssignment = Double.parseDouble(parts[4]);
            this.finalMarks = Double.parseDouble(parts[5]);
            this.totalFinal = Double.parseDouble(parts[6]);
        }
    }
}
