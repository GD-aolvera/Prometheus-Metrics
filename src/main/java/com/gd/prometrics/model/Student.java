package com.gd.prometrics.model;
import lombok.Data;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table
public class Student {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String lastName;
  private Integer age;
}