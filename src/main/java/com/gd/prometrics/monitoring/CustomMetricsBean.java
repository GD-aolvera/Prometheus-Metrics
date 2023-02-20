package com.gd.prometrics.monitoring;

import com.gd.prometrics.controller.StudentController;
import com.gd.prometrics.model.Student;
import com.gd.prometrics.repository.IStudentRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;
import io.micrometer.core.instrument.Tags;
import io.micrometer.elastic.ElasticMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class CustomMetricsBean {

  MultiGauge studentSummary = null;

  @Lazy
  @Autowired
  protected StudentController studentController;

  @Lazy
  @Autowired
  protected IStudentRepository studentRepository;

  public void updateStudentSummary() {
    boolean overWrite = true;
    studentSummary.register(studentRepository.findAll().stream().
        map(
        (Student s) -> MultiGauge.Row.of(Tags.of("name",""+s.getName(), "age", s.getAge().toString()),
            s.getId())
    ).
                         collect(Collectors.toList())
        ,overWrite);
  }

  public Supplier<Number> fetchDeletedStudentsCount() {
    return () -> studentController.fetchDeletedStudents().get();
  }

  public CustomMetricsBean(ElasticMeterRegistry registry) {
    studentSummary = MultiGauge.builder("student.age.details").tag("name", "age").register(registry);
    Gauge.builder("number.of.deleted.students", fetchDeletedStudentsCount()).register(registry);
  }

}
