package studentmanagement;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** Main AWT view. All controls use java.awt components; no Swing is required. */
public class StudentManagementGUI extends Frame implements ActionListener {
    private final StudentManager manager = new StudentManager(new FileStorage(new java.io.File("data/student-records.dat")));
    private final CardLayout cards = new CardLayout();
    private final Panel cardPanel = new Panel(cards);
    private final Label status = new Label("Ready");
    private final Label totalValue = new Label("0");
    private final Label marksValue = new Label("0");

    private final TextField nameField = new TextField();
    private final TextField rollField = new TextField();
    private final Choice branchChoice = choice("CSE", "CSM", "ECE", "EEE", "IT");
    private final Choice semesterChoice = choice("1", "2", "3", "4", "5", "6", "7", "8");
    private final CheckboxGroup genderGroup = new CheckboxGroup();
    private final Checkbox maleBox = new Checkbox("Male", genderGroup, true);
    private final Checkbox femaleBox = new Checkbox("Female", genderGroup, false);
    private final Checkbox otherBox = new Checkbox("Other", genderGroup, false);
    private final TextField phoneField = new TextField();
    private final TextField emailField = new TextField();
    private final TextArea addressField = new TextArea(3, 35);
    private final Button submitButton = new Button("Register Student");
    private final Button clearButton = new Button("Clear");
    private String editingRoll;

    private final TextField searchField = new TextField(20);
    private final List studentList = new List(14, false);
    private final TextArea detailArea = new TextArea("Select a student to view complete details.", 10, 45, TextArea.SCROLLBARS_VERTICAL_ONLY);
    private final Button updateButton = new Button("Edit Selected");
    private final Button deleteButton = new Button("Delete Selected");

    private final Choice resultStudentChoice = new Choice();
    private final TextField[] markFields = { new TextField(), new TextField(), new TextField(), new TextField(), new TextField() };
    private final TextArea resultArea = new TextArea("Enter five marks and click Calculate Result.", 8, 45, TextArea.SCROLLBARS_VERTICAL_ONLY);

    public StudentManagementGUI() {
        super("Student Management System - Java AWT");
        setSize(1000, 700);
        setMinimumSize(new Dimension(820, 580));
        setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(8, 8));
        setMenuBar(createMenuBar());
        add(createHeader(), BorderLayout.NORTH);
        cardPanel.add(createDashboard(), "dashboard");
        cardPanel.add(createRegistration(), "add");
        cardPanel.add(createManage(), "manage");
        cardPanel.add(createResults(), "results");
        cardPanel.add(createAbout(), "about");
        add(cardPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
        refreshDashboard();
        refreshStudents("");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) { exitApplication(); }
        });
    }

    private MenuBar createMenuBar() {
        MenuBar bar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save Records"); save.addActionListener(this); file.add(save);
        MenuItem exit = new MenuItem("Exit"); exit.addActionListener(this); file.add(exit);
        Menu actions = new Menu("Actions");
        String[] actionNames = { "Dashboard", "Add Student", "Manage Students", "Results" };
        for (String name : actionNames) { MenuItem item = new MenuItem(name); item.addActionListener(this); actions.add(item); }
        Menu help = new Menu("Help"); MenuItem about = new MenuItem("About"); about.addActionListener(this); help.add(about);
        bar.add(file); bar.add(actions); bar.add(help); return bar;
    }

    private Panel createHeader() {
        Panel header = new Panel(new BorderLayout()); header.setBackground(new Color(31, 61, 92));
        Label title = new Label("  STUDENT MANAGEMENT SYSTEM", Label.LEFT); title.setForeground(Color.WHITE); title.setFont(new Font("SansSerif", Font.BOLD, 22)); header.add(title, BorderLayout.CENTER);
        Label subtitle = new Label("Java AWT  ", Label.RIGHT); subtitle.setForeground(new Color(220, 230, 240)); header.add(subtitle, BorderLayout.EAST); return header;
    }

    private Panel createStatusBar() { Panel panel = new Panel(new BorderLayout()); status.setBackground(new Color(225, 232, 239)); panel.add(status, BorderLayout.CENTER); return panel; }

    private Panel createDashboard() {
        Panel page = new Panel(new BorderLayout(15, 15)); Panel intro = new Panel(new GridLayout(2, 1));
        Label heading = new Label("Dashboard", Label.LEFT); heading.setFont(new Font("SansSerif", Font.BOLD, 24)); intro.add(heading); intro.add(new Label("Register, maintain, and calculate student academic results.")); page.add(intro, BorderLayout.NORTH);
        Panel stats = new Panel(new GridLayout(1, 2, 20, 20)); stats.add(statPanel("Total Registered Students", totalValue)); stats.add(statPanel("Students With Marks", marksValue)); page.add(stats, BorderLayout.CENTER);
        Panel navigation = new Panel(new FlowLayout(FlowLayout.CENTER, 12, 12)); navigation.add(navButton("Add Student", "add")); navigation.add(navButton("Manage Students", "manage")); navigation.add(navButton("Results", "results")); navigation.add(navButton("About", "about")); page.add(navigation, BorderLayout.SOUTH); return page;
    }

    private Panel statPanel(String title, Label value) { Panel panel = new Panel(new GridLayout(2, 1)); panel.setBackground(Color.WHITE); Label label = new Label(title, Label.CENTER); label.setFont(new Font("SansSerif", Font.BOLD, 14)); value.setAlignment(Label.CENTER); value.setFont(new Font("SansSerif", Font.BOLD, 34)); value.setForeground(new Color(31, 95, 145)); panel.add(label); panel.add(value); return panel; }
    private Button navButton(String text, final String card) { Button button = new Button(text); button.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { showCard(card); } }); return button; }

    private Panel createRegistration() {
        Panel page = new Panel(new BorderLayout(8, 8)); Label heading = new Label("Student Registration", Label.LEFT); heading.setFont(new Font("SansSerif", Font.BOLD, 22)); page.add(heading, BorderLayout.NORTH);
        Panel form = new Panel(new GridLayout(9, 2, 8, 8)); form.add(new Label("Full name *")); form.add(nameField); form.add(new Label("Roll number *")); form.add(rollField); form.add(new Label("Branch *")); form.add(branchChoice); form.add(new Label("Semester *")); form.add(semesterChoice);
        form.add(new Label("Gender *")); Panel genders = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 0)); genders.add(maleBox); genders.add(femaleBox); genders.add(otherBox); form.add(genders); form.add(new Label("Phone number *")); form.add(phoneField); form.add(new Label("Email *")); form.add(emailField); form.add(new Label("Address *")); form.add(addressField);
        form.add(new Label("* Required fields")); Panel buttons = new Panel(new FlowLayout(FlowLayout.LEFT)); buttons.add(submitButton); buttons.add(clearButton); form.add(buttons); submitButton.addActionListener(this); clearButton.addActionListener(this); ScrollPane scroll = new ScrollPane(); scroll.add(form); page.add(scroll, BorderLayout.CENTER); return page;
    }

    private Panel createManage() {
        Panel page = new Panel(new BorderLayout(8, 8)); Label heading = new Label("Manage Students", Label.LEFT); heading.setFont(new Font("SansSerif", Font.BOLD, 22)); page.add(heading, BorderLayout.NORTH);
        Panel top = new Panel(new FlowLayout(FlowLayout.LEFT)); top.add(new Label("Search name or roll number:")); top.add(searchField); Button search = new Button("Search"); search.addActionListener(this); top.add(search); Button refresh = new Button("Refresh"); refresh.addActionListener(this); top.add(refresh);
        Panel center = new Panel(new GridLayout(1, 2, 10, 0)); center.add(studentList); detailArea.setEditable(false); center.add(detailArea); page.add(center, BorderLayout.CENTER);
        Panel bottom = new Panel(new FlowLayout(FlowLayout.RIGHT)); updateButton.addActionListener(this); deleteButton.addActionListener(this); bottom.add(updateButton); bottom.add(deleteButton);
        Panel controls = new Panel(new BorderLayout()); controls.add(top, BorderLayout.NORTH); controls.add(bottom, BorderLayout.SOUTH); page.add(controls, BorderLayout.SOUTH);
        studentList.addItemListener(new ItemListener() { public void itemStateChanged(ItemEvent event) { showSelectedDetails(); } }); return page;
    }

    private Panel createResults() {
        Panel page = new Panel(new BorderLayout(8, 8)); Label heading = new Label("Academic Results", Label.LEFT); heading.setFont(new Font("SansSerif", Font.BOLD, 22)); page.add(heading, BorderLayout.NORTH);
        Panel input = new Panel(new GridLayout(6, 2, 8, 8)); input.add(new Label("Student")); input.add(resultStudentChoice); for (int i = 0; i < markFields.length; i++) { input.add(new Label("Subject " + (i + 1) + " mark (0-100)")); input.add(markFields[i]); }
        Panel actions = new Panel(new FlowLayout(FlowLayout.LEFT)); Button calculate = new Button("Calculate Result"); calculate.addActionListener(this); Button clear = new Button("Clear Marks"); clear.addActionListener(this); actions.add(calculate); actions.add(clear); Panel north = new Panel(new BorderLayout()); north.add(input, BorderLayout.CENTER); north.add(actions, BorderLayout.SOUTH); page.add(north, BorderLayout.NORTH); resultArea.setEditable(false); page.add(resultArea, BorderLayout.CENTER);
        Panel note = new Panel(new FlowLayout(FlowLayout.LEFT)); note.add(new Label("Grades: A >= 90, B >= 80, C >= 70, D >= 60, F below 60. Pass threshold: " + ResultCalculator.PASSING_THRESHOLD + " per subject.")); page.add(note, BorderLayout.SOUTH); return page;
    }

    private Panel createAbout() {
        Panel page = new Panel(new BorderLayout(10, 10)); Label heading = new Label("About This Project", Label.LEFT); heading.setFont(new Font("SansSerif", Font.BOLD, 22)); page.add(heading, BorderLayout.NORTH);
        TextArea text = new TextArea("Student Management System\n\nBuilt using Java AWT only.\n\nThe application stores records in data/student-records.dat. Fields are encoded safely before storage, and damaged individual records are skipped while loading.\n\nFeatures: registration and validation, search, update, delete, five-subject marks, percentage, grade, pass/fail result, and persistence.\n\nThis project demonstrates Frame, Panel, Label, TextField, TextArea, Choice, CheckboxGroup, List, MenuBar, Dialog, ScrollPane, CardLayout, GridLayout, FlowLayout, ActionListener, ItemListener, and WindowAdapter.", 15, 60, TextArea.SCROLLBARS_VERTICAL_ONLY); text.setEditable(false); page.add(text, BorderLayout.CENTER); return page;
    }

    private void showCard(String name) { if ("results".equals(name)) refreshResultStudents(); cards.show(cardPanel, name); }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if ("Register Student".equals(command) || "Save Changes".equals(command)) registerStudent(); else if ("Clear".equals(command)) { clearForm(); clearMarks(); }
        else if ("Search".equals(command)) refreshStudents(searchField.getText()); else if ("Refresh".equals(command)) refreshStudents(""); else if ("Edit Selected".equals(command)) editSelected(); else if ("Delete Selected".equals(command)) deleteSelected();
        else if ("Calculate Result".equals(command)) calculateResult(); else if ("Clear Marks".equals(command)) clearMarks(); else if ("Save Records".equals(command)) { manager.save(); setStatus("Records saved successfully."); }
        else if ("Exit".equals(command)) exitApplication(); else if ("About".equals(command)) showCard("about"); else if ("Dashboard".equals(command)) showCard("dashboard");
        else if ("Add Student".equals(command)) { clearForm(); showCard("add"); } else if ("Manage Students".equals(command)) { refreshStudents(""); showCard("manage"); } else if ("Results".equals(command)) showCard("results");
    }

    private void registerStudent() {
        try { Student student = readForm(); if (editingRoll == null) { manager.add(student); setStatus("Student registered successfully."); } else { manager.update(student); setStatus("Student updated successfully."); } clearForm(); refreshDashboard(); refreshStudents(""); refreshResultStudents(); showCard("manage"); }
        catch (IllegalArgumentException exception) { showMessage("Validation error", exception.getMessage()); } catch (RuntimeException exception) { showMessage("Save error", exception.getMessage()); }
    }

    private Student readForm() {
        String name = nameField.getText().trim(), roll = rollField.getText().trim(), phone = phoneField.getText().trim(), email = emailField.getText().trim(), address = addressField.getText().trim();
        if (name.length() == 0 || roll.length() == 0 || phone.length() == 0 || email.length() == 0 || address.length() == 0) throw new IllegalArgumentException("Please fill every required field.");
        if (!phone.matches("[0-9]{7,15}")) throw new IllegalArgumentException("Phone must contain 7 to 15 digits only."); if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) throw new IllegalArgumentException("Enter a valid email address.");
        String gender = genderGroup.getSelectedCheckbox().getLabel(); Student student = new Student(name, roll, branchChoice.getSelectedItem(), semesterChoice.getSelectedItem(), gender, phone, email, address);
        if (editingRoll != null) { Student old = manager.findByRoll(editingRoll); if (old != null && old.hasMarks()) student.setMarks(old.getMarks()); } return student;
    }

    private void clearForm() { editingRoll = null; nameField.setText(""); rollField.setText(""); rollField.setEditable(true); phoneField.setText(""); emailField.setText(""); addressField.setText(""); submitButton.setLabel("Register Student"); }
    private void refreshDashboard() { int marked = 0; for (Student student : manager.getAll()) if (student.hasMarks()) marked++; totalValue.setText(Integer.toString(manager.getAll().size())); marksValue.setText(Integer.toString(marked)); }
    private void refreshStudents(String query) { studentList.removeAll(); for (Student student : manager.search(query)) studentList.add(student.displayLabel()); detailArea.setText(studentList.getItemCount() == 0 ? "No matching student found." : "Select a student to view complete details."); }
    private void showSelectedDetails() { Student student = selectedStudent(); if (student == null) return; detailArea.setText("Name: " + student.getFullName() + "\nRoll number: " + student.getRollNumber() + "\nBranch: " + student.getBranch() + "\nSemester: " + student.getSemester() + "\nGender: " + student.getGender() + "\nPhone: " + student.getPhone() + "\nEmail: " + student.getEmail() + "\nAddress: " + student.getAddress() + "\nMarks recorded: " + (student.hasMarks() ? "Yes" : "No")); }
    private Student selectedStudent() { int index = studentList.getSelectedIndex(); if (index < 0) return null; String item = studentList.getItem(index); return manager.findByRoll(item.substring(0, item.indexOf(" - "))); }
    private void editSelected() { Student student = selectedStudent(); if (student == null) { showMessage("Selection", "Select a student first."); return; } editingRoll = student.getRollNumber(); populateForm(student); submitButton.setLabel("Save Changes"); rollField.setEditable(false); showCard("add"); }
    private void populateForm(Student student) { nameField.setText(student.getFullName()); rollField.setText(student.getRollNumber()); branchChoice.select(student.getBranch()); semesterChoice.select(student.getSemester()); for (Checkbox box : new Checkbox[] { maleBox, femaleBox, otherBox }) box.setState(box.getLabel().equals(student.getGender())); phoneField.setText(student.getPhone()); emailField.setText(student.getEmail()); addressField.setText(student.getAddress()); }
    private void deleteSelected() { Student student = selectedStudent(); if (student == null) { showMessage("Selection", "Select a student first."); return; } if (confirm("Confirm deletion", "Delete " + student.getFullName() + " (" + student.getRollNumber() + ")?")) { manager.delete(student.getRollNumber()); refreshStudents(searchField.getText()); refreshDashboard(); refreshResultStudents(); setStatus("Student deleted successfully."); } }
    private void refreshResultStudents() { String selected = resultStudentChoice.getSelectedItem(); resultStudentChoice.removeAll(); for (Student student : manager.getAll()) resultStudentChoice.add(student.displayLabel()); if (selected != null) resultStudentChoice.select(selected); }
    private void calculateResult() { try { Student student = findResultStudent(); if (student == null) throw new IllegalArgumentException("Register a student before entering marks."); int[] marks = new int[Student.SUBJECT_COUNT]; for (int i = 0; i < marks.length; i++) { if (markFields[i].getText().trim().length() == 0) throw new IllegalArgumentException("Enter all five marks."); marks[i] = Integer.parseInt(markFields[i].getText().trim()); } student.setMarks(marks); manager.save(); ResultCalculator.Result result = ResultCalculator.calculate(marks); resultArea.setText("Student: " + student.getFullName() + "\nRoll number: " + student.getRollNumber() + "\nMarks: " + joinMarks(marks) + "\nTotal: " + result.getTotal() + " / 500\nPercentage: " + String.format("%.2f", result.getPercentage()) + "%\nGrade: " + result.getGrade() + "\nResult: " + (result.isPassed() ? "PASS" : "FAIL")); refreshDashboard(); setStatus("Result calculated and saved."); } catch (NumberFormatException exception) { showMessage("Marks error", "Marks must be whole numbers from 0 to 100."); } catch (IllegalArgumentException exception) { showMessage("Marks error", exception.getMessage()); } }
    private Student findResultStudent() { String item = resultStudentChoice.getSelectedItem(); return item == null ? null : manager.findByRoll(item.substring(0, item.indexOf(" - "))); }
    private String joinMarks(int[] marks) { StringBuilder text = new StringBuilder(); for (int i = 0; i < marks.length; i++) { if (i > 0) text.append(", "); text.append(marks[i]); } return text.toString(); }
    private void clearMarks() { for (TextField field : markFields) field.setText(""); resultArea.setText("Enter five marks and click Calculate Result."); }
    private void setStatus(String message) { status.setText("  " + message); }
    private void showMessage(String title, String message) { final Dialog dialog = new Dialog(this, title, true); dialog.setLayout(new BorderLayout(10, 10)); dialog.add(new Label("  " + message), BorderLayout.CENTER); Button ok = new Button("OK"); ok.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { dialog.dispose(); } }); Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT)); buttons.add(ok); dialog.add(buttons, BorderLayout.SOUTH); dialog.setSize(430, 140); dialog.setLocation(getX() + 250, getY() + 200); dialog.setVisible(true); }
    private boolean confirm(String title, String message) { final Dialog dialog = new Dialog(this, title, true); final boolean[] answer = { false }; dialog.setLayout(new BorderLayout(10, 10)); dialog.add(new Label("  " + message), BorderLayout.CENTER); Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT)); Button yes = new Button("Yes"); Button no = new Button("No"); yes.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { answer[0] = true; dialog.dispose(); } }); no.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { dialog.dispose(); } }); buttons.add(yes); buttons.add(no); dialog.add(buttons, BorderLayout.SOUTH); dialog.setSize(450, 140); dialog.setLocation(getX() + 250, getY() + 200); dialog.setVisible(true); return answer[0]; }
    private void exitApplication() { if (confirm("Exit", "Save records and close the application?")) { manager.save(); dispose(); System.exit(0); } }
    private Choice choice(String... values) { Choice choice = new Choice(); for (String value : values) choice.add(value); return choice; }
}
