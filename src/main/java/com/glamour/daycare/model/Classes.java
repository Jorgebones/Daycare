package com.glamour.daycare.model;

import jakarta.persistence.Entity;

@Entity
public class Classes {

  private long id;
  private String courseName;
  private long teacherId;
  private String teacherName;
}
