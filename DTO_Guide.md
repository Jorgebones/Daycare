# 📘 DTO Architecture Notes
### *Guide for Correct DTO Usage in Spring Boot Applications*

This document explains how to structure and use Data Transfer Objects (DTOs) in the Daycare project.  
It serves as a reminder of best practices, use cases, and mapping patterns.

---

# 🧩 1. What is a DTO?

A **Data Transfer Object (DTO)** is a simple data container used to exchange data between:

- Controllers  
- Services  
- The frontend  
- The database layer  

DTOs **prevent exposing your JPA entities directly**, which improves:

- Security  
- Flexibility  
- Separation of concerns  
- Clean architecture  
- Easier API versioning  

---

# 🧩 2. Why do we need multiple DTOs?

Because different operations require **different shapes** of data.

Typical DTO types:

### ✔ **CreateDTO**  
Used for POST requests (creating new records).  
Contains **only the fields needed to create something**.

### ✔ **ReadDTO / FullDTO**  
Used to return detailed information to the client.  
Contains nested DTOs, summary lists, and all fields needed by the frontend.

### ✔ **SummaryDTO**  
A lightweight version of an entity, used inside other DTOs to reduce payload size.

### ✔ **UpdateDTO (optional)**  
Used for PUT requests (full updates).  

### ✔ **PatchDTO (optional)**  
Used for PATCH requests (partial updates).  

---

# 🧩 3. When to use each DTO?

| Operation | Use Which DTO? | Why |
|-----------|----------------|-----|
| **POST** | CreateDTO | Build a NEW entity |
| **GET** | ReadDTO | Return complete structured data |
| **PUT** | UpdateDTO | Replace many fields of EXISTING entity |
| **PATCH** | UpdateDTO or PatchDTO | Replace SOME fields |
| **Internally** | SummaryDTO | For lists/nested data |

---

# 🧩 4. Why POST needs a `toEntity()` but PUT/PATCH do NOT

### ✔ POST = brand new object → needs a full conversion  
### ✔ PUT/PATCH = modify existing object → update fields only

Rebuilding the entity during update would:

- Lose relationships  
- Lose IDs  
- Overwrite data  
- Break the database consistency  

---

# 🧩 5. Mapping patterns

### ✔ Entity → DTO (Returning data)
```java
public TeacherDTO toDTO(Teacher teacher) {
    return TeacherDTO.builder()
        .id(teacher.getId())
        .firstName(teacher.getFirstName())
        .lastName(teacher.getLastName())
        .classrooms(
            teacher.getClasses().stream()
                .map(c -> new ClassroomSummaryDTO(c.getId(), c.getClassName()))
                .toList()
        )
        .build();
}
```

---

### ✔ DTO → Entity (Only for POST)
```java
public Teacher toEntity(TeacherCreateDTO dto) {
    return Teacher.builder()
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .classes(List.of()) // new teachers start with 0 classrooms
        .build();
}
```

---

# 🧩 6. Practical rules

### ✔ Never attach classrooms or children inside CreateDTOs  
### ✔ Always return lists using SummaryDTOs in ReadDTOs  
### ✔ Never overwrite entities during update — only modify fields  
### ✔ DTOs contain **no behavior**, only data  
### ✔ Mapping stays inside **services**, never controllers  
### ✔ Use loops if streams feel complex — both are correct  

---

# 🧩 7. Summary Table

| Action | DTO | Mapping |
|--------|------|----------|
| POST | CreateDTO | toEntity() |
| GET | ReadDTO | toDTO() |
| PUT | UpdateDTO | update fields |
| PATCH | PatchDTO | partial update |
| Internal lists | SummaryDTO | used inside toDTO() |

---

# 🧩 8. Tips

- CreateDTOs → minimal, ONLY what’s needed  
- ReadDTOs → full nested structure  
- Keep DTOs separated by responsibility  
- Protect against null lists  
- Use SummaryDTOs for preventing deep recursion  
- Streams are optional; loops are fine  
- Keep DTO package organized  

---
