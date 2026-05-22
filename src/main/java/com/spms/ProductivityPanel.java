package com.spms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * ProductivityPanel Class
 * Purpose: Customizable Study Tracker with a modern UI, History, and Live Points.
 * OOP Concept: Encapsulation, Composition.
 */
public class ProductivityPanel extends JPanel {
    private int timeSeconds;
    private int initialDurationMinutes; // to track how many points to award and save
    private Timer swingTimer;
    private Timer animTimer;
    
    private JLabel lblTimer;
    private JLabel lblAnimation;
    private JComboBox<Integer> comboDuration;
    private JTextField txtSubject;
    private JLabel lblTotalSessions;
    
    private JButton btnStart;
    private JButton btnPause;
    private JButton btnReset;
    private JButton btnHistory;

    private StorageManager storageManager;
    private ArrayList<StudySession> sessions;
    private ProductivityManager prodManager;

    public ProductivityPanel(ProductivityManager prodManager) {
        this.prodManager = prodManager;
        this.storageManager = new StorageManager();
        this.sessions = storageManager.loadItems("productivity.txt", StudySession::new);

        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(new Color(245, 247, 250));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Hero Section ---
        JPanel heroPanel = new RoundedPanel(15, new Color(155, 89, 182)); // Purple theme
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Study Focus Tracker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Stay focused. Earn points. Track your progress.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(230, 210, 250));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        heroPanel.add(title);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        heroPanel.add(subtitle);
        this.add(heroPanel, BorderLayout.NORTH);

        // --- Center Tracking Area ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        // We use a white card for the timer
        RoundedPanel timerCard = new RoundedPanel(20, Color.WHITE);
        timerCard.setLayout(new BoxLayout(timerCard, BoxLayout.Y_AXIS));
        timerCard.setBorder(new EmptyBorder(30, 50, 30, 50));
        
        // Inputs
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setOpaque(false);
        inputPanel.add(new JLabel("Subject / Topic:"));
        txtSubject = new JTextField("General");
        inputPanel.add(txtSubject);
        
        inputPanel.add(new JLabel("Duration (Minutes):"));
        Integer[] durations = {1, 15, 25, 30, 45, 60, 90, 120}; // Added 1 for testing
        comboDuration = new JComboBox<>(durations);
        comboDuration.setSelectedItem(25); // Default
        inputPanel.add(comboDuration);
        
        // Live Points Animation Label
        lblAnimation = new JLabel(" ");
        lblAnimation.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblAnimation.setForeground(new Color(46, 204, 113)); // Green
        lblAnimation.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Timer Display
        lblTimer = new JLabel("25:00", SwingConstants.CENTER);
        lblTimer.setFont(new Font("Segoe UI", Font.BOLD, 80));
        lblTimer.setForeground(new Color(50, 50, 50));
        lblTimer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        controlPanel.setOpaque(false);
        btnStart = createStyledButton("Start", new Color(46, 204, 113));
        btnPause = createStyledButton("Pause", new Color(241, 196, 15));
        btnReset = createStyledButton("Reset", new Color(231, 76, 60));
        
        controlPanel.add(btnStart);
        controlPanel.add(btnPause);
        controlPanel.add(btnReset);

        // Bottom Info
        JPanel bottomInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottomInfoPanel.setOpaque(false);
        lblTotalSessions = new JLabel("Total Completed Sessions: " + sessions.size());
        lblTotalSessions.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblTotalSessions.setForeground(new Color(120, 120, 120));
        
        btnHistory = new JButton("📜 History");
        btnHistory.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHistory.setBackground(new Color(200, 200, 200));
        btnHistory.setFocusPainted(false);
        
        bottomInfoPanel.add(lblTotalSessions);
        bottomInfoPanel.add(btnHistory);

        timerCard.add(inputPanel);
        timerCard.add(Box.createRigidArea(new Dimension(0, 10)));
        timerCard.add(lblAnimation);
        timerCard.add(Box.createRigidArea(new Dimension(0, 5)));
        timerCard.add(lblTimer);
        timerCard.add(Box.createRigidArea(new Dimension(0, 30)));
        timerCard.add(controlPanel);
        timerCard.add(Box.createRigidArea(new Dimension(0, 20)));
        timerCard.add(bottomInfoPanel);

        centerWrapper.add(timerCard);
        this.add(centerWrapper, BorderLayout.CENTER);

        // --- Logic ---
        comboDuration.addActionListener(e -> {
            if (swingTimer == null || !swingTimer.isRunning()) {
                resetTimer();
            }
        });

        swingTimer = new Timer(1000, e -> {
            timeSeconds--;
            updateTimerLabel();

            // Real-time point award every minute (60 seconds)
            if (timeSeconds > 0 && timeSeconds % 60 == 0) {
                prodManager.addPoints(1); // Realtime Dashboard update!
                showPointAnimation();
            }

            if (timeSeconds <= 0) {
                swingTimer.stop();
                timeSeconds = 0;
                updateTimerLabel();
                completeSession();
            }
        });

        btnStart.addActionListener(e -> {
            if (timeSeconds == 0 && (swingTimer == null || !swingTimer.isRunning())) {
                resetTimer();
            }
            comboDuration.setEnabled(false);
            swingTimer.start();
        });
        
        btnPause.addActionListener(e -> swingTimer.stop());
        
        btnReset.addActionListener(e -> {
            swingTimer.stop();
            resetTimer();
        });

        btnHistory.addActionListener(e -> showHistoryDialog());

        resetTimer(); // Initialize display
    }

    private void showPointAnimation() {
        lblAnimation.setText("🌟 +1 Point!");
        lblAnimation.setForeground(new Color(46, 204, 113));
        
        if (animTimer != null && animTimer.isRunning()) {
            animTimer.stop();
        }
        
        // Simple fade-out simulation using Timer
        animTimer = new Timer(100, null);
        animTimer.addActionListener(new java.awt.event.ActionListener() {
            int alpha = 255;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                alpha -= 15;
                if (alpha <= 0) {
                    alpha = 0;
                    lblAnimation.setText(" ");
                    animTimer.stop();
                } else {
                    lblAnimation.setForeground(new Color(46, 204, 113, alpha));
                }
            }
        });
        animTimer.start();
    }

    private void showHistoryDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "📜 Study History", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        
        String[] columns = {"Date", "Subject", "Duration (Mins)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (StudySession s : sessions) {
            model.addRow(new Object[]{s.getDate(), s.getSubject(), s.getDurationMinutes()});
        }
        
        JTable table = new JTable(model);
        table.getTableHeader().setBackground(new Color(155, 89, 182));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(ev -> dialog.dispose());
        JPanel bottom = new JPanel();
        bottom.add(btnClose);
        dialog.add(bottom, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }

    private void resetTimer() {
        initialDurationMinutes = (Integer) comboDuration.getSelectedItem();
        timeSeconds = initialDurationMinutes * 60;
        comboDuration.setEnabled(true);
        lblAnimation.setText(" ");
        updateTimerLabel();
    }

    private void completeSession() {
        String subj = txtSubject.getText().trim();
        if (subj.isEmpty()) subj = "General";
        
        StudySession newSession = new StudySession(subj, initialDurationMinutes, LocalDate.now().toString());
        sessions.add(newSession);
        storageManager.saveItems("productivity.txt", sessions);
        
        lblTotalSessions.setText("Total Completed Sessions: " + sessions.size());
        
        comboDuration.setEnabled(true);
        JOptionPane.showMessageDialog(this, "Session Complete! Points have been added live to your Dashboard.", "Great Job!", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTimerLabel() {
        int minutes = timeSeconds / 60;
        int seconds = timeSeconds % 60;
        lblTimer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(100, 40));
        return btn;
    }

    // Inner class for rounded panels
    class RoundedPanel extends JPanel {
        private int radius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.radius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        }
    }
}
