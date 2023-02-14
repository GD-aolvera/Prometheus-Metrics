package com.gd.prometrics.controller;

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
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/student")
public class StudentController {

  @Autowired
  private StudentService studentService;

  private List<Student> studentList = new ArrayList<>();

  public Supplier<Number> fetchStudentCount(){
    return () -> studentList.size();
  }

  public StudentController (MeterRegistry registry){
    Gauge.builder("studencontroller.studentcount",fetchStudentCount()).
        tag("version", "v1").
        description("ammount of added students").
        register(registry);
  }

  @PostMapping
  public ResponseEntity<Student> saveStudent (@Valid @RequestBody Student student){
    studentList.add(student);
    return ResponseEntity.status(HttpStatus.CREATED).body(studentService.saveStudent(student));
  }


  @GetMapping
  public ResponseEntity<Page<Student>> getAllStudent (
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "false") Boolean enablePagination
  ){
    return ResponseEntity.ok(studentService.getAllStudent(page, size, enablePagination));
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity deleteStudent(@PathVariable ("id") Long id){
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


}
