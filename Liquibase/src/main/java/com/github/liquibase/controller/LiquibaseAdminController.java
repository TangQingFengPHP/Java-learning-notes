package com.github.liquibase.controller;

import com.github.liquibase.model.ChangelogRecordDTO;
import com.github.liquibase.service.LiquibaseAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/changelog")
@RequiredArgsConstructor
public class LiquibaseAdminController {

    private final LiquibaseAdminService liquibaseAdminService;

    @GetMapping
    public List<ChangelogRecordDTO> list() {
        return liquibaseAdminService.listExecutedChangesets();
    }

    @GetMapping("/count")
    public Map<String, Long> count() {
        return Map.of("executed", liquibaseAdminService.countExecutedChangesets());
    }
}
