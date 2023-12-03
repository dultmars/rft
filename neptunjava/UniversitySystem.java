import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UniversitySystem {
    private List<Course> courses;
    private List<Exam> exams;
    private List<User> users;

    public UniversitySystem() {
        this.courses = new ArrayList<>();
        this.exams = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public User authenticateUser (String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void createExam(Exam exam) {

        exams.add(exam);
    }

    public void registerUser(User user) {
        users.add(user);
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public List<User> getUsers() {
        return users;
    }
    public boolean enrollStudentInCourse(User student, Course course) {
        // Ellenőrizzük, hogy a felhasználó diák és a kurzus valóban létezik
        if ("Student".equals(student.getRole()) && courses.contains(course)) {
            // TODO: A valóságban itt hozzáadnánk a diákot a kurzushoz
            System.out.println(student.getUsername() + " enrolled in " + course.getCourseName());
            return true;
        }
        return false;
    }
    public boolean createExamForCourse(User teacher, Course course, String examName, String dateStr, String location) {
        if ("Teacher".equals(teacher.getRole()) && courses.contains(course)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date = dateFormat.parse(dateStr);
                Exam exam = new Exam(exams.size() + 1, examName, dateStr);
                exams.add(exam);
                System.out.println("Exam created for " + course.getCourseName() + ": " + examName);
                return true;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Use 'yyyy-MM-dd HH:mm'.");
            }
        }
        return false;
    }

    public boolean registerForExam(User student, Exam exam) {
        if ("Student".equals(student.getRole()) && exams.contains(exam)) {
            // TODO: A valóságban itt hozzáadnánk a diákot a vizsgához
            System.out.println(student.getUsername() + " registered for " + exam.getExamName());
            return true;
        }
        return false;
    }


    public boolean createCourse(User teacher, String courseName) {
        if ("teacher".equals(teacher.getRole())) {
            Course course = new Course(courses.size() + 1, courseName);
            courses.add(course);
           // System.out.println("Course created: " + courseName);
            return true;
        }
        System.out.println("Course creation failed. Only teachers can create courses.");
        return false;
    }



}

