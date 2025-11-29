package com.wms.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public class PagedResponse<T> {

    private boolean success;
    private String message;
    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public PagedResponse() {}

    public PagedResponse(boolean success, String message, List<T> data, int page, int size,
                         long totalElements, int totalPages, boolean first, boolean last, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }
    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // Static factory methods
    public static <T> PagedResponse<T> of(Page<T> page, String message) {
        return new PagedResponse<>(
                true,
                message,
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                LocalDateTime.now()
        );
    }

    public static <T> PagedResponse<T> of(Page<T> page) {
        return of(page, "Data retrieved successfully");
    }
}