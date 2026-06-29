package com.github.mavenpractice.domain.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BatchUpdateStatusRequest {

    @NotEmpty
    private List<Long> ids;

    @NotNull
    private String status;
}
