
// package librarymanagement;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

class Book {
    String bookId;
    String title;
    String author;
    int totalCopies;
    int availableCopies;
    ArrayList<String> issuedTo = new ArrayList<>();

    Book(String bookId, String title, String author, int totalCopies, int availableCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.issuedTo = new ArrayList<>();
    }

    public String toString() {
        return "Book ID: " + bookId + ", Title: " + title + ", Author: " + author +
                ", Total Copies: " + totalCopies + ", Available: " + availableCopies;
    }

    String getbookId() {
        return bookId;
    }

    String gettitle() {
        return title;
    }

    String getauthor() {
        return author;
    }

    int gettotalCopies() {
        return totalCopies;
    }

    int getavailableCopies() {
        return availableCopies;
    }

}

class Student {
    String studentId;
    String stdName;
    ArrayList<String> issuedBooks = new ArrayList<>();

    Student(String studentId, String stdName) {
        this.studentId = studentId;
        this.stdName = stdName;
        this.issuedBooks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId + ", Name: " + stdName;
    }

    String getstudentId() {
        return studentId;
    }

    String getstdName() {
        return stdName;
    }

}

class Admin {
    String adminId;
    String admName;
    String password;

    Admin(String adminId, String admName, String password) {
        this.adminId = adminId;
        this.admName = admName;
        this.password = password;
    }

    String getadminId() {
        return adminId;
    }

    String getadmName() {
        return admName;
    }

    String getPassword() {
        return password;
    }
}

class Library {

    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Student> students = new ArrayList<>();
    ArrayList<Admin> admins = new ArrayList<>();

    Library() {

        loadAdminsFromFile();
        loadStudentsFromFile();
        loadBooksFromFile();
        if (admins.isEmpty()) {
            admins.add(new Admin("A1", "Alice", "1234"));
            admins.add(new Admin("A2", "Ram", "4567"));
        }
        if (students.isEmpty()) {
            students.add(new Student("S101", "Kanan"));
            students.add(new Student("S102", "Ishitiv"));
            students.add(new Student("S103", "Laksh"));
            students.add(new Student("S104", "Aksh"));
        }
        if (books.isEmpty()) {
            books.add(new Book("B101", "Operating System", "Silberschatz", 4, 4));
            books.add(new Book("B102", "Data Structures", "Mark Weiss", 3, 3));
            books.add(new Book("B103", "Java Programming", "James Gosling", 5, 5));
            books.add(new Book("B104", "Atomic Habits", "James Clear", 8, 8));
        }
    }

    boolean adminLogin(String adminId, String password) {
        for (Admin adminInfo : admins) {
            if (adminId.equals(adminInfo.getadminId()) && password.equals(adminInfo.getPassword())) {
                System.out.println("Authentication Successful\n");
                return true;
            }

        }
        return false;
    }

    boolean studentLogin(String studentId) {
        for (Student studentInfo : students) {
            if (studentId.equals(studentInfo.getstudentId())) {
                System.out.println("Authentication Successful\n");
                return true;
            }

        }
        return false;
    }

    void viewAllBooks() {
        for (Book b : books) {
            System.out.println(b);
        }
    }

    void viewAvailableBooks() {
        for (Book b : books) {
            if (b.availableCopies > 0) {
                System.out.println(b);
            }
        }
    }

    void viewAllStudents() {
        for (Student std : students) {
            System.out.println(std);
        }
    }

    void addAdmin(Admin newAdmin) {
        for (Admin a : admins) {
            if (a.adminId.equals(newAdmin.adminId)) {
                System.out.println("Admin with this ID already exists!");
                return; // Stop method
            }
        }

        admins.add(newAdmin);
        System.out.println("Admin added successfully!");
        saveAdminsToFile();
    }

    void addBook(Book newBook) {
        for (Book b : books) {
            if (b.bookId.equals(newBook.bookId)) {
                System.out.println("Book with this ID already exists!");
                return; // Stop method
            }
        }

        if (newBook.totalCopies <= 0 || newBook.availableCopies < 0) {
            System.out.println("Invalid copy numbers!");
            return;
        }

        if (!newBook.bookId.matches("[B][0-9]+")) {
            System.out.println("Book ID must start with 'B' followed by numbers.");
        }

        books.add(newBook);
        System.out.println("Book added successfully!");
        saveBooksToFile(); // Save after adding
    }

    void addStudent(Student s) {
        boolean studentExists = false;
        for (Student std : students) {
            if (std.studentId.equals(s.studentId)) {
                studentExists = true;
                break;
            }
        }
        if (!s.studentId.matches("[S][0-9]+")) {
            System.out.println("Student ID must start with 'S' followed by numbers.");
        }
        if (studentExists) {
            System.out.println("Error: A student with this ID already exists.");
        }

        else {
            students.add(s);
            saveStudentsToFile();
            System.out.println("Student added successfully!");
        }

        // saveStudentsToFile();
    }

    void issueBook(String stdid, String bookid) {
        boolean stdFound = false;
        boolean bookFound = false;
        Student selectedStudent = null;
        Book selectedBook = null;
        for (Book b : books) {
            if (b.bookId.equals(bookid)) {
                bookFound = true;
                selectedBook = b;
                break;
            }
        }
        for (Student s : students) {
            if (s.studentId.equals(stdid)) {
                stdFound = true;
                selectedStudent = s;
                break;
            }

        }
        if (bookFound && stdFound) {
            if (selectedBook.availableCopies > 0) {
                if (!selectedStudent.issuedBooks.contains(selectedBook.bookId)) {
                    selectedBook.availableCopies--;
                    selectedStudent.issuedBooks.add(selectedBook.bookId);
                    selectedBook.issuedTo.add(selectedStudent.studentId);
                    System.out.println("Book issued Successfully");
                } else {
                    System.out.println("You have already issued this book");
                }
            } else {
                System.out.println("Sorry,this book is not available right now");
            }

        } else {
            System.out.println("Invalid student Id or Book Id");
        }

    }

    void returnBook(String stdid, String bookid) {
        boolean stdFound = false;
        boolean bookFound = false;
        Student selectedStudent = null;
        Book selectedBook = null;
        for (Book b : books) {
            if (b.bookId.equals(bookid)) {
                bookFound = true;
                selectedBook = b;
                break;
            }
        }
        for (Student s : students) {
            if (s.studentId.equals(stdid)) {
                stdFound = true;
                selectedStudent = s;
                break;
            }

        }
        if (bookFound && stdFound) {
            if (selectedStudent.issuedBooks.contains(selectedBook.bookId)) {
                selectedBook.availableCopies++;
                selectedStudent.issuedBooks.remove(selectedBook.bookId);
                selectedBook.issuedTo.remove(selectedStudent.studentId);
                System.out.println("Book returned successfully. Now available copies: " + selectedBook.availableCopies);

            } else {
                System.out.println("You did not issued this book");
            }
        } else {
            System.out.println("Invalid student Id or Book Id");
        }

    }

    public void viewIssuedBooks(String stdid) {
        Student selectedStudent = null;
        for (Student s : students) {
            if (s.studentId.equals(stdid)) {
                selectedStudent = s;
                break;
            }
        }

        if (selectedStudent == null) {
            System.out.println("Invalid Student ID");
            return;
        }

        if (selectedStudent.issuedBooks.isEmpty()) {
            System.out.println("You have not issued any books yet.");
            return;
        }

        System.out.println("Books issued by " + selectedStudent.stdName + ":");
        for (String bookId : selectedStudent.issuedBooks) {
            for (Book b : books) {
                if (b.bookId.equals(bookId)) {
                    System.out.println(b.title);
                }
            }
        }

    }

    void saveAdminsToFile() {
        try (FileWriter myFileWriter = new FileWriter("admin.txt")) {
            for (Admin a : admins) {
                myFileWriter.write(
                        a.adminId + "," + a.admName + "," + a.password + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving Admins: " + e.getMessage());
        }
    }

    void saveStudentsToFile() {
        try (FileWriter myFileWriter = new FileWriter("students.txt")) {
            for (Student s : students) {
                myFileWriter.write(
                        s.studentId + "," + s.stdName + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving Students: " + e.getMessage());
        }
    }

    void saveBooksToFile() {
        try (FileWriter myFileWriter = new FileWriter("books.txt")) {
            for (Book b : books) {
                myFileWriter.write(
                        b.bookId + "," + b.title + "," + b.author + "," +
                                b.totalCopies + "," + b.availableCopies + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    void loadBooksFromFile() {
        books.clear(); // clear old data before loading new one
        try (Scanner fileReader = new Scanner(new File("books.txt"))) {
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 5) { // ensure the format is correct
                    String bookId = parts[0].trim();
                    String title = parts[1].trim();
                    String author = parts[2].trim();
                    int totalCopies = Integer.parseInt(parts[3].trim());
                    int availableCopies = Integer.parseInt(parts[4].trim());

                    books.add(new Book(bookId, title, author, totalCopies, availableCopies));
                }
            }
            System.out.println("Books loaded successfully from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No existing file found. Starting with default library data.");
        } catch (Exception e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    void loadStudentsFromFile() {
        students.clear(); // clear old data before loading new one
        try (Scanner fileReader = new Scanner(new File("students.txt"))) {
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 2) { // ensure the format is correct
                    String studentId = parts[0].trim();
                    String stdName = parts[1].trim();

                    students.add(new Student(studentId, stdName));
                }
            }
            System.out.println("Students loaded successfully from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No existing file found. Starting with default library data.");
        } catch (Exception e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    void loadAdminsFromFile() {
        admins.clear(); // clear old data before loading new one
        try (Scanner fileReader = new Scanner(new File("admin.txt"))) {
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 3) { // ensure the format is correct
                    String adminId = parts[0].trim();
                    String admName = parts[1].trim();
                    String password = parts[2].trim();

                    admins.add(new Admin(adminId, admName, password));
                }
            }
            System.out.println("Admins loaded successfully from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No existing file found. Starting with default library data.");
        } catch (Exception e) {
            System.out.println("Error loading Admins: " + e.getMessage());
        }
    }

    void searchBooks() {
        Scanner sc = new Scanner(System.in);
        System.out.println("<------ Search Menu ------>");
        System.out.println("1. Search by Book ID");
        System.out.println("2. Search by Title");
        System.out.println("3. Search by Author");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        boolean found = false;

        switch (choice) {
            case 1:
                System.out.print("Enter Book ID: ");
                String id = sc.nextLine();
                for (Book b : books) {
                    if (b.bookId.equalsIgnoreCase(id)) {
                        System.out.println(b);
                        found = true;
                    }
                }
                if (!found)
                    System.out.println("No book found with that ID.");
                break;

            case 2:
                System.out.print("Enter Book Title: ");
                String title = sc.nextLine();
                for (Book b : books) {
                    if (b.title.toLowerCase().contains(title.toLowerCase())) {
                        System.out.println(b);
                        found = true;
                    }
                }
                if (!found)
                    System.out.println("No book found with that title.");
                break;

            case 3:
                System.out.print("Enter Author Name: ");
                String author = sc.nextLine();
                for (Book b : books) {
                    if (b.author.toLowerCase().contains(author.toLowerCase())) {
                        System.out.println(b);
                        found = true;
                    }
                }
                if (!found)
                    System.out.println("No book found by that author.");
                break;

            default:
                System.out.println("Invalid choice!");
        }
    }

}

public class libManage {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();
        System.out.println("<-------Welcome to Library Management System------>");
        System.out.println("1.Admin Login");
        System.out.println("2.Student Login");
        System.out.println("3.Exit");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                System.out.println("Enter admin Id:");
                String adminIdLogin = sc.nextLine();
                System.out.println("Enter password");
                // Console console = System.console();
                // String passwordLogin;
                // if (console != null) {
                // char[] passChars = console.readPassword("Enter password: ");
                // passwordLogin = new String(passChars);
                // } else {
                // System.out.print("Enter password: ");
                // passwordLogin = sc.nextLine();
                // }

                String passwordLogin = sc.nextLine();
                boolean loginSuccess = library.adminLogin(adminIdLogin, passwordLogin);
                if (loginSuccess) {
                    boolean logout = false;
                    while (!logout) {
                        System.out.println("<-------Admin Menu------>");
                        System.out.println("1.Add Book");
                        System.out.println("2. Add Student");
                        System.out.println("3. Add Admin");
                        System.out.println("4. View All Books");
                        System.out.println("5. View All Students");
                        System.out.println("6.Search Book");
                        System.out.println("7. Logout");
                        int Adminchoice = sc.nextInt();
                        sc.nextLine();
                        switch (Adminchoice) {
                            case 1:
                                System.out.println("Enter Book id:");
                                String bookId = sc.nextLine();
                                System.out.println("Enter title of book");
                                String title = sc.nextLine();
                                System.out.println("Enter author of the book:");
                                String author = sc.nextLine();
                                System.out.println("Enter totalCopies:");
                                int totalCopies = sc.nextInt();
                                System.out.println("Enter available Copies");
                                int availableCopies = sc.nextInt();

                                Book newBook = new Book(bookId, title, author, totalCopies, availableCopies);
                                library.addBook(newBook);
                                // library.books.add(newBook);
                                // System.out.println("Book added successfully!");

                                break;
                            case 2:
                                // call library.addStudent()
                                System.out.println("Enter Student id:");
                                String studentId = sc.nextLine();
                                System.out.println("Enter name of the Student:");
                                String studentName = sc.nextLine();

                                Student newStudent = new Student(studentId, studentName);
                                library.addStudent(newStudent);

                                break;
                            case 3:
                                // call library.addStudent()
                                System.out.println("Enter Admin id:");
                                String adminId = sc.nextLine();
                                System.out.println("Enter name of the Admin:");
                                String admName = sc.nextLine();
                                System.out.println("Enter password ");
                                String password = sc.nextLine();

                                Admin newAdmin = new Admin(adminId, admName, password);
                                library.addAdmin(newAdmin);

                                break;
                            case 4:
                                library.viewAllBooks();
                                break;
                            case 5:
                                library.viewAllStudents();
                                break;
                            case 6:
                                // Scanner sc = new Scanner(System.in);

                                library.searchBooks();
                                break;
                            case 7:
                                logout = true;
                                break;
                            default:
                                System.out.println("Invalid choice! Try again.");
                        }
                    }

                } else {
                    System.out.println("Login Failed!");
                }

                break;

            case 2:
                System.out.println("Enter Student Id");
                String studentidLogin = sc.nextLine();
                boolean studentLogin = library.studentLogin(studentidLogin);
                if (studentLogin) {
                    boolean logout = false;
                    while (!logout) {
                        System.out.println("<---------Student Menu-------->");
                        System.out.println("1.View Available books");
                        System.out.println("2.Issue Book");
                        System.out.println("3.Return Book");
                        System.out.println("4.View issued Books");
                        System.out.println("5.Search book");
                        System.out.println("6.Logout");
                        int Studentchoice = sc.nextInt();
                        sc.nextLine();
                        switch (Studentchoice) {
                            case 1:
                                library.viewAvailableBooks();
                                break;
                            case 2:

                                System.out.println("Enter Book id");
                                String bookid = sc.nextLine();
                                library.issueBook(studentidLogin, bookid);
                                break;
                            case 3:
                                System.out.println("Enter Book id");
                                String bookkid = sc.nextLine();
                                library.returnBook(studentidLogin, bookkid);
                                break;
                            case 4:
                                library.viewIssuedBooks(studentidLogin);
                                break;
                            case 5:

                                library.searchBooks();
                                break;
                            case 6:
                                logout = true;
                                break;
                            default:
                                System.out.println("Invalid choice");
                                break;
                        }
                    }
                }

                break;
            case 3:
                System.out.println("Exiting... Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }

    }
}
