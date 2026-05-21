package com.spms;

import javax.swing.SwingUtilities;

/**
 * Main Class
 * Purpose: Entry point for the Student Productivity Assistant.
 * Initializes the Java Swing GUI.
 */
public class Main {
    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
