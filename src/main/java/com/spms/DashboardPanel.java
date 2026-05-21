package com.spms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * DashboardPanel Class
 * Purpose: Central overview of academic progress.
 * OOP Concept: Inheritance, Composition.
 */
public class DashboardPanel extends JPanel {
    private ProductivityManager prodManager;
    private StorageManager storageManager;
    
    private JLabel lblPoints;
    private JLabel lblPendingTasks;
    private JLabel lblTotalSessions;

    public DashboardPanel(ProductivityManager prodManager) {
        this.prodManager = prodManager;
        this.storageManager = new StorageManager();

        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(new Color(245, 247, 250)); // Light modern background
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Hero Section ---
        JPanel heroPanel = new RoundedPanel(15, new Color(41, 128, 185)); // Nice blue
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Welcome back to SPMS!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Your personal command center for academic success.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(220, 235, 255));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        heroPanel.add(title);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        heroPanel.add(subtitle);

        this.add(heroPanel, BorderLayout.NORTH);

        // --- Cards Section ---
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(new Color(245, 247, 250));

        // Card 1: Productivity Points
        RoundedPanel pnlPoints = createCard("Productivity Points", new Color(46, 204, 113));
        lblPoints = createCardValueLabel("0");
        pnlPoints.add(lblPoints, BorderLayout.CENTER);
        
        // Card 2: Pending Tasks
        RoundedPanel pnlTasks = createCard("Pending Tasks", new Color(231, 76, 60));
        lblPendingTasks = createCardValueLabel("0");
        pnlTasks.add(lblPendingTasks, BorderLayout.CENTER);

        // Card 3: Study Sessions
        RoundedPanel pnlSessions = createCard("Study Sessions", new Color(155, 89, 182));
        lblTotalSessions = createCardValueLabel("0");
        pnlSessions.add(lblTotalSessions, BorderLayout.CENTER);

        cardsPanel.add(pnlPoints);
        cardsPanel.add(pnlTasks);
        cardsPanel.add(pnlSessions);

        this.add(cardsPanel, BorderLayout.CENTER);

        // --- Tip Section ---
        RoundedPanel tipPanel = new RoundedPanel(15, Color.WHITE);
        tipPanel.setLayout(new BorderLayout());
        tipPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTip = new JLabel("💡 Tip: Use the GPA Calculator to track grades!");
        lblTip.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblTip.setForeground(new Color(100, 100, 100));
        tipPanel.add(lblTip, BorderLayout.CENTER);
        
        this.add(tipPanel, BorderLayout.SOUTH);

        // Update callback
        prodManager.setOnPointsUpdated(this::refreshData);
        refreshData();
    }

    private RoundedPanel createCard(String titleText, Color topColor) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        
        // Custom top panel to hold strip and title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JPanel strip = new JPanel();
        strip.setBackground(topColor);
        strip.setPreferredSize(new Dimension(0, 5));
        topPanel.add(strip, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel(titleText, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(80, 80, 80));
        lblTitle.setBorder(new EmptyBorder(10, 0, 0, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);

        card.add(topPanel, BorderLayout.NORTH);

        return card;
    }

    private JLabel createCardValueLabel(String value) {
        JLabel lbl = new JLabel(value, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lbl.setForeground(new Color(50, 50, 50));
        return lbl;
    }

    public void refreshData() {
        lblPoints.setText(String.valueOf(prodManager.getTotalPoints()));
        
        ArrayList<Task> tasks = storageManager.loadItems("tasks.txt", Task::new);
        long pendingCount = tasks.stream().filter(t -> !t.isCompleted()).count();
        lblPendingTasks.setText(String.valueOf(pendingCount));

        ArrayList<StudySession> sessions = storageManager.loadItems("productivity.txt", StudySession::new);
        lblTotalSessions.setText(String.valueOf(sessions.size()));
    }

    // Inner class for rounded panels (Beginner friendly custom painting)
    class RoundedPanel extends JPanel {
        private int radius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.radius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false); // Make transparent so custom paint works
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            // Anti-aliasing for smooth corners
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        }
    }
}
