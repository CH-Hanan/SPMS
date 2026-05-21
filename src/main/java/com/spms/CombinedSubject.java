package com.spms;

/**
 * CombinedSubject Class
 * Purpose: Calculates grades for subjects combining Theory and Lab.
 * OOP Concept: Inheritance (extends Subject), Encapsulation.
 */
public class CombinedSubject extends Subject {
    private int theoryCredits;
    private int labCredits;

    // Theory Marks
    private double theorySessionalObtained; // Max 25
    private double midMarks;
    private double midTotal;
    private double finalMarks;
    private double finalTotal;

    // Lab Marks
    private double labSessionalMarks; // Assignments only
    private double labSessionalTotal;
    private double labMidMarks;
    private double labMidTotal;
    private double labFinalMarks;
    private double labFinalTotal;

    public CombinedSubject(String name, int theoryCredits, int labCredits,
                           double theorySessionalObtained,
                           double midMarks, double midTotal,
                           double finalMarks, double finalTotal,
                           double labSessionalMarks, double labSessionalTotal,
                           double labMidMarks, double labMidTotal,
                           double labFinalMarks, double labFinalTotal) {
        super(name, theoryCredits + labCredits);
        this.theoryCredits = theoryCredits;
        this.labCredits = labCredits;
        
        this.theorySessionalObtained = theorySessionalObtained;
        this.midMarks = midMarks;
        this.midTotal = midTotal;
        this.finalMarks = finalMarks;
        this.finalTotal = finalTotal;

        this.labSessionalMarks = labSessionalMarks;
        this.labSessionalTotal = labSessionalTotal;
        this.labMidMarks = labMidMarks;
        this.labMidTotal = labMidTotal;
        this.labFinalMarks = labFinalMarks;
        this.labFinalTotal = labFinalTotal;
    }

    @Override
    public double calculateTotalPercentage() {
        double theoryPercentage = 0;
        if (theoryCredits > 0) {
            double midWeight = (midTotal > 0) ? (midMarks / midTotal) * 25 : 0; // 25% weight
            double finalWeight = (finalTotal > 0) ? (finalMarks / finalTotal) * 50 : 0; // 50% weight
            // Sessional is already out of 25
            theoryPercentage = Math.min(100, theorySessionalObtained + midWeight + finalWeight);
        }

        double labPercentage = 0;
        if (labCredits > 0) {
            double labSessionalWeight = (labSessionalTotal > 0) ? (labSessionalMarks / labSessionalTotal) * 25 : 0; // 25% weight
            double labMidWeight = (labMidTotal > 0) ? (labMidMarks / labMidTotal) * 25 : 0; // 25% weight
            double labFinalWeight = (labFinalTotal > 0) ? (labFinalMarks / labFinalTotal) * 50 : 0; // 50% weight
            labPercentage = Math.min(100, labSessionalWeight + labMidWeight + labFinalWeight);
        }

        if (theoryCredits > 0 && labCredits > 0) {
            return ((theoryPercentage * theoryCredits) + (labPercentage * labCredits)) / (theoryCredits + labCredits);
        } else if (theoryCredits > 0) {
            return theoryPercentage;
        } else if (labCredits > 0) {
            return labPercentage;
        }
        return 0;
    }

    @Override
    public String toStorageString() {
        return "COMBINED|" + name + "|" + theoryCredits + "|" + labCredits;
    }

    @Override
    public void fromStorageString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length >= 4 && parts[0].equals("COMBINED")) {
            this.name = parts[1];
            this.theoryCredits = Integer.parseInt(parts[2]);
            this.labCredits = Integer.parseInt(parts[3]);
        }
    }
}
