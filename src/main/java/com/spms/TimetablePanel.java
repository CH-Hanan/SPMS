package com.spms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * TimetablePanel Class
 * Purpose: Manages and displays the student schedule using JTable with Day-wise
 * filtering.
 * OOP Concept: Inheritance, Composition.
 */
public class TimetablePanel extends JPanel {
    private StorageManager storageManager;
    private ArrayList<Lecture> lectures;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterDayCombo;

    public TimetablePanel() {
        this.storageManager = new StorageManager();
        this.lectures = storageManager.loadItems("lectures.txt", Lecture::new);

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Filter Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterDayCombo = new JComboBox<>(new String[] { "All Days", "Mon", "Tue", "Wed", "Thu", "Fri" });
        
        // Auto-detect current day
        java.time.DayOfWeek dayOfWeek = java.time.LocalDate.now().getDayOfWeek();
        String today = dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH); // e.g., "Mon", "Tue"
        
        // Set combo box to today if it's a weekday, else keep "All Days"
        boolean isWeekday = !today.equals("Sat") && !today.equals("Sun");
        if (isWeekday) {
            filterDayCombo.setSelectedItem(today);
        }

        topPanel.add(new JLabel("View Schedule For:"));
        topPanel.add(filterDayCombo);
        this.add(topPanel, BorderLayout.NORTH);

        // Center Table (Removed Status/Attended column)
        String[] columns = { "Subject", "Teacher", "Day", "Time", "Room" };
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        
        table.getTableHeader().setBackground(new Color(41, 128, 185)); // Nice Blue
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        this.add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom Input
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtTitle = new JTextField(10);
        JTextField txtSubj = new JTextField(8);
        JComboBox<String> comboDay = new JComboBox<>(new String[] { "Mon", "Tue", "Wed", "Thu", "Fri" });
        JTextField txtTime = new JTextField(8);
        JTextField txtRoom = new JTextField(8);
        JButton btnAdd = new JButton("Add Class");

        bottomPanel.add(new JLabel("Teacher:"));
        bottomPanel.add(txtTitle);
        bottomPanel.add(new JLabel("Subj:"));
        bottomPanel.add(txtSubj);
        bottomPanel.add(new JLabel("Day:"));
        bottomPanel.add(comboDay);
        bottomPanel.add(new JLabel("Time:"));
        bottomPanel.add(txtTime);
        bottomPanel.add(new JLabel("Room:"));
        bottomPanel.add(txtRoom);
        bottomPanel.add(btnAdd);

        this.add(bottomPanel, BorderLayout.SOUTH);
        refreshTable();

        btnAdd.addActionListener(e -> {
            if (!txtTitle.getText().isEmpty()) {
                String newDay = comboDay.getSelectedItem().toString();
                String newTime = txtTime.getText().trim();
                
                // Parse new time range
                int[] newRange = parseTimeRange(newTime);
                if (newRange == null) {
                    JOptionPane.showMessageDialog(this, "Invalid time format. Use 'HH:MM - HH:MM' (e.g., '8:30 - 10:00' or '1:40 pm - 3:05 pm').", "Format Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (hasTimeConflict(newDay, newRange)) {
                    JOptionPane.showMessageDialog(this, "Time conflict detected! This class overlaps with an existing class on " + newDay + ".", "Conflict Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Lecture l = new Lecture(txtTitle.getText(), txtSubj.getText(),
                        newDay, newTime, txtRoom.getText());
                lectures.add(l);
                storageManager.saveItems("lectures.txt", lectures);
                txtTitle.setText("");
                txtSubj.setText("");
                txtTime.setText("");
                txtRoom.setText("");
                refreshTable();
            }
        });

        filterDayCombo.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        String selectedDay = (String) filterDayCombo.getSelectedItem();

        for (Lecture l : lectures) {
            String day = l.getScheduleTime().split(" at ")[0]; // Extract day

            if ("All Days".equals(selectedDay) || day.equals(selectedDay)) {
                tableModel.addRow(new Object[] {
                        l.getSubject(), l.getTitle(),
                        day,
                        l.getScheduleTime().split(" at ")[1], // Extract time
                        l.getRoom()
                });
            }
        }
    }

    private boolean hasTimeConflict(String day, int[] newRange) {
        for (Lecture l : lectures) {
            String existingDay = l.getScheduleTime().split(" at ")[0];
            if (existingDay.equals(day)) {
                String existingTimeRange = l.getScheduleTime().split(" at ")[1];
                int[] existingRange = parseTimeRange(existingTimeRange);
                if (existingRange != null) {
                    // Check overlap: newStart < existingEnd && newEnd > existingStart
                    if (newRange[0] < existingRange[1] && newRange[1] > existingRange[0]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int[] parseTimeRange(String timeRange) {
        try {
            String[] parts = timeRange.split("-");
            if (parts.length != 2) return null;
            int start = parseTime(parts[0].trim());
            int end = parseTime(parts[1].trim());
            return new int[]{start, end};
        } catch (Exception e) {
            return null;
        }
    }

    private int parseTime(String time) {
        time = time.toLowerCase().trim();
        boolean isPM = time.contains("pm");
        boolean isAM = time.contains("am");
        time = time.replace("am", "").replace("pm", "").trim();

        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0].trim());
        int minute = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 0;

        if (isPM && hour != 12) {
            hour += 12;
        } else if (isAM && hour == 12) {
            hour = 0;
        } else if (!isPM && !isAM) {
            // Fallback heuristic if no am/pm specified
            if (hour >= 1 && hour <= 7) {
                hour += 12;
            }
        }
        return hour * 60 + minute;
    }
}
