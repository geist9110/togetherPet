package com.ktc.togetherPet.service;

import static com.ktc.togetherPet.model.entity.ImageRelation.ImageEntityType.PET;

import com.ktc.togetherPet.model.dto.pet.PetRegisterDTO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RegisterService {

    private final PetService petService;
    private final ImageService imageService;
    private final UserService userService;

    public RegisterService(PetService petService, ImageService imageService,
        UserService userService) {
        this.petService = petService;
        this.imageService = imageService;
        this.userService = userService;
    }

    @Transactional
    public void create(PetRegisterDTO petRegisterDTO,
        MultipartFile petImage, String email, String userName) {
        Long petId = petService.createPet(petRegisterDTO);

        imageService.saveImages(petId, PET, List.of(petImage));

        // TODO 여러장이 될 수 있음
        List<String> imageFilePath = imageService.getImageUrl(petId, PET);

//        petService.setImageSrc(petId, imageFilePath);

        userService.setUserPet(petId, email);
        userService.setUserName(email, userName);
    }
}
