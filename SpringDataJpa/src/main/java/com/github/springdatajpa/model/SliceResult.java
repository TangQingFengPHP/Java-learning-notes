package com.github.springdatajpa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@AllArgsConstructor
public class SliceResult<T> {
    private List<T> records;
    private boolean hasNext;
    private int pageNumber;
    private int pageSize;

    public static <T> SliceResult<T> from(Slice<T> slice) {
        return new SliceResult<>(
                slice.getContent(),
                slice.hasNext(),
                slice.getNumber() + 1,
                slice.getSize()
        );
    }
}
