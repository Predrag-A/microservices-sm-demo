package com.predrag.a.mediaservice.web.dto;

public record FileUploadResponse(String fileName,
                                 String uri,
                                 String fileType) {
}
