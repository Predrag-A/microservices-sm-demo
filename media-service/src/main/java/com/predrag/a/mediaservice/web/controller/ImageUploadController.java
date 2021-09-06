package com.predrag.a.mediaservice.web.controller;

import com.predrag.a.mediaservice.model.ImageMetadata;
import com.predrag.a.mediaservice.service.ImageService;
import com.predrag.a.mediaservice.web.dto.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class ImageUploadController {

    private final ImageService imageService;

    @Autowired
    public ImageUploadController(final ImageService imageService) {
        this.imageService = imageService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public FileUploadResponse uploadFile(@RequestParam final MultipartFile image,
                                         @AuthenticationPrincipal final String username) {
        final ImageMetadata metadata = imageService.upload(image, username);
        return new FileUploadResponse(metadata.getFilename(), metadata.getUri(), metadata.getFileType());
    }
}
