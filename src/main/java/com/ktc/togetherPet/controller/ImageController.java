package com.ktc.togetherPet.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG;

import com.ktc.togetherPet.service.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{file-name}")
    public ResponseEntity<byte[]> getImage(
        @PathVariable("file-name") String fileName
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(IMAGE_JPEG);

        return ResponseEntity
            .status(OK)
            .headers(headers)
            .body(imageService.getImageBytesFromFileName(fileName));
    }
}
