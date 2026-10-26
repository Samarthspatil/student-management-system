package studentmanagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/** Saves one Base64-encoded field per tab-separated column. */
public class FileStorage {
    private static final String HEADER = "SMS-AWT-1";
    private final File file;

    public FileStorage(File file) { this.file = file; }
    public File getFile() { return file; }

    public List<Student> load() {
        List<Student> result = new ArrayList<Student>();
        if (!file.exists()) return result;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) { first = false; if (HEADER.equals(line)) continue; }
                try {
                    String[] parts = line.split("\\t", -1);
                    if (parts.length != 13) continue;
                    Student student = new Student(decode(parts[0]), decode(parts[1]), decode(parts[2]),
                            decode(parts[3]), decode(parts[4]), decode(parts[5]), decode(parts[6]), decode(parts[7]));
                    int[] marks = new int[Student.SUBJECT_COUNT];
                    for (int i = 0; i < marks.length; i++) marks[i] = Integer.parseInt(parts[8 + i]);
                    boolean allMissing = true;
                    for (int mark : marks) if (mark >= 0) allMissing = false;
                    if (!allMissing) student.setMarks(marks);
                    result.add(student);
                } catch (RuntimeException ignored) {
                    // Ignore one damaged record and continue loading usable records.
                }
            }
        } catch (IOException ignored) {
            // The GUI reports the empty/partially loaded state without crashing.
        }
        return result;
    }

    public void save(List<Student> students) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
        File temporary = new File(file.getPath() + ".tmp");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(temporary))) {
            writer.write(HEADER);
            writer.newLine();
            for (Student student : students) {
                writer.write(encode(student.getFullName())); writer.write('\t');
                writer.write(encode(student.getRollNumber())); writer.write('\t');
                writer.write(encode(student.getBranch())); writer.write('\t');
                writer.write(encode(student.getSemester())); writer.write('\t');
                writer.write(encode(student.getGender())); writer.write('\t');
                writer.write(encode(student.getPhone())); writer.write('\t');
                writer.write(encode(student.getEmail())); writer.write('\t');
                writer.write(encode(student.getAddress()));
                for (int mark : student.getMarks()) { writer.write('\t'); writer.write(Integer.toString(mark)); }
                writer.newLine();
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not save records: " + exception.getMessage(), exception);
        }
        if (file.exists() && !file.delete()) throw new IllegalStateException("Could not replace the records file.");
        if (!temporary.renameTo(file)) throw new IllegalStateException("Could not finalize the records file.");
    }

    private String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
    private String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
