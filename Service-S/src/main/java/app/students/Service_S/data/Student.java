package app.students.Service_S.data;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_id_seq_gen")
    @SequenceGenerator(
            name = "student_id_seq_gen",
            sequenceName = "student_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "record_book", nullable = false, unique = true)
    private String recordBook;

    @Column(nullable = false)
    private String faculty;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordBook() {
        return recordBook;
    }

    public void setRecordBook(String recordBook) {
        this.recordBook = recordBook;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
