package com.gd.prometrics.model;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@jakarta.persistence.Entity
@Table
public class Student {

  @jakarta.persistence.Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String lastName;
  private Integer age;
}