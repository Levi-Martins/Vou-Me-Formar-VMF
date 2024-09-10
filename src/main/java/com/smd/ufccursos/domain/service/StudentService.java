package com.smd.ufccursos.domain.service;

import com.smd.ufccursos.domain.DTO.PageTO;
import com.smd.ufccursos.domain.DTO.PaginationTO;
import com.smd.ufccursos.domain.DTO.StudentTO;
import com.smd.ufccursos.domain.entity.Course;
import com.smd.ufccursos.domain.entity.Phone;
import com.smd.ufccursos.domain.entity.Student;
import com.smd.ufccursos.domain.ports.repositoryPort.StudentRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;
import com.smd.ufccursos.domain.ports.servicePort.StudentServicePort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class StudentService implements StudentServicePort {

    private final StudentRepositoryPort studentRepositoryPort;
    private final CourseServicePort courseServicePort;

    public StudentService(StudentRepositoryPort studentRepositoryPort, CourseServicePort courseServicePort) {
        this.studentRepositoryPort = studentRepositoryPort;
        this.courseServicePort = courseServicePort;
    }

    @Override
    public PageTO<Student> findAll(PaginationTO paginationTO) {
        return studentRepositoryPort.findAll(paginationTO);
    }

    @Override
    public Student findById(UUID id) {
        Optional<Student> student = studentRepositoryPort.findById(id);
        if (student.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        return student.get();
    }

    @Override
    public Student save(StudentTO studentTO) {
        Course course = courseServicePort.findById(studentTO.getCourseId());

        Student student = Student.builder()
                .name(studentTO.getName())
                .email(studentTO.getEmail())
                .registration(studentTO.getRegistration())
                .course(course)
                .build();

        Student savedStudent = studentRepositoryPort.save(student);

        if (studentTO.getPhones() != null && !studentTO.getPhones().isEmpty()) {
            List<Phone> phones = studentTO.getPhones().stream().map(phoneTO -> {
                Phone phone = Phone.builder()
                        .ddd(phoneTO.getDdd())
                        .number(phoneTO.getNumber())
                        .student(savedStudent).build();
                return phone;
            }).collect(Collectors.toList());

            savedStudent.setPhones(phones);
        }

        return studentRepositoryPort.save(savedStudent);
    }



    @Override
    public Student update(UUID id, Student student) {
       Student studentToUpdate = findById(id);
       studentToUpdate.setName(student.getName());
       studentToUpdate.setEmail(student.getEmail());
       studentToUpdate.setRegistration(student.getRegistration());
       studentToUpdate.setRegistrationDate(LocalDate.now());
       studentToUpdate.setCourse(student.getCourse());
       studentToUpdate.setPhones(student.getPhones());
       return studentRepositoryPort.save(studentToUpdate);
    }

    @Override
    public void deleteById(UUID id) {
        studentRepositoryPort.deleteById(id);
    }
}
