package com.spms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * GPAPanel Class
 * Purpose: Allows students to calculate grades combining Theory/Lab, and calculates CGPA.
 * OOP Concept: Inheritance, Polymorphism.
 */
public class GPAPanel extends JPanel {

    public GPAPanel() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTabbedPane gpaTabs = new JTabbedPane();
        gpaTabs.addTab("Semester Subject GPA", createSubjectPanel());
        gpaTabs.addTab("CGPA Calculator", createCGPAPanel());

        JLabel title = new JLabel("GPA & Academic Performance Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        this.add(title, BorderLayout.NORTH);
        this.add(gpaTabs, BorderLayout.CENTER);
    }

    private JPanel createSubjectPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Basic Info
        JPanel basicPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        basicPanel.setBorder(BorderFactory.createTitledBorder("Subject Info"));
        JTextField txtName = new JTextField();
        JTextField txtTheoryCredit = new JTextField("3");
        JTextField txtLabCredit = new JTextField("1");
        
        basicPanel.add(new JLabel("Subject Name:")); basicPanel.add(txtName);
        basicPanel.add(new JLabel("Theory Credits:")); basicPanel.add(txtTheoryCredit);
        basicPanel.add(new JLabel("")); basicPanel.add(new JLabel("")); // Spacer
        basicPanel.add(new JLabel("Lab Credits (0 if none):")); basicPanel.add(txtLabCredit);

        // Theory Sessional Marks
        JPanel sessionalPanel = new JPanel(new GridLayout(4, 3, 10, 10));
        sessionalPanel.setBorder(BorderFactory.createTitledBorder("Theory Sessional (Max 25)"));
        
        JComboBox<String> weightageCombo = new JComboBox<>(new String[]{"15 Quiz / 10 Assignment", "10 Quiz / 15 Assignment"});
        JCheckBox chkPresentation = new JCheckBox("Include Presentation");
        
        JTextField txtQuiz = new JTextField("0");
        JTextField txtAssign = new JTextField("0");
        JTextField txtPres = new JTextField("0");
        txtPres.setEnabled(false);

        chkPresentation.addItemListener(e -> {
            txtPres.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            if (!txtPres.isEnabled()) txtPres.setText("0");
        });

        sessionalPanel.add(new JLabel("Weightage Target:")); sessionalPanel.add(weightageCombo); sessionalPanel.add(new JLabel(""));
        sessionalPanel.add(chkPresentation); sessionalPanel.add(new JLabel("Pres. Marks Obt:")); sessionalPanel.add(txtPres);
        sessionalPanel.add(new JLabel("Quiz Marks Obt:")); sessionalPanel.add(txtQuiz); sessionalPanel.add(new JLabel(""));
        sessionalPanel.add(new JLabel("Assign. Marks Obt:")); sessionalPanel.add(txtAssign); sessionalPanel.add(new JLabel(""));

        // Theory Exams
        JPanel examPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        examPanel.setBorder(BorderFactory.createTitledBorder("Theory Exams"));
        JTextField txtMidMarks = new JTextField("0"); JTextField txtMidTotal = new JTextField("25");
        JTextField txtFinalMarks = new JTextField("0"); JTextField txtFinalTotal = new JTextField("50");
        
        examPanel.add(new JLabel("Mid (Obt/Tot):")); examPanel.add(txtMidMarks); examPanel.add(txtMidTotal); examPanel.add(new JLabel(""));
        examPanel.add(new JLabel("Final (Obt/Tot):")); examPanel.add(txtFinalMarks); examPanel.add(txtFinalTotal); examPanel.add(new JLabel(""));

        // Lab Exams
        JPanel labPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        labPanel.setBorder(BorderFactory.createTitledBorder("Lab Marks (If Lab Credits > 0)"));
        JTextField txtLabSessMarks = new JTextField("0"); JTextField txtLabSessTotal = new JTextField("25");
        JTextField txtLabMidMarks = new JTextField("0"); JTextField txtLabMidTotal = new JTextField("25");
        JTextField txtLabFinalMarks = new JTextField("0"); JTextField txtLabFinalTotal = new JTextField("50");
        
        labPanel.add(new JLabel("Assignments (Obt/Tot):")); labPanel.add(txtLabSessMarks); labPanel.add(txtLabSessTotal); labPanel.add(new JLabel(""));
        labPanel.add(new JLabel("Mid-Term (Obt/Tot):")); labPanel.add(txtLabMidMarks); labPanel.add(txtLabMidTotal); labPanel.add(new JLabel(""));
        labPanel.add(new JLabel("Final-Term (Obt/Tot):")); labPanel.add(txtLabFinalMarks); labPanel.add(txtLabFinalTotal); labPanel.add(new JLabel(""));

        inputPanel.add(basicPanel);
        inputPanel.add(sessionalPanel);
        inputPanel.add(examPanel);
        inputPanel.add(labPanel);

        JButton btnCalc = new JButton("Calculate Subject Grade");
        btnCalc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCalc.setBackground(new Color(46, 204, 113));
        btnCalc.setForeground(Color.WHITE);

        btnCalc.addActionListener(e -> {
            try {
                int tCred = Integer.parseInt(txtTheoryCredit.getText());
                int lCred = Integer.parseInt(txtLabCredit.getText());

                double qMarks = Double.parseDouble(txtQuiz.getText());
                double aMarks = Double.parseDouble(txtAssign.getText());
                double pMarks = Double.parseDouble(txtPres.getText());

                double totalSessional = qMarks + aMarks + pMarks;
                if (totalSessional > 25) {
                    JOptionPane.showMessageDialog(this, "Total Sessional marks cannot exceed 25!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                CombinedSubject subject = new CombinedSubject(
                        txtName.getText().isEmpty() ? "Unknown Subject" : txtName.getText(),
                        tCred, lCred,
                        totalSessional,
                        Double.parseDouble(txtMidMarks.getText()), Double.parseDouble(txtMidTotal.getText()),
                        Double.parseDouble(txtFinalMarks.getText()), Double.parseDouble(txtFinalTotal.getText()),
                        Double.parseDouble(txtLabSessMarks.getText()), Double.parseDouble(txtLabSessTotal.getText()),
                        Double.parseDouble(txtLabMidMarks.getText()), Double.parseDouble(txtLabMidTotal.getText()),
                        Double.parseDouble(txtLabFinalMarks.getText()), Double.parseDouble(txtLabFinalTotal.getText())
                );
                
                showResultDialog(subject);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(new JScrollPane(inputPanel), BorderLayout.CENTER);
        
        JPanel bottom = new JPanel();
        bottom.add(btnCalc);
        mainPanel.add(bottom, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createCGPAPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Semester", "Credit Hours", "GPA"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtSem = new JTextField(10);
        JTextField txtCredits = new JTextField(5);
        JTextField txtGPA = new JTextField(5);
        JButton btnAdd = new JButton("Add Semester");

        topPanel.add(new JLabel("Semester:")); topPanel.add(txtSem);
        topPanel.add(new JLabel("Credits:")); topPanel.add(txtCredits);
        topPanel.add(new JLabel("GPA:")); topPanel.add(txtGPA);
        topPanel.add(btnAdd);
        
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblResult = new JLabel("Cumulative CGPA: 0.00");
        lblResult.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bottomPanel.add(lblResult);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            try {
                String sem = txtSem.getText();
                double credits = Double.parseDouble(txtCredits.getText());
                double gpa = Double.parseDouble(txtGPA.getText());
                
                model.addRow(new Object[]{sem, credits, gpa});
                
                txtSem.setText(""); txtCredits.setText(""); txtGPA.setText("");
                
                double totalPoints = 0;
                double totalCredits = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    double c = Double.parseDouble(model.getValueAt(i, 1).toString());
                    double g = Double.parseDouble(model.getValueAt(i, 2).toString());
                    totalPoints += (c * g);
                    totalCredits += c;
                }
                
                double cgpa = (totalCredits > 0) ? totalPoints / totalCredits : 0;
                lblResult.setText(String.format("Cumulative CGPA: %.2f", cgpa));
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private void showResultDialog(Subject subject) {
        double percentage = subject.calculateTotalPercentage();
        String grade = subject.calculateGrade();
        double points = subject.calculateGradePoints();

        String result = String.format("Subject: %s\nTotal Credits: %d\nTotal Percentage: %.2f%%\nGrade: %s\nGrade Points (GPA): %.2f",
                subject.getName(), subject.getCreditHours(), percentage, grade, points);

        JOptionPane.showMessageDialog(this, result, "GPA Calculation Result", JOptionPane.INFORMATION_MESSAGE);
    }
}
