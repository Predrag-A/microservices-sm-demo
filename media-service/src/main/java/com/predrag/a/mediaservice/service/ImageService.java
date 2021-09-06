package com.predrag.a.mediaservice.service;

import com.predrag.a.mediaservice.model.ImageMetadata;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    
    ImageMetadata upload(MultipartFile file, String username);
}
