# Project Report: Student Management System Using Java AWT

## Problem statement
College staff need a simple local application to register students, maintain academic information, and calculate results without relying on a web server or external database.

## Objectives

1. Register students with validated personal and academic details.
2. Search, view, update, and delete records.
3. Record five subject marks and calculate total, percentage, grade, and result.
4. Preserve records between program runs using local file storage.

## Features

The application provides a dashboard, registration form, unique roll-number validation, name/roll search, complete record display, edit and delete confirmation, result entry, configurable constant passing threshold, menu commands, status messages, and graceful handling of missing or damaged files.

## Technology and implementation

The project is written in Java and uses AWT components only. `Student` is the model, `StudentManager` owns CRUD operations, `FileStorage` persists records, `ResultCalculator` contains pure academic rules, and `StudentManagementGUI` assembles the event-driven desktop interface. `Main` starts the application on the AWT event queue.

The storage format has a header and Base64-encoded text fields separated by tabs. This avoids data corruption from commas, tabs, or punctuation inside user text. A temporary file is written before replacement, and malformed individual records are skipped instead of crashing startup.

## Limitations

This is a local single-user application. It does not provide login/security, multi-user synchronization, database transactions, or export to PDF. Results are limited to five subjects because that is the defined first-year demonstration scope.

## Conclusion

The project demonstrates practical object-oriented Java, AWT layout management, event listeners, validation, exception handling, persistence, and separation of concerns in a beginner-friendly desktop application.

## Test checklist

- [ ] Start with no data file; confirm the application opens.
- [ ] Register a valid student and confirm the dashboard count increases.
- [ ] Submit blank fields; confirm a validation dialog appears.
- [ ] Enter letters or too few digits in phone; confirm rejection.
- [ ] Enter an invalid email; confirm rejection.
- [ ] Register the same roll number twice; confirm rejection.
- [ ] Search by full/partial name and roll number.
- [ ] Select a record and verify all details.
- [ ] Edit the name, phone, or address; confirm the change persists.
- [ ] Delete a record; confirm the dialog and list refresh.
- [ ] Enter marks 0-100 and verify total, percentage, grade, and PASS/FAIL.
- [ ] Enter a mark below 0 or above 100; confirm rejection.
- [ ] Leave a mark empty; confirm the application does not crash.
- [ ] Close and reopen; verify records and marks remain.
- [ ] Add punctuation and commas to address; verify it reloads correctly.
- [ ] Add a damaged line to a copy of the data file; confirm other records still load.

## Common errors

- `javac is not recognized`: install a JDK and add its `bin` directory to PATH, then restart VS Code.
- `Could not find or load main class`: run `java -cp build studentmanagement.Main` from the `project` directory after compiling.
- AWT window cannot open in a remote/headless terminal: run it on a normal desktop session; compilation and non-GUI classes still work headlessly.
- Duplicate roll number: choose a new roll number; roll numbers intentionally identify records uniquely.
