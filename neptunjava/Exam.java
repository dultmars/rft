
import java.lang.String;

public class Exam {
    private int examId;
    private String examName;
    private String dateStr;
    private String examlocation;

    public Exam(int examId, String examName, String dateStr) {
        this.examId = examId;
        this.examName = examName;
        this.dateStr = dateStr;
        this.examlocation = examlocation;
    }

    public int getExamId() {
        return examId;
    }

    public String getExamName() {
        return examName;
    }
    public String getDateStr() {
        return dateStr;
    }
    public String getExamLocation() {
        return examlocation;
    }
}
