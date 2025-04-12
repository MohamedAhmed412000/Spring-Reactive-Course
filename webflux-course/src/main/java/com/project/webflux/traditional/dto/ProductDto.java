package com.project.webflux.traditional.dto;

public record ProductDto(
    Integer id,
    String description,
    Integer price
) {}
