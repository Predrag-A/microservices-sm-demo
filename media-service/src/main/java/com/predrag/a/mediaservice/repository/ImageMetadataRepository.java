package com.predrag.a.mediaservice.repository;

import com.predrag.a.mediaservice.model.ImageMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMetadataRepository extends MongoRepository<ImageMetadata, String> {
}
