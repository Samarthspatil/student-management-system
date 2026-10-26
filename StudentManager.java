package studentmanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Owns in-memory CRUD operations and delegates persistence to FileStorage. */
public class StudentManager {
    private final List<Student> students = new ArrayList<Student>();
    private final FileStorage storage;

    public StudentManager(FileStorage storage) {
        this.storage = storage;
        students.addAll(storage.load());
    }

    public List<Student> getAll() {
        return Collections.unmodifiableList(students);
    }

    public Student findByRoll(String rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber().equalsIgnoreCase(rollNumber.trim())) return student;
        }
        return null;
    }

    public List<Student> search(String query) {
        List<Student> matches = new ArrayList<Student>();
        String text = query == null ? "" : query.trim().toLowerCase();
        for (Student student : students) {
            if (text.length() == 0 || student.getRollNumber().toLowerCase().contains(text)
                    || student.getFullName().toLowerCase().contains(text)) matches.add(student);
        }
        return matches;
    }

    public void add(Student student) {
        if (findByRoll(student.getRollNumber()) != null) {
            throw new IllegalArgumentException("Roll number already exists.");
        }
        students.add(student);
        save();
    }

    public void update(Student student) {
        Student existing = findByRoll(student.getRollNumber());
        if (existing == null) throw new IllegalArgumentException("Student record was not found.");
        int index = students.indexOf(existing);
        students.set(index, student);
        save();
    }

    public void delete(String rollNumber) {
        Student student = findByRoll(rollNumber);
        if (student == null) throw new IllegalArgumentException("Student record was not found.");
        students.remove(student);
        save();
    }

    public void save() { storage.save(students); }
}
