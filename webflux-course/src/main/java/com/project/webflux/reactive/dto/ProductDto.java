package com.project.webflux.reactive.dto;

public record ProductDto(
    Integer id,
    String description,
    Integer price
) {}
