package com.spms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * FileManagerPanel Class
 * Purpose: UI for organizing local study files using Swing and JFileChooser, with Subject filtering.
 * OOP Concept: Inheritance, Composition.
 */
public class FileManagerPanel extends JPanel {
    private LocalFileManager fileManager;
    private StorageManager storageManager;
    private ArrayList<FileRecord> fileRecords;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterCombo;

    public FileManagerPanel() {
        this.fileManager = new LocalFileManager();
        this.storageManager = new StorageManager();
        this.fileRecords = storageManager.loadItems("files.txt", FileRecord::new);

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Control Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtSubject = new JTextField(15);
        JButton btnImport = new JButton("Import File");
        JButton btnCreateNote = new JButton("Create Note");
        JButton btnOpen = new JButton("Open Selected");
        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60)); // Red for delete
        btnDelete.setForeground(Color.WHITE);

        filterCombo = new JComboBox<>();
        filterCombo.addItem("All Subjects");
        updateFilterOptions();

        topPanel.add(new JLabel("Subject:"));
        topPanel.add(txtSubject);
        topPanel.add(btnImport);
        topPanel.add(btnCreateNote);
        topPanel.add(new JLabel("  |  Filter:"));
        topPanel.add(filterCombo);
        topPanel.add(btnOpen);
        topPanel.add(btnDelete);

        this.add(topPanel, BorderLayout.NORTH);

        // Center Table
        String[] columns = {"Subject", "File Name", "File Type", "Location"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        
        table.getTableHeader().setBackground(new Color(46, 204, 113)); // Nice Green
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        this.add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();

        // Event Listeners
        btnImport.addActionListener(e -> {
            String subject = txtSubject.getText().trim();
            if (subject.isEmpty()) subject = "General";

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                FileRecord record = fileManager.importFile(selectedFile, subject);
                if (record != null) {
                    fileRecords.add(record);
                    storageManager.saveItems("files.txt", fileRecords);
                    updateFilterOptions();
                    filterCombo.setSelectedItem(subject); // Auto-filter to the new subject
                    refreshTable();
                    txtSubject.setText("");
                    JOptionPane.showMessageDialog(this, "File successfully imported to StudentData/" + subject);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to import file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCreateNote.addActionListener(e -> {
            String subject = txtSubject.getText().trim();
            if (subject.isEmpty()) subject = "General";
            
            // Get the main frame as owner
            Window window = SwingUtilities.getWindowAncestor(this);
            Frame owner = (window instanceof Frame) ? (Frame) window : null;
            
            NoteEditorDialog dialog = new NoteEditorDialog(owner, subject, fileManager);
            dialog.setVisible(true);
            
            File savedFile = dialog.getSavedFile();
            if (savedFile != null) {
                FileRecord record = new FileRecord(savedFile.getName(), savedFile.getAbsolutePath(), subject, "rtf");
                fileRecords.add(record);
                storageManager.saveItems("files.txt", fileRecords);
                updateFilterOptions();
                filterCombo.setSelectedItem(subject);
                refreshTable();
                txtSubject.setText("");
            }
        });

        filterCombo.addActionListener(e -> refreshTable());

        btnOpen.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String filePath = (String) tableModel.getValueAt(row, 3);
                fileManager.openFile(filePath);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a file to open.");
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String filePath = (String) tableModel.getValueAt(row, 3);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this file?\nThis action cannot be undone.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    File fileToDelete = new File(filePath).getAbsoluteFile();
                    boolean deleted = false;
                    if (fileToDelete.exists()) {
                        deleted = fileToDelete.delete();
                    } else {
                        deleted = true; // If it doesn't exist on disk, we still want to remove from list
                    }
                    
                    if (deleted) {
                        fileRecords.removeIf(r -> r.getFilePath().equals(filePath));
                        storageManager.saveItems("files.txt", fileRecords);
                        updateFilterOptions();
                        refreshTable();
                        JOptionPane.showMessageDialog(this, "File permanently deleted from application folder.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Could not delete file. It might be currently open in another program.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a file to delete.");
            }
        });
    }

    private void updateFilterOptions() {
        String currentSelection = (String) filterCombo.getSelectedItem();
        filterCombo.removeAllItems();
        filterCombo.addItem("All Subjects");
        
        java.util.List<String> subjects = fileRecords.stream()
                .map(FileRecord::getSubject)
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
        
        for (FileRecord r : fileRecords) {
            if ("All Subjects".equals(selectedFilter) || r.getSubject().equals(selectedFilter)) {
                tableModel.addRow(new Object[]{
                    r.getSubject(), r.getFileName(), r.getFileType(), r.getFilePath()
                });
            }
        }
    }
}
