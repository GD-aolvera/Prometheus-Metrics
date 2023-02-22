package com.gd.prometrics.controller;

import com.gd.prometrics.monitoring.CustomMetricsBean;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gd.prometrics.model.Student;
import com.gd.prometrics.service.StudentService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("SuspiciousMethodCalls")
@RestController
@RequestMapping("/student")
public class StudentController {

  @Autowired
  private StudentService studentService;

  @Autowired
  private CustomMetricsBean customMetricsBean;

  private final List<Student> addedStudentList = new ArrayList<>();

  private final List<Student> deletedStudentsList = new ArrayList<>();

  private final List<Student> studentList = new ArrayList<>();

  public Supplier<Number> fetchStudentCount(){
    return addedStudentList::size;
  }

  public Supplier<Number> fetchDeletedStudents(){
    return deletedStudentsList::size;
  }

  public Supplier<Number> fetchStudentsInDB(){
    return studentList::size;
  }

  public StudentController (MeterRegistry registry){
    //noinspection SpellCheckingInspection
    Gauge.builder("studentcontroller.studentcount",fetchStudentCount()).
        tag("version", "v1").
        description("ammount of added students in DB").
        register(registry);
  }

  @PostMapping
  public ResponseEntity<Student> saveStudent (@Valid @RequestBody Student student){
    addedStudentList.add(student);
    studentList.add(student);
    return ResponseEntity.status(HttpStatus.CREATED).body(studentService.saveStudent(student));
  }


  @Timed(value="students.all.get.time", description = "time to retrieve all students", percentiles = {1})
  @GetMapping
  public ResponseEntity<Page<Student>> getAllStudent (
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "false") Boolean enablePagination
  ){
    customMetricsBean.updateStudentSummary();
    return ResponseEntity.ok(studentService.getAllStudent(page, size, enablePagination));
  }

  @SuppressWarnings("rawtypes")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity deleteStudent(@PathVariable ("id") Long id){
    updateDeleteFromStudentLists(id);
    studentService.deleteStudent(id);
    return ResponseEntity.ok(!studentService.existById(id));
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Optional<Student>> findStudentById(@PathVariable ("id") Long id){
    return ResponseEntity.status(HttpStatus.OK).body(studentService.findById(id));
  }

  @PutMapping
  public ResponseEntity<Student> editStudent (@Valid @RequestBody Student student){
    return ResponseEntity.status(HttpStatus.CREATED).body(studentService.editStudent(student));
  }

  private void updateDeleteFromStudentLists(Long id) {
    Student removed = addedStudentList
        .stream()
        .filter(student -> Objects.equals(student.getId(), id)).findAny().orElse(null);
    deletedStudentsList.add(removed);
    studentList.remove(removed);
  }


}
