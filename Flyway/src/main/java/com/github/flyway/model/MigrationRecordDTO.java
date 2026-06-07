package com.github.flyway.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MigrationRecordDTO {
    private Integer installedRank;
    private String version;
    private String description;
    private String type;
    private String script;
    private Integer checksum;
    private String installedBy;
    private LocalDateTime installedOn;
    private Integer executionTime;
    private Boolean success;
}
