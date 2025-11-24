package com.jorgegarcia.daycare.service;

import com.jorgegarcia.daycare.dto.TeacherCreateDTO;
import com.jorgegarcia.daycare.dto.TeacherDTO;
import com.jorgegarcia.daycare.dto.TeacherPatchDTO;
import com.jorgegarcia.daycare.dto.TeacherUpdateDTO;
import com.jorgegarcia.daycare.exception.DuplicateEntryException;
import com.jorgegarcia.daycare.exception.ResourceNotFoundException;
import com.jorgegarcia.daycare.model.Teacher;
import com.jorgegarcia.daycare.repository.TeacherRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 🍼 Enables Mockito for this test class
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository; // 🍼 Fake repository (no DB needed)

    @InjectMocks
    private TeacherService teacherService; // 🍼 Service with mocks injected

    private Teacher teacherEntity;

    @BeforeEach
    void setup() {
        // 🍼 Create a fake teacher entity reused in tests
        teacherEntity = Teacher.builder()
                .id(1L)
                .firstName("Marina")
                .lastName("Princess")
                .email("marina@test.com")
                .classes(List.of()) // no classrooms yet
                .build();
    }

    // ============================================================
    // 🍼 TEST 1 — getAllTeachers()
    // ============================================================
    @Test
    void shouldReturnAllTeachers() {

        // ARRANGE 🧸
        when(teacherRepository.findAll()).thenReturn(List.of(teacherEntity));

        // ACT 🍼
        List<TeacherDTO> result = teacherService.getAllTeachers();

        // ASSERT ✨
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("marina@test.com");

        verify(teacherRepository, times(1)).findAll();
    }

    // ============================================================
    // 🍼 TEST 2 — addTeacher() success
    // ============================================================
    @Test
    void shouldAddNewTeacherWhenEmailNotUsed() {

        // ARRANGE
        TeacherCreateDTO dto = TeacherCreateDTO.builder()
                .firstName("Marina")
                .lastName("Princess")
                .email("marina@test.com")
                .build();

        // email is available → empty Optional
        when(teacherRepository.findByEmail("marina@test.com")).thenReturn(Optional.empty());
        when(teacherRepository.save(any())).thenReturn(teacherEntity);

        // ACT
        TeacherDTO result = teacherService.addTeacher(dto);

        // ASSERT
        assertThat(result.getEmail()).isEqualTo("marina@test.com");

        verify(teacherRepository).save(any());
    }

    // ============================================================
    // 🍼 TEST 3 — addTeacher() duplicate email
    // ============================================================
    @Test
    void shouldNotAddTeacherWhenEmailAlreadyExists() {

        // ARRANGE
        TeacherCreateDTO dto = TeacherCreateDTO.builder()
                .firstName("Marina")
                .lastName("Princess")
                .email("marina@test.com")
                .build();

        // email exists → return entity
        when(teacherRepository.findByEmail("marina@test.com"))
                .thenReturn(Optional.of(teacherEntity));

        // ACT + ASSERT
        assertThatThrownBy(() -> teacherService.addTeacher(dto))
                .isInstanceOf(DuplicateEntryException.class)
                .hasMessage("Email already exists");

        verify(teacherRepository, never()).save(any());
    }
    @Test
    void shouldReturnTeacherById() {

    // ARRANGE 🧸
    when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacherEntity));

    // ACT 🍼
    TeacherDTO result = teacherService.getTeacherByTeacherId(1L);

    // ASSERT ✨
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getEmail()).isEqualTo("marina@test.com");

    verify(teacherRepository, times(1)).findById(1L);
  }

  @Test
  void shouldThrowWhenTeacherNotFound() {

      // ARRANGE 💔
      when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

      // ACT + ASSERT 💥
      assertThatThrownBy(() -> teacherService.getTeacherByTeacherId(99L))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage("Teacher not found");

      verify(teacherRepository, times(1)).findById(99L);
  }
  @Test
  void shouldUpdateTeacherById() {

      // ARRANGE 🍼
      when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacherEntity));

      TeacherUpdateDTO dto = TeacherUpdateDTO.builder()
              .firstName("Updated")
              .lastName("Name")
              .email("newemail@test.com")
              .build();

      Teacher updatedEntity = Teacher.builder()
              .id(1L)
              .firstName("Updated")
              .lastName("Name")
              .email("newemail@test.com")
              .classes(List.of())
              .build();

      when(teacherRepository.save(any())).thenReturn(updatedEntity);

      // ACT ✨
      TeacherDTO result = teacherService.updateTeacherById(1L, dto);

      // ASSERT 💗
      assertThat(result.getFirstName()).isEqualTo("Updated");
      assertThat(result.getEmail()).isEqualTo("newemail@test.com");
  }
  @Test
  void shouldThrowWhenUpdatingNonexistentTeacher() {

      // ARRANGE 💔
      when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

      TeacherUpdateDTO dto = TeacherUpdateDTO.builder()
              .firstName("New")
              .lastName("Name")
              .email("new@mail.com")
              .build();

      // ACT + ASSERT 💥
      assertThatThrownBy(() -> teacherService.updateTeacherById(99L, dto))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage("Teacher doesn't exist");
  }

  @Test
  void shouldPatchTeacherById() {

      // ARRANGE 🧸
      when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacherEntity));

      TeacherPatchDTO dto = TeacherPatchDTO.builder()
              .firstName("Patched")
              .build(); // only patching FIRST name

      Teacher patchedEntity = Teacher.builder()
              .id(1L)
              .firstName("Patched")
              .lastName("Princess")      // unchanged
              .email("marina@test.com")  // unchanged
              .classes(List.of())
              .build();

      when(teacherRepository.save(any())).thenReturn(patchedEntity);

      // ACT ✨
      TeacherDTO result = teacherService.patchTeacherById(1L, dto);

      // ASSERT 🍼
      assertThat(result.getFirstName()).isEqualTo("Patched");
      assertThat(result.getLastName()).isEqualTo("Princess"); // same
  }
  @Test
  void shouldThrowWhenPatchingNonexistentTeacher() {

      // ARRANGE ❌
      when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

      TeacherPatchDTO dto = TeacherPatchDTO.builder()
              .firstName("XXX")
              .build();

      // ACT + ASSERT 💥
      assertThatThrownBy(() -> teacherService.patchTeacherById(99L, dto))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage("Teacher doesn't exist");
  }
  @Test
  void shouldDeleteTeacher() {

      // ARRANGE 🍼
      when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacherEntity));

      // ACT ✨
      teacherService.deleteTeacher(1L);

      // ASSERT ✔
      verify(teacherRepository, times(1)).delete(teacherEntity);
  }
  @Test
  void shouldThrowWhenDeletingNonexistentTeacher() {

      // ARRANGE 💔
      when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

      // ACT + ASSERT 💥
      assertThatThrownBy(() -> teacherService.deleteTeacher(99L))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage("Teacher doesn't exist");

      verify(teacherRepository, never()).delete(any());
  }


}
