package com.spms;

import java.io.*;
import java.util.Scanner;

/**
 * ProductivityManager Class
 * Purpose: Manages overall productivity points and stats with persistence.
 * OOP Concept: Encapsulation, File I/O.
 */
public class ProductivityManager {
    private int totalPoints;
    private Runnable onPointsUpdated;
    private static final String POINTS_FILE = "StudentData/Database/points.txt";

    public ProductivityManager() {
        this.totalPoints = loadPoints();
    }

    public void addPoints(int points) {
        this.totalPoints += points;
        savePoints();
        if (onPointsUpdated != null) {
            onPointsUpdated.run();
        }
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setOnPointsUpdated(Runnable callback) {
        this.onPointsUpdated = callback;
    }

    private void savePoints() {
        try {
            File dir = new File("StudentData/Database");
            if (!dir.exists()) dir.mkdirs();
            
            PrintWriter writer = new PrintWriter(new FileWriter(POINTS_FILE));
            writer.println(this.totalPoints);
            writer.close();
        } catch (IOException e) {
            System.err.println("Could not save points.");
        }
    }
    
    private int loadPoints() {
        try {
            File file = new File(POINTS_FILE);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextInt()) {
                    int p = scanner.nextInt();
                    scanner.close();
                    return p;
                }
                scanner.close();
            }
        } catch (Exception e) {
            System.err.println("Could not load points.");
        }
        return 0;
    }
}
