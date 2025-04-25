package com.project.webflux.dto;

import lombok.Builder;

@Builder
public record ProductDto (
    Integer id, String description, Integer price
) {}
