package studentmanagement;

/** Pure calculation class: grades are A >= 90, B >= 80, C >= 70, D >= 60, F below 60. */
public final class ResultCalculator {
    public static final int PASSING_THRESHOLD = 40;
    private ResultCalculator() { }

    public static Result calculate(int[] marks) {
        if (marks == null || marks.length != Student.SUBJECT_COUNT) throw new IllegalArgumentException("Five marks are required.");
        int total = 0;
        boolean passed = true;
        for (int mark : marks) {
            if (mark < 0 || mark > 100) throw new IllegalArgumentException("Every mark must be between 0 and 100.");
            total += mark;
            if (mark < PASSING_THRESHOLD) passed = false;
        }
        double percentage = total / (double) (Student.SUBJECT_COUNT * 100) * 100.0;
        String grade = percentage >= 90 ? "A" : percentage >= 80 ? "B" : percentage >= 70 ? "C" : percentage >= 60 ? "D" : "F";
        return new Result(total, percentage, grade, passed && percentage >= PASSING_THRESHOLD);
    }

    public static final class Result {
        private final int total;
        private final double percentage;
        private final String grade;
        private final boolean passed;
        private Result(int total, double percentage, String grade, boolean passed) {
            this.total = total; this.percentage = percentage; this.grade = grade; this.passed = passed;
        }
        public int getTotal() { return total; }
        public double getPercentage() { return percentage; }
        public String getGrade() { return grade; }
        public boolean isPassed() { return passed; }
    }
}
