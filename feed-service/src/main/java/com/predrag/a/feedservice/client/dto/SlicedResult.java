package com.predrag.a.feedservice.client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SlicedResult<T> {

    private Integer page;
    private boolean isLast;
    private List<T> content;
}