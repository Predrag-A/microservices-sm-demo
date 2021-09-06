package com.predrag.a.mediaservice.service.impl;

import com.predrag.a.mediaservice.model.ImageMetadata;
import com.predrag.a.mediaservice.repository.ImageMetadataRepository;
import com.predrag.a.mediaservice.service.FileStorageService;
import com.predrag.a.mediaservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DefaultImageService implements ImageService {

    private final FileStorageService fileStorageService;

    private final ImageMetadataRepository imageMetadataRepository;

    @Autowired
    public DefaultImageService(final FileStorageService fileStorageService,
                               final ImageMetadataRepository imageMetadataRepository) {
        this.fileStorageService = fileStorageService;
        this.imageMetadataRepository = imageMetadataRepository;
    }

    @Override
    public ImageMetadata upload(final MultipartFile file, final String username) {
        final ImageMetadata imageMetadata = fileStorageService.store(file, username);
        return imageMetadataRepository.insert(imageMetadata);
    }
}
