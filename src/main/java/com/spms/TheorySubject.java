package com.spms;

/**
 * TheorySubject Class
 * Purpose: Calculates grades for theory-based subjects.
 * OOP Concept: Inheritance (extends Subject).
 */
public class TheorySubject extends Subject {
    private double assignmentMarks;
    private double quizMarks;
    private double midMarks;
    private double finalMarks;

    private double totalAssignment;
    private double totalQuiz;
    private double totalMid;
    private double totalFinal;

    public TheorySubject() {
        super();
    }

    public TheorySubject(String name, int creditHours, 
                         double assignmentMarks, double totalAssignment,
                         double quizMarks, double totalQuiz,
                         double midMarks, double totalMid,
                         double finalMarks, double totalFinal) {
        super(name, creditHours);
        this.assignmentMarks = assignmentMarks;
        this.totalAssignment = totalAssignment;
        this.quizMarks = quizMarks;
        this.totalQuiz = totalQuiz;
        this.midMarks = midMarks;
        this.totalMid = totalMid;
        this.finalMarks = finalMarks;
        this.totalFinal = totalFinal;
    }

    @Override
    public double calculateTotalPercentage() {
        // Beginner-friendly formula: Calculate percentage of each component
        double assignmentWeight = (totalAssignment > 0) ? (assignmentMarks / totalAssignment) * 10 : 0; // Assuming 10% weight
        double quizWeight = (totalQuiz > 0) ? (quizMarks / totalQuiz) * 15 : 0; // Assuming 15% weight
        double midWeight = (totalMid > 0) ? (midMarks / totalMid) * 25 : 0; // Assuming 25% weight
        double finalWeight = (totalFinal > 0) ? (finalMarks / totalFinal) * 50 : 0; // Assuming 50% weight

        return assignmentWeight + quizWeight + midWeight + finalWeight;
    }

    @Override
    public String toStorageString() {
        return "THEORY|" + name + "|" + creditHours + "|" + 
               assignmentMarks + "|" + totalAssignment + "|" +
               quizMarks + "|" + totalQuiz + "|" +
               midMarks + "|" + totalMid + "|" +
               finalMarks + "|" + totalFinal;
    }

    @Override
    public void fromStorageString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 11 && parts[0].equals("THEORY")) {
            this.name = parts[1];
            this.creditHours = Integer.parseInt(parts[2]);
            this.assignmentMarks = Double.parseDouble(parts[3]);
            this.totalAssignment = Double.parseDouble(parts[4]);
            this.quizMarks = Double.parseDouble(parts[5]);
            this.totalQuiz = Double.parseDouble(parts[6]);
            this.midMarks = Double.parseDouble(parts[7]);
            this.totalMid = Double.parseDouble(parts[8]);
            this.finalMarks = Double.parseDouble(parts[9]);
            this.totalFinal = Double.parseDouble(parts[10]);
        }
    }
}
