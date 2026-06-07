package com.github.flyway.controller;

import com.github.flyway.model.MigrationRecordDTO;
import com.github.flyway.service.FlywayAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/migrations")
@RequiredArgsConstructor
public class FlywayAdminController {

    private final FlywayAdminService flywayAdminService;

    @GetMapping
    public List<MigrationRecordDTO> list() {
        return flywayAdminService.listMigrations();
    }

    @GetMapping("/count")
    public Map<String, Long> count() {
        return Map.of("executed", flywayAdminService.countSuccessfulMigrations());
    }
}
