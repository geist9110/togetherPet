package com.ktc.togetherPet.service;

import static com.ktc.togetherPet.exception.ErrorMessage.PET_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.ktc.togetherPet.exception.CustomException;
import com.ktc.togetherPet.model.dto.pet.PetRegisterRequestDTO;
import com.ktc.togetherPet.model.entity.Breed;
import com.ktc.togetherPet.model.entity.Pet;
import com.ktc.togetherPet.repository.PetRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private BreedService breedService;

    @InjectMocks
    private PetService petService;

    @Nested
    @DisplayName("펫 아이디를 기반으로 펫을 찾는 기능 테스트/findPetById")
    class 펫_아이디를_기반으로_펫을_찾는_기능 {

        @Test
        @DisplayName("성공")
        void 성공() {
            // given
            long petId = 1L;
            Pet expect = new Pet(
                "testPetName",
                1L,
                new Breed("testBreed"),
                true
            );

            // when
            when(petRepository.findById(petId))
                .thenReturn(Optional.of(expect));

            // then
            assertEquals(expect, petService.findPetById(petId));

            verify(petRepository, times(1))
                .findById(petId);
        }

        @Test
        @DisplayName("실패")
        void 실패() {
            // given
            long petId = 1L;

            // when
            when(petRepository.findById(petId))
                .thenReturn(Optional.empty());

            // then
            CustomException thrown = assertThrows(
                CustomException.class,
                () -> petService.findPetById(petId)
            );

            assertAll(
                () -> assertEquals(PET_NOT_FOUND, thrown.getErrorMessage()),
                () -> assertEquals(NOT_FOUND, thrown.getStatus())
            );

            verify(petRepository, times(1))
                .findById(petId);
        }
    }

    @Test
    @DisplayName("펫 생성 테스트/createPet")
    void 펫_생성() {
        // given
        PetRegisterRequestDTO petRegisterRequestDTO = new PetRegisterRequestDTO(
            "testPetName",
            1,
            "testPetBreed",
            true,
            "testPetFeature"
        );

        Breed expectBreed = new Breed(petRegisterRequestDTO.petType());

        Pet expectPet = new Pet(
            petRegisterRequestDTO.petName(),
            petRegisterRequestDTO.petBirthMonth(),
            expectBreed,
            petRegisterRequestDTO.isNeutering()
        );

        Pet savedPet = spy(expectPet);

        long expectPetId = 1L;

        // when
        when(breedService.findBreedByName(petRegisterRequestDTO.petType()))
            .thenReturn(expectBreed);

        when(petRepository.save(expectPet))
            .thenReturn(savedPet);

        when(savedPet.getId())
            .thenReturn(expectPetId);

        // then
        assertEquals(expectPetId, petService.createPet(petRegisterRequestDTO));

        verify(breedService, times(1))
            .findBreedByName(petRegisterRequestDTO.petType());

        verify(petRepository, times(1))
            .save(expectPet);
    }
}