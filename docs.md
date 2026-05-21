# Student Productivity Assistant - Project Documentation

## 1. Full Project Overview
The **Student Productivity Assistant** is a desktop application designed to solve common organizational challenges faced by university students. It is a completely offline, file-based tool built entirely in **Core Java** and **Java Swing**. It eliminates the need for complex databases or internet connectivity, offering a secure, self-contained environment. The system features a Smart Local File Organizer, an Advanced Task Manager, a Timetable Tracker, a Focus/Study Pomodoro Tracker, a comprehensive GPA Calculator, and a Campus Map.

## 2. Problem Statement
University students often struggle with digital disorganization. Study materials (PDFs, images) are lost in cluttered "Downloads" folders, assignments are forgotten due to poor task tracking, and manually calculating GPA or tracking attendance risks is tedious and error-prone. Existing solutions are either too complex, require constant internet connectivity, or lock data behind cloud paywalls. 

## 3. Objectives
- Provide a completely offline, unified hub for student productivity.
- Automate the organization of academic files into logical local directories.
- Offer smart, rule-based suggestions to help prioritize tasks.
- Provide a robust GPA calculator handling both Theory and Lab grading structures.

## 4. Functional Requirements
- **File Management:** Import and automatically categorize files into `StudentData/<Subject>` folders.
- **Task Management:** Add tasks with priority levels and due dates; receive automated study suggestions.
- **Timetable:** Log and track weekly lectures and room locations.
- **Focus Tracker:** A 25-minute Pomodoro timer that awards productivity points.
- **GPA Calculation:** Calculate percentages, grades, and grade points for both Theory (Assignments, Quizzes, Midterm, Final) and Lab (Assignments, Final) subjects.
- **Campus Map:** Interactive static buttons showing campus location details.

## 5. Non-Functional Requirements
- **Simplicity:** The UI must be straightforward, utilizing standard Swing components without overwhelming styling.
- **Independence:** Must run entirely locally without external APIs, MySQL databases, or cloud integrations.
- **Performance:** File I/O operations must be efficient, utilizing lightweight delimited text files (`.txt`).
- **Modularity:** Code must be cleanly separated to allow easy explanations during academic presentations (vivas).

## 6. OOP Explanation
The project strictly adheres to Object-Oriented Programming principles:
- **Encapsulation:** All model properties (e.g., `marks`, `creditHours`) are kept `private` and accessed only via getters/setters, ensuring data integrity.
- **Inheritance:** `TheorySubject` and `LabSubject` both inherit common properties (name, credit hours) and methods (calculateGrade) from the parent `Subject` class.
- **Abstraction:** The `Subject` class is `abstract` because a "Subject" on its own cannot calculate a total percentage without knowing if it is a Theory or Lab type. It forces child classes to implement `calculateTotalPercentage()`.
- **Polymorphism:** The `StorageManager` saves arrays of `Storable` objects. At runtime, it dynamically calls `toStorageString()` depending on whether the object is a `Task`, `Lecture`, or `AttendanceRecord`.
- **Interfaces:** `Storable`, `Trackable`, and `Schedulable` define contracts that guarantee certain behaviors across disparate classes.

## 7. Class Relationships
- `MainFrame` **has-a** `JTabbedPane` containing multiple UI Panels (`DashboardPanel`, `GPAPanel`, etc.) -> *Composition*.
- `DashboardPanel` **has-a** `ProductivityManager` to display points -> *Aggregation*.
- `TheorySubject` **is-a** `Subject` -> *Inheritance*.
- `StorageManager` **uses** `Storable` -> *Dependency*.
- `Task` **implements** `Trackable` and `Storable` -> *Realization*.

## 8. Swing GUI Structure
The application uses a modular Swing layout:
- **`JFrame` (MainFrame):** The main window container.
- **`JTabbedPane`:** Allows smooth switching between the 7 core modules.
- **`JPanel`:** Each tab (e.g., `TaskPanel`, `GPAPanel`) is a separate class extending `JPanel`, utilizing `BorderLayout` and `GridLayout` to organize components.
- **`JTable`:** Used extensively in Tasks, Timetables, and Attendance to display structured data clearly.
- **`JOptionPane`:** Used for pop-up alerts, GPA calculation results, and Campus Map information.

## 9. File Handling Logic
All data is stored inside a local `StudentData/` folder. 
The `StorageManager.java` class handles serialization by converting objects into pipe-delimited strings (`|`). 
For example, a task is saved as: `Finish Report|CS101|Tomorrow|High|false`. 
To load data, Java's `Scanner` reads the text file line-by-line, uses `split("\\|")` to extract the fields, and reconstructs the Java Objects.

## 10. GPA Calculation Formulas
The GPA engine (`Subject.java`, `TheorySubject.java`, `LabSubject.java`) uses beginner-friendly formulas:

**Theory Subject Weightage:**
- Assignments: 10% `((Obtained/Total) * 10)`
- Quizzes: 15% `((Obtained/Total) * 15)`
- Midterm: 25% `((Obtained/Total) * 25)`
- Final: 50% `((Obtained/Total) * 50)`

**Lab Subject Weightage:**
- Lab Assignments: 40% `((Obtained/Total) * 40)`
- Lab Final: 60% `((Obtained/Total) * 60)`

**Grading Scale (If/Else Logic):**
- `>= 85` = A (4.0)
- `>= 80` = A- (3.66)
- `>= 75` = B+ (3.33)
- `< 50` = F (0.0)

## 11. Rule-Based Suggestion Logic
Found in `TaskPanel.java`. It iterates through the list of tasks:
1. It first looks for any incomplete task with a "High" priority. If found, it immediately suggests: *"Study this task next -> [Task Name] (High Risk)"*.
2. If no High priority tasks exist, it suggests the first incomplete task it finds.
3. If all are complete, it returns a congratulatory message.

## 13. Beginner-Friendly Java Code Examples
**Polymorphism & File Saving Example:**
```java
// Notice how this method accepts ANY class that implements Storable
public <T extends Storable> void saveItems(String filename, ArrayList<T> items) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + filename))) {
        for (T item : items) {
            // Polymorphism in action: Java knows exactly which toStorageString() to call!
            writer.println(item.toStorageString());
        }
    } catch (IOException e) {
        System.err.println("Error saving: " + e.getMessage());
    }
}
```

## 14. Swing Event Handling Examples
**Lambda Expression for Button Clicks:**
```java
JButton btnCalc = new JButton("Calculate Grade");

// Action listener triggered when button is clicked
btnCalc.addActionListener(e -> {
    try {
        // Instantiate OOP model
        LabSubject ls = new LabSubject(txtName.getText(), 1, 
                        Double.parseDouble(txtAssignMarks.getText()), ...);
        // Display result in dialog
        JOptionPane.showMessageDialog(this, "Grade: " + ls.calculateGrade());
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
    }
});
```

## 15. Viva Explanation Notes
- **Why did you use Text Files instead of a Database?** 
  *"To ensure the app is 100% offline, requires zero configuration, and to demonstrate my understanding of core Java File I/O concepts without relying on external libraries."*
- **Explain Abstraction in your GPA Calculator.** 
  *"I created an abstract `Subject` class. You can't calculate a final percentage for a generic 'Subject' because theory and lab subjects have different formulas. So, the `calculateTotalPercentage()` method is left abstract, forcing `TheorySubject` and `LabSubject` to provide their specific implementations."*
- **Why did you use JTabbedPane?**
  *"It keeps the User Interface clean and modular. It allowed me to separate the logic into distinct `JPanel` classes (like `GPAPanel` and `TaskPanel`), which adheres to the Single Responsibility Principle."*

## 16. Future Improvements
- Implement charting libraries (like JFreeChart) to visualize GPA trends over multiple semesters.
- Add an automated file backup system that zips the `StudentData/` folder to a USB drive automatically on exit.
- Implement a search and filter function inside the `JTable`s for faster data retrieval as lists grow large.
