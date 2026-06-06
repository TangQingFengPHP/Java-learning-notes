package com.github.liquibase.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChangelogRecordDTO {
    private String id;
    private String author;
    private String filename;
    private LocalDateTime dateExecuted;
    private Integer orderExecuted;
    private String description;
    private String contexts;
    private String labels;
}
