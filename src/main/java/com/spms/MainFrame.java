package com.spms;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame Class
 * Purpose: The main JFrame containing the JTabbedPane to host all modules.
 * OOP Concept: Composition.
 */
public class MainFrame extends JFrame {
    
    private ProductivityManager prodManager;

    public MainFrame() {
        super("Student Productivity Assistant");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(950, 700);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        // Core Managers
        this.prodManager = new ProductivityManager();

        // Main Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        // Add Tabs
        tabbedPane.addTab("Dashboard", new DashboardPanel(prodManager));
        tabbedPane.addTab("GPA Calculator", new GPAPanel());
        tabbedPane.addTab("Task Manager", new TaskPanel(prodManager));
        tabbedPane.addTab("Timetable", new TimetablePanel());
        tabbedPane.addTab("Study Tracker", new ProductivityPanel(prodManager));
        tabbedPane.addTab("File Organizer", new FileManagerPanel());

        this.add(tabbedPane, BorderLayout.CENTER);
    }
}
