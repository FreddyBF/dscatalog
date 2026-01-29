package com.github.freddy.dscatalog.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> data,
        PaginationMetadata pagination
) {
    public record PaginationMetadata(
            int page,
            int size,
            Long totalElements,
            int totalPPages,
            boolean hasPrevious,
            boolean hasNext
    ) {}

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                new PaginationMetadata(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.hasPrevious(),
                        page.hasNext()
                )
        );
    }
}