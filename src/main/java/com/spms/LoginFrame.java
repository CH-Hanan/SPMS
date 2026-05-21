package com.spms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * LoginFrame Class
 * Purpose: Provides a modern Login and Registration screen.
 * OOP Concept: Encapsulation, File I/O.
 */
public class LoginFrame extends JFrame {
    private static final String USERS_FILE = "StudentData/Database/users.txt";
    private HashMap<String, String> usersDatabase;

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        super("Welcome to SPMS - Login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 550);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        usersDatabase = loadUsers();

        // Main background panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Top Hero Area
        JPanel heroPanel = new JPanel();
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setOpaque(false);
        
        JLabel title = new JLabel("Welcome Back!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(41, 128, 185));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Login to your Productivity Workspace");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(120, 120, 120));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        heroPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        heroPanel.add(title);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        heroPanel.add(subtitle);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Center Login Form (Inside a Card)
        RoundedPanel formCard = new RoundedPanel(20, Color.WHITE);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(100, 100, 100));
        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(100, 100, 100));
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        formCard.add(lblUser);
        formCard.add(Box.createRigidArea(new Dimension(0, 5)));
        formCard.add(txtUsername);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        formCard.add(lblPass);
        formCard.add(Box.createRigidArea(new Dimension(0, 5)));
        formCard.add(txtPassword);
        formCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Buttons
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton btnRegister = new JButton("Register New Account");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegister.setBackground(new Color(230, 230, 230));
        btnRegister.setForeground(new Color(80, 80, 80));
        btnRegister.setFocusPainted(false);
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        formCard.add(btnLogin);
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        formCard.add(btnRegister);

        mainPanel.add(heroPanel, BorderLayout.NORTH);
        mainPanel.add(formCard, BorderLayout.CENTER);

        this.add(mainPanel);

        // Action Listeners
        btnLogin.addActionListener(e -> attemptLogin());
        btnRegister.addActionListener(e -> registerUser());
    }

    private void attemptLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Username and Password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usersDatabase.containsKey(user) && usersDatabase.get(user).equals(pass)) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + user + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            launchMainApp();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerUser() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a Username and Password to register.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usersDatabase.containsKey(user)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please login or choose another.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add and Save
        usersDatabase.put(user, pass);
        saveUsers();
        JOptionPane.showMessageDialog(this, "Account Registered Successfully! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void launchMainApp() {
        this.dispose(); // Close login window
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }

    private HashMap<String, String> loadUsers() {
        HashMap<String, String> map = new HashMap<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            // Default Credentials if no file exists
            map.put("admin", "admin");
            saveDefaultUser();
            return map;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    map.put(parts[0], parts[1]);
                }
            }
        } catch (Exception e) {
            System.err.println("Could not load users.");
        }
        return map;
    }

    private void saveDefaultUser() {
        File dir = new File("StudentData/Database");
        if (!dir.exists()) dir.mkdirs();
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            writer.println("admin:admin");
        } catch (IOException e) {
            System.err.println("Could not save default user.");
        }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (String user : usersDatabase.keySet()) {
                writer.println(user + ":" + usersDatabase.get(user));
            }
        } catch (IOException e) {
            System.err.println("Could not save users.");
        }
    }

    // Custom Rounded Panel for the Card
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
