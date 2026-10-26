# Student Management System Using Java AWT

## Requirements

- JDK 8 or newer. JDK 17 or 21 is recommended.
- No external libraries are required.
- The program uses only `java.awt.*` and standard Java file handling.

## Structure

- `Main.java`: starts the AWT application.
- `Student.java`: encapsulated student model and marks.
- `StudentManager.java`: add, search, update, delete, and save operations.
- `FileStorage.java`: Base64-encoded, tab-separated local persistence.
- `ResultCalculator.java`: total, percentage, grade, and pass/fail rules.
- `StudentManagementGUI.java`: Frame, cards, forms, menus, dialogs, and event handling.
- Sample student data is intentionally not included because this repository is public.
- `PROJECT_REPORT.md`: short project report and viva-ready explanation.

## Compile and run from `project`

Windows PowerShell:

```powershell
$sources = Get-ChildItem .\studentmanagement -Filter '*.java' | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d .\build $sources
java -cp .\build studentmanagement.Main
```

Windows Command Prompt:

```bat
javac -encoding UTF-8 -d build studentmanagement\*.java
java -cp build studentmanagement.Main
```

The data file is created automatically at `project/data/student-records.dat`. Existing records are loaded at startup. The application writes a temporary file and replaces the old file only after the new file has been written.

## AWT components used

`Frame` is the main window. `Panel` and `BorderLayout`, `GridLayout`, `FlowLayout`, and `CardLayout` organize the screens. `Label`, `TextField`, `TextArea`, `Button`, `Choice`, `CheckboxGroup`, and `List` provide the interface. `MenuBar`, `Menu`, and `MenuItem` provide commands. `Dialog` handles warnings and confirmations. `ScrollPane` contains the registration form. `ActionListener`, `ItemListener`, and `WindowAdapter` handle events.

## Grade rules

Grades use the percentage: A for 90-100, B for 80-89, C for 70-79, D for 60-69, and F below 60. A student passes only when every subject is at least 40 and the overall percentage is at least 40.
