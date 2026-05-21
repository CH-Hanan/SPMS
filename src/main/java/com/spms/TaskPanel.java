package com.spms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * TaskPanel Class
 * Purpose: Manages tasks with JTable and simple rule-based suggestions, with subject filtering.
 * OOP Concept: Inheritance, Composition.
 */
public class TaskPanel extends JPanel {
    private StorageManager storageManager;
    private ArrayList<Task> taskList;
    private DefaultTableModel tableModel;
    private JLabel lblSuggestion;
    private JComboBox<String> filterCombo;
    
    private ProductivityManager prodManager;
    private JLabel lblAnimation;
    private Timer animTimer;

    public TaskPanel(ProductivityManager prodManager) {
        this.prodManager = prodManager;
        this.storageManager = new StorageManager();
        this.taskList = storageManager.loadItems("tasks.txt", Task::new);

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: Suggestion Label and Filter
        JPanel topPanel = new JPanel(new BorderLayout());
        lblSuggestion = new JLabel("Suggestion: Add a task to get started!");
        lblSuggestion.setFont(new Font("Arial", Font.BOLD, 14));
        lblSuggestion.setForeground(Color.BLUE);
        topPanel.add(lblSuggestion, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterCombo = new JComboBox<>();
        filterCombo.addItem("All Subjects");
        updateFilterOptions();
        filterPanel.add(new JLabel("Filter by Subject:"));
        filterPanel.add(filterCombo);
        topPanel.add(filterPanel, BorderLayout.SOUTH);

        this.add(topPanel, BorderLayout.NORTH);

        // Center: JTable
        String[] columns = {"Title", "Subject", "Due Date", "Priority", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        
        table.getTableHeader().setBackground(new Color(231, 76, 60)); // Nice Red
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        this.add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom: Input and Actions
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtTitle = new JTextField(15);
        JTextField txtSubject = new JTextField(10);
        JTextField txtDue = new JTextField(10);
        JComboBox<String> comboPriority = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        
        inputPanel.add(new JLabel("Title:")); inputPanel.add(txtTitle);
        inputPanel.add(new JLabel("Subj:")); inputPanel.add(txtSubject);
        inputPanel.add(new JLabel("Due:")); inputPanel.add(txtDue);
        inputPanel.add(new JLabel("Pri:")); inputPanel.add(comboPriority);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Add Task");
        JButton btnComplete = new JButton("Mark Completed");
        JButton btnDelete = new JButton("Delete Task");
        
        lblAnimation = new JLabel(" ");
        lblAnimation.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAnimation.setForeground(new Color(46, 204, 113)); // Green

        actionPanel.add(btnAdd);
        actionPanel.add(btnComplete);
        actionPanel.add(btnDelete);
        actionPanel.add(lblAnimation);

        bottomPanel.add(inputPanel);
        bottomPanel.add(actionPanel);
        this.add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();

        // Event Listeners
        btnAdd.addActionListener(e -> {
            if (!txtTitle.getText().isEmpty()) {
                Task t = new Task(txtTitle.getText(), 
                                  txtSubject.getText().isEmpty() ? "Gen" : txtSubject.getText(),
                                  txtDue.getText(),
                                  comboPriority.getSelectedItem().toString());
                taskList.add(t);
                storageManager.saveItems("tasks.txt", taskList);
                txtTitle.setText(""); txtSubject.setText(""); txtDue.setText("");
                updateFilterOptions();
                refreshTable();
            }
        });

        filterCombo.addActionListener(e -> refreshTable());

        btnComplete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String title = (String) tableModel.getValueAt(row, 0);
                Task t = findTaskByTitle(title);
                if (t != null && !t.isCompleted()) {
                    t.markCompleted();
                    storageManager.saveItems("tasks.txt", taskList);
                    refreshTable();
                    
                    prodManager.addPoints(5);
                    showPointAnimation();
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String title = (String) tableModel.getValueAt(row, 0);
                Task t = findTaskByTitle(title);
                if (t != null) {
                    taskList.remove(t);
                    storageManager.saveItems("tasks.txt", taskList);
                    updateFilterOptions();
                    refreshTable();
                }
            }
        });
    }

    private void showPointAnimation() {
        lblAnimation.setText("🌟 +5 Points!");
        lblAnimation.setForeground(new Color(46, 204, 113));
        
        if (animTimer != null && animTimer.isRunning()) {
            animTimer.stop();
        }
        
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

    private Task findTaskByTitle(String title) {
        for (Task t : taskList) {
            if (t.getTitle().equals(title)) {
                return t;
            }
        }
        return null;
    }

    private void updateFilterOptions() {
        String currentSelection = (String) filterCombo.getSelectedItem();
        filterCombo.removeAllItems();
        filterCombo.addItem("All Subjects");
        
        java.util.List<String> subjects = taskList.stream()
                .map(Task::getSubject)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
                
        for (String sub : subjects) {
            filterCombo.addItem(sub);
        }
        
        if (currentSelection != null && ((DefaultComboBoxModel<String>)filterCombo.getModel()).getIndexOf(currentSelection) >= 0) {
            filterCombo.setSelectedItem(currentSelection);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        String selectedFilter = (String) filterCombo.getSelectedItem();

        for (Task t : taskList) {
            if ("All Subjects".equals(selectedFilter) || t.getSubject().equals(selectedFilter)) {
                tableModel.addRow(new Object[]{
                    t.getTitle(), t.getSubject(), t.getDueDate(), t.getPriority(), 
                    t.isCompleted() ? "Done" : "Pending"
                });
            }
        }
        updateSuggestion();
    }

    private void updateSuggestion() {
        if (taskList.isEmpty()) {
            lblSuggestion.setText("Suggestion: Add your first task!");
            return;
        }

        // Rule: Suggest High Priority first (evaluate all tasks regardless of filter)
        for (Task t : taskList) {
            if (!t.isCompleted() && "High".equals(t.getPriority())) {
                lblSuggestion.setText("Suggestion: Study this task next -> " + t.getTitle() + " (High Risk)");
                return;
            }
        }

        for (Task t : taskList) {
            if (!t.isCompleted()) {
                lblSuggestion.setText("Suggestion: You should work on -> " + t.getTitle());
                return;
            }
        }
        lblSuggestion.setText("Suggestion: Great job! All tasks completed.");
    }
}
