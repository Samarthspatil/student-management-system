package studentmanagement;

import java.util.Arrays;

/** Represents one registered student and optional marks for five subjects. */
public class Student {
    public static final int SUBJECT_COUNT = 5;
    private String fullName;
    private String rollNumber;
    private String branch;
    private String semester;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private final int[] marks;

    public Student(String fullName, String rollNumber, String branch, String semester,
                   String gender, String phone, String email, String address) {
        this.fullName = fullName;
        this.rollNumber = rollNumber;
        this.branch = branch;
        this.semester = semester;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.marks = new int[SUBJECT_COUNT];
        Arrays.fill(this.marks, -1);
    }

    public String getFullName() { return fullName; }
    public String getRollNumber() { return rollNumber; }
    public String getBranch() { return branch; }
    public String getSemester() { return semester; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public int[] getMarks() { return Arrays.copyOf(marks, marks.length); }

    public void setFullName(String value) { fullName = value; }
    public void setBranch(String value) { branch = value; }
    public void setSemester(String value) { semester = value; }
    public void setGender(String value) { gender = value; }
    public void setPhone(String value) { phone = value; }
    public void setEmail(String value) { email = value; }
    public void setAddress(String value) { address = value; }

    public void setMarks(int[] values) {
        if (values == null || values.length != SUBJECT_COUNT) {
            throw new IllegalArgumentException("Exactly five marks are required.");
        }
        for (int mark : values) {
            if (mark < 0 || mark > 100) {
                throw new IllegalArgumentException("Marks must be between 0 and 100.");
            }
        }
        System.arraycopy(values, 0, marks, 0, SUBJECT_COUNT);
    }

    public boolean hasMarks() {
        for (int mark : marks) {
            if (mark >= 0) return true;
        }
        return false;
    }

    public String displayLabel() {
        return rollNumber + " - " + fullName;
    }
}
