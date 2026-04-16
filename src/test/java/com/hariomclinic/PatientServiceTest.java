package com.hariomclinic;

import com.hariomclinic.dto.PatientRequest;
import com.hariomclinic.dto.PatientResponse;
import com.hariomclinic.model.Patient;
import com.hariomclinic.repository.PatientNoteRepository;
import com.hariomclinic.repository.PatientRepository;
import com.hariomclinic.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * PatientServiceTest - Unit tests for PatientService business logic.
 *
 * @ExtendWith(MockitoExtension.class) -> Enables Mockito annotations without Spring context.
 *   Faster than @SpringBootTest - only tests the service class in isolation.
 *
 * @Mock -> Creates a Mockito mock of the repository (no real DB calls)
 * @InjectMocks -> Creates PatientService and injects the mocks into it
 *
 * AssertJ (assertThat) -> Fluent assertion library, more readable than JUnit assertions
 *
 * verify() -> Asserts that a mock method was called with specific arguments
 */
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientNoteRepository noteRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient mockPatient;

    @BeforeEach
    void setUp() {
        mockPatient = new Patient();
        mockPatient.setId(1L);
        mockPatient.setName("Ramesh Kumar");
        mockPatient.setAge(35);
        mockPatient.setMobileNumber("9876543210");
        mockPatient.setLocation("Wada, Palghar");
        mockPatient.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createPatient_withValidRequest_returnsPatientResponse() {
        // Arrange
        PatientRequest request = new PatientRequest();
        request.setName("Ramesh Kumar");
        request.setAge(35);
        request.setMobileNumber("9876543210");
        request.setLocation("Wada, Palghar");

        when(patientRepository.save(any(Patient.class))).thenReturn(mockPatient);
        when(noteRepository.findByPatientIdOrderByCreatedAtDesc(1L)).thenReturn(List.of());

        // Act
        PatientResponse response = patientService.createPatient(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Ramesh Kumar");
        assertThat(response.getAge()).isEqualTo(35);
        assertThat(response.getNotes()).isEmpty();

        // Verify repository was called once
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void getPatientById_withValidId_returnsPatient() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.of(mockPatient));
        when(noteRepository.findByPatientIdOrderByCreatedAtDesc(1L)).thenReturn(List.of());

        // Act
        PatientResponse response = patientService.getPatientById(1L);

        // Assert
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Ramesh Kumar");
    }

    @Test
    void getPatientById_withInvalidId_throwsException() {
        // Arrange
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert - assertThatThrownBy verifies exception is thrown
        assertThatThrownBy(() -> patientService.getPatientById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void deletePatient_withValidId_callsRepositoryDelete() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.of(mockPatient));

        // Act
        patientService.deletePatient(1L);

        // Assert - verify delete was called with the correct patient
        verify(patientRepository, times(1)).delete(mockPatient);
    }

    @Test
    void searchPatients_returnsMatchingPatients() {
        // Arrange
        when(patientRepository.findByNameContainingIgnoreCase("ramesh"))
                .thenReturn(List.of(mockPatient));
        when(noteRepository.findByPatientIdOrderByCreatedAtDesc(1L)).thenReturn(List.of());

        // Act
        List<PatientResponse> results = patientService.searchPatients("ramesh");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Ramesh Kumar");
    }
}
