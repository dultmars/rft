import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UniversitySystemConsoleUI {
    private UniversitySystem universitySystem;
    private Scanner scanner;
    private User currentUser;
    private String userFile;
    private String courseFile;
    private String examFile;
    private List<Exam> exams;  // Lista a vizsgák tárolásához
    private List<Course> courses;  // Lista a kurzusok tárolásához
    public UniversitySystemConsoleUI(UniversitySystem universitySystem, String userFile, String courseFile, String examFile) {
        this.universitySystem = universitySystem;
        this.scanner = new Scanner(System.in);
        //this.currentUser = null;
        // A fájlnevek tárolása
        this.userFile = userFile;
        this.courseFile = courseFile;
        this.examFile = examFile;
        this.exams = new ArrayList<>();  // Inicializáljuk a vizsga listát
        this.courses = new ArrayList<>();  // Inicializáljuk a kurzus listát
        // Felhasználók, kurzusok és vizsgák beolvasása a fájlokból
        loadUsersFromFile();
        loadCoursesFromFile();
        loadExamsFromFile();
    }
    private void loadUsersFromFile() {
        loadFromTxtFile(userFile, "user");
    }

    private void loadCoursesFromFile() {
        loadFromTxtFile(courseFile, "course");
    }

    private void loadExamsFromFile() {
        loadFromTxtFile(examFile, "exam");
    }

    private void loadFromTxtFile(String filename, String type) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (type.equals("user") && parts.length == 4) {
                    String username = parts[1].trim();
                    String password = parts[2].trim();
                    String role = parts[3].trim();
                    universitySystem.registerUser(new User(Integer.parseInt(parts[0].trim()), username, password, role));
                } else if (type.equals("course") && parts.length == 2) {
                    universitySystem.addCourse(new Course(Integer.parseInt(parts[0].trim()), parts[1].trim()));
                } else if (type.equals("exam") && parts.length == 4) {
                    String examDateStr = parts[2].trim();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    Date examDate = dateFormat.parse(examDateStr);

                    // Vizsga betöltése és hozzáadása a listához
                    exams.add(new Exam(
                            Integer.parseInt(parts[0].trim()),
                            parts[1].trim(),
                            parts[2].trim()
                          //  parts[3].trim()
                    ));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    public void login() {

        boolean loginSuccess = false;
        while (!loginSuccess) {
            System.out.println("***--- Login ---***");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            currentUser = universitySystem.authenticateUser(username, password);

            if (currentUser == null) {
                System.out.println("Login failed. Invalid username or password.");
            } else {
                System.out.println("Login successful. Welcome, " + currentUser.getUsername() + "!");
                loginSuccess = true;
                //displayMenu();
            }
        }
       // scanner.nextLine(); // Consumes the newline character after reading the user input
    }


    /*public void displayMenu() {
        System.out.println("\nUniversity System Menu:");
        System.out.println("1. Display Courses");
        System.out.println("2. Display Exams");
        System.out.println("3. Display Users");
        System.out.println("4. Create Course");
        System.out.println("5. Create Exam for Course");
        System.out.println("6. Register for Exam");
        System.out.println("7. Register for Course");
        System.out.println("8. Exit");
    }*/

    public void displayMenu() {
        System.out.println("\nUniversity System Menu:");

        if ("teacher".equals(currentUser.getRole())) {
            // Tanárok menüje
            System.out.println("1. Display Courses");
            System.out.println("2. Display Exams");
            System.out.println("3. Display Users");
            System.out.println("4. Create Course");
            System.out.println("5. Create Exam for Course");
            System.out.println("6. Exit");
        } else if ("student".equals(currentUser.getRole())) {
            // Diákok menüje
            System.out.println("1. Display Courses");
            System.out.println("2. Display Exams");
            System.out.println("3. Register for Exam");
            System.out.println("4. Register for Course");
            System.out.println("5. Exit");
        } else {
            // Egyéb felhasználók menüje (ha lenne ilyen lehetőség)
            System.out.println("Invalid user role. Exiting the program.");
            System.exit(0);
        }
    }

    public void run() {
        while (true) {
            login();
            if (currentUser != null) {
                boolean loggedIn = true;
                while (loggedIn) {
                    displayMenu();
                    int choice = scanner.nextInt();
                    //scanner.nextLine();

                    switch (choice) {
                        case 1:
                            displayCourses();
                            break;
                        case 2:
                            displayExams();
                            break;
                        case 3:
                            if ("teacher".equals(currentUser.getRole())) {
                                displayUsers();
                            } else if ("student".equals(currentUser.getRole())) {
                                registerForExam();
                            }
                            break;
                        case 4:
                            if ("teacher".equals(currentUser.getRole())) {
                                createCourse();
                            } else if ("student".equals(currentUser.getRole())) {
                                registerForCourse();
                            }
                            break;
                        case 5:
                            if ("teacher".equals(currentUser.getRole())) {
                                System.out.println("Enter teacher username:");
                                String teacherUsername = scanner.nextLine();

                                System.out.println("Enter course ID for the exam:");
                                int courseId = scanner.nextInt();
                                scanner.nextLine();

                                User teacher = findUserByUsername(teacherUsername);
                                Course course = findCourseById(courseId);
                                System.out.println("Enter exam name:");
                                String examName = scanner.nextLine();

                                System.out.println("Enter exam date and time (yyyyMMdd):");
                                String dateStr = scanner.nextLine();

                                System.out.println("Enter exam location:");
                                String location = scanner.nextLine();

                                createExamForCourse(teacher, course, examName, dateStr, location);
                            } else if ("student".equals(currentUser.getRole())) {
                                    System.out.println("Exit");
                                    loggedIn = false;
                                }else {
                                System.out.println("Invalid choice for this user type.");
                            }
                            break;
                        /*case 6:
                            registerForExam();
                            break;
                        case 7:
                            registerForCourse();
                            break;*/
                        case 6:
                            loggedIn = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                    }
                }
            } else {
                System.out.println("Exiting the program. Login required.");
                break;
            }
        }
    }

    private void displayCourses() {
        System.out.println("Available Courses:");
        List<Course> courses = universitySystem.getCourses();
        for (Course course : courses) {
            System.out.println(course.getCourseId() + ". " + course.getCourseName());
        }
    }

    private void displayExams() {
        System.out.println("Available Exams:");
        //List<Exam> exams = universitySystem.getExams();
        for (Exam exam : exams) {
            System.out.println(exam.getExamId() + ". " + exam.getExamName());
        }
    }

    private void displayUsers() {
        System.out.println("Registered Users:");
        List<User> users = universitySystem.getUsers();
        for (User user : users) {
            System.out.println(user.getUserId() + ". " + user.getUsername() + " - " + user.getRole());
        }
    }

    public static void main(String[] args) throws Exception{
        UniversitySystem universitySystem = new UniversitySystem();

        //UniversitySystemConsoleUI consoleUI = new UniversitySystemConsoleUI(universitySystem);
        UniversitySystemConsoleUI consoleUI = new UniversitySystemConsoleUI(universitySystem, "user.txt", "course.txt", "exam.txt");
        // Kurzusok, vizsgák, felhasználók hozzáadása és regisztráció
        /*Course mathCourse = new Course(001, "Mathematics");
        Course physicsCourse = new Course(002, "Physics");

        Exam mathExam = new Exam(001, "Math Final Exam",20231216, "Room 101");
        Exam physicsExam = new Exam(002, "Physics Final Exam", 20231208, "Room 102");

        User teacher1 = new User(001, "teacher1", "Teacher1", "teacher");
        User teacher2 = new User(002, "teacher2", "Teacher2", "teacher");
        User teacher3 = new User(003, "teacher3", "Teacher3", "teacher");
        User student1 = new User(101, "student1", "Student1", "student");
        User student2 = new User(102, "student2", "Student2", "student");
        User student3 = new User(103, "student3", "Student3", "student");

        universitySystem.addCourse(mathCourse);
        universitySystem.addCourse(physicsCourse);
        universitySystem.createExam(mathExam);
        universitySystem.createExam(physicsExam);
        universitySystem.registerUser(teacher1);
        universitySystem.registerUser(student1);
        universitySystem.registerUser(teacher2);
        universitySystem.registerUser(student2);
        universitySystem.registerUser(teacher3);
        universitySystem.registerUser(student3); */

        consoleUI.run();

    }

    private void enrollStudentInCourse() {
        System.out.println("Enter student username:");
        String studentUsername = scanner.nextLine();

        System.out.println("Enter course ID to enroll in:");
        int courseId = scanner.nextInt();
        scanner.nextLine(); // Consumes the newline character

        User student = findUserByUsername(studentUsername);
        Course course = findCourseById(courseId);

        if (student != null && course != null) {
            boolean success = universitySystem.enrollStudentInCourse(student, course);
            if (!success) {
                System.out.println("Enrollment failed. Make sure the course exists.");
            }
        } else {
            System.out.println("Enrollment failed. Make sure the user and course exist.");
        }
    }

    private User findUserByUsername(String username) {
        List<User> users = universitySystem.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private Course findCourseById(int courseId) {
        List<Course> courses = universitySystem.getCourses();
        for (Course course : courses) {
            if (course.getCourseId() == courseId) {
                return course;
            }
        }
        return null;
    }

    /*private void createExamForCourse() {
        System.out.println("Enter teacher username:");
        String teacherUsername = scanner.nextLine();

        System.out.println("Enter course ID for the exam:");
        int courseId = scanner.nextInt();
        scanner.nextLine(); // Consumes the newline character

        User teacher = findUserByUsername(teacherUsername);
        Course course = findCourseById(courseId);

        if (teacher != null && course != null) {
            System.out.println("Enter exam name:");
            String examName = scanner.nextLine();

            System.out.println("Enter exam date and time (yyyy-MM-dd HH:mm):");
            String dateStr = scanner.nextLine();

            System.out.println("Enter exam location:");
            String location = scanner.nextLine();

            boolean success = universitySystem.createExamForCourse(teacher, course, examName, dateStr, location);
            if (!success) {
                System.out.println("Exam creation failed. Make sure the user is a teacher and the course exists.");
            }
        } else {
            System.out.println("Exam creation failed. Make sure the user and course exist.");
        }
    }*/

    /*public boolean createExamForCourse(User teacher, Course course, String examName, String dateStr, String location) {
        if ("Teacher".equals(teacher.getRole()) && courses.contains(course)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date = dateFormat.parse(dateStr);

                // Vizsga létrehozása és hozzáadása a listához
                Exam exam = new Exam(exams.size() + 1, examName, date, location);
                exams.add(exam);

                System.out.println("Exam created for " + course.getCourseName() + ": " + examName);
                return true;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Use 'yyyy-MM-dd HH:mm'.");
            }
        }
        return false;
    }*/

    public boolean createExamForCourse(User teacher, Course course, String examName, String dateStr, String location) {
        if ("Teacher".equals(teacher.getRole()) && courses.contains(course)) {
            // Vizsga létrehozása és hozzáadása a listához
            exams.add(new Exam(exams.size() + 1, examName, dateStr));

            System.out.println("Exam created for " + course.getCourseName() + ": " + examName);
            return true;
        }
        return false;
    }
    private void registerForExam() {
        System.out.println("Enter student username:");
        String studentUsername = scanner.nextLine();

        // Kiírjuk a rendelkezésre álló vizsgákat
        System.out.println("Available Exams:");
        List<Exam> exams = universitySystem.getExams();
        for (Exam exam : exams) {
            System.out.println(exam.getExamId() + ". " + exam.getExamName());
        }

        System.out.println("Enter exam ID to register for:");
        int examId = scanner.nextInt();
        scanner.nextLine(); // Consumes the newline character

        User student = findUserByUsername(studentUsername);
        Exam exam = findExamById(examId);

        if (student != null && exam != null) {
            boolean success = universitySystem.registerForExam(student, exam);
            if (success) {
                // Kiírjuk a regisztráció sikeres üzenetét, beleértve a vizsga nevét is
                System.out.println(student.getUsername() + " registered for " + exam.getExamName() + " exam");
            } else {
                System.out.println("Registration failed. Make sure the user is a student and the exam exists.");
            }
        } else {
            System.out.println("Registration failed. Make sure the user and exam exist.");
        }
    }

    private Exam findExamById(int examId) {
        List<Exam> exams = universitySystem.getExams();
        for (Exam exam : exams) {
            if (exam.getExamId() == examId) {
                return exam;
            }
        }
        return null;
    }

    private void registerForCourse() {
        System.out.println("Enter student username:");
        String studentUsername = scanner.nextLine();

        System.out.println("Enter course ID to register for:");
        int courseId = scanner.nextInt();
        scanner.nextLine(); // Consumes the newline character

        User student = findUserByUsername(studentUsername);
        Course course = findCourseById(courseId);

        if (student != null && course != null) {
            boolean success = universitySystem.enrollStudentInCourse(student, course);
            if (success) {
                System.out.println(student.getUsername() + " enrolled in " + course.getCourseName());
            } else {
                System.out.println("Enrollment failed. Make sure the user is a student and the course exists.");
            }
        } else {
            System.out.println("Enrollment failed. Make sure the user and course exist.");
        }
    }

    private void createCourse() {
        if ("teacher".equals(currentUser.getRole())) {
            System.out.println("Enter course name:");
            String courseName = scanner.nextLine();

            boolean success = universitySystem.createCourse(currentUser, courseName);
            if (success) {
                System.out.println("Course created: " + courseName);
            } else {
                System.out.println("Course creation failed.");
            }
        } else {
            System.out.println("You do not have the privilege to create a course.");
        }
    }
}
