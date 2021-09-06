package com.predrag.a.postservice.web.dto;

import com.predrag.a.postservice.model.Post;

public record ApiResponse(Boolean success,
                          String message,
                          Post post) {
}
