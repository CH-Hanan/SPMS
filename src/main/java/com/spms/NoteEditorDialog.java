package com.spms;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

/**
 * NoteEditorDialog Class
 * Purpose: Provides a rich-text editor for taking notes.
 * OOP Concept: Encapsulation, Event Handling.
 */
public class NoteEditorDialog extends JDialog {
    private JTextPane textPane;
    private File savedFile;

    public NoteEditorDialog(Frame owner, String subjectName, LocalFileManager fileManager) {
        super(owner, "Rich Text Notes - " + subjectName, true);
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

        // Toolbar for rich text features
        JToolBar toolBar = new JToolBar();
        
        Action boldAction = new StyledEditorKit.BoldAction();
        boldAction.putValue(Action.NAME, "Bold");
        JButton btnBold = new JButton(boldAction);
        
        Action italicAction = new StyledEditorKit.ItalicAction();
        italicAction.putValue(Action.NAME, "Italic");
        JButton btnItalic = new JButton(italicAction);
        
        Action underlineAction = new StyledEditorKit.UnderlineAction();
        underlineAction.putValue(Action.NAME, "Underline");
        JButton btnUnderline = new JButton(underlineAction);

        JButton btnColor = new JButton("Text Color");
        btnColor.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Choose Text Color", Color.BLACK);
            if (c != null) {
                MutableAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setForeground(attrs, c);
                textPane.setCharacterAttributes(attrs, false);
            }
        });

        JButton btnHighlight = new JButton("Highlight");
        btnHighlight.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Choose Highlight Color", Color.YELLOW);
            if (c != null) {
                MutableAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setBackground(attrs, c);
                textPane.setCharacterAttributes(attrs, false);
            }
        });

        toolBar.add(btnBold);
        toolBar.add(btnItalic);
        toolBar.add(btnUnderline);
        toolBar.addSeparator();
        toolBar.add(btnColor);
        toolBar.add(btnHighlight);

        add(toolBar, BorderLayout.NORTH);

        // Bottom panel for saving
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField txtFileName = new JTextField(15);
        JButton btnSave = new JButton("Save Note");

        bottomPanel.add(new JLabel("Note Name:"));
        bottomPanel.add(txtFileName);
        bottomPanel.add(btnSave);
        add(bottomPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            String name = txtFileName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a note name.");
                return;
            }
            if (!name.endsWith(".rtf")) {
                name += ".rtf";
            }

            try {
                // Ensure Subject folder exists
                File baseDir = new File("StudentData");
                File subjectDir = new File(baseDir, subjectName.isEmpty() ? "General" : subjectName);
                if (!subjectDir.exists()) subjectDir.mkdirs();

                File destFile = new File(subjectDir, name);
                
                // Save using RTFEditorKit to preserve rich text
                javax.swing.text.rtf.RTFEditorKit rtfKit = new javax.swing.text.rtf.RTFEditorKit();
                FileOutputStream fos = new FileOutputStream(destFile);
                rtfKit.write(fos, textPane.getDocument(), 0, textPane.getDocument().getLength());
                fos.close();

                this.savedFile = destFile;
                JOptionPane.showMessageDialog(this, "Note saved successfully!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving note: " + ex.getMessage());
            }
        });
    }

    public File getSavedFile() {
        return savedFile;
    }
}
