package vn.ducbackend.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private String sort;
    private long totalElements;
    private int totalPages;
    private int numberOfElements;
}
