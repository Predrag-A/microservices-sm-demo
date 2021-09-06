package com.predrag.a.mediaservice.service.impl;

import com.predrag.a.mediaservice.model.ImageMetadata;
import com.predrag.a.mediaservice.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class DefaultFileStorageService implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    @Value("${file.path.prefix}")
    private String filePathPrefix;

    private final Environment environment;

    @Autowired
    public DefaultFileStorageService(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public ImageMetadata store(final MultipartFile file, final String username) {

        final String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (file.isEmpty()) {
            log.warn("File {} is empty", fileName);
            throw new IllegalArgumentException();
        }
        final String extension = FilenameUtils.getExtension(fileName);
        final String newFileName = UUID.randomUUID() + "." + extension;


        try (final InputStream inputStream = file.getInputStream()) {
            final Path userDir = Paths.get(uploadDirectory + "/" + username);

            if (Files.notExists(userDir)) {
                Files.createDirectory(userDir);
            }

            Files.copy(inputStream, userDir.resolve(newFileName),
                    StandardCopyOption.REPLACE_EXISTING);

            final String port = environment.getProperty("local.server.port");
            final String hostName = InetAddress.getLocalHost().getHostAddress();

            final String fileUrl = String.format("http://%s:%s%s/%s/%s",
                    hostName, port, filePathPrefix, username, newFileName);

            log.info("Successfully stored file {} location {}", fileName, fileUrl);

            return new ImageMetadata(newFileName, fileUrl, Objects.requireNonNull(file.getContentType()));

        } catch (final IOException e) {
            log.error("Failed to store file {} error: {}", fileName, e);
            throw new IllegalStateException("Failed to store file " + fileName, e);
        }
    }
}
