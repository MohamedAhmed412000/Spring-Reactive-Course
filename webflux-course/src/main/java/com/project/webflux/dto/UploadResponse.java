package com.project.webflux.dto;

import java.util.UUID;

public record UploadResponse (
    UUID confirmationId,
    Long productsCount
) {}
