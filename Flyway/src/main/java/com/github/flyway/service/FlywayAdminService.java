package com.github.flyway.service;

import com.github.flyway.model.MigrationRecordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlywayAdminService {

    private final JdbcTemplate jdbcTemplate;

    public long countSuccessfulMigrations() {
        Long count = jdbcTemplate.queryForObject(
                "select count(*) from flyway_schema_history where success = true",
                Long.class
        );
        return count == null ? 0 : count;
    }

    public List<MigrationRecordDTO> listMigrations() {
        return jdbcTemplate.query("""
                        select installed_rank, version, description, type, script, checksum,
                               installed_by, installed_on, execution_time, success
                        from flyway_schema_history
                        order by installed_rank
                        """,
                (rs, rowNum) -> {
                    Timestamp installedOn = rs.getTimestamp("installed_on");
                    return new MigrationRecordDTO(
                            rs.getInt("installed_rank"),
                            rs.getString("version"),
                            rs.getString("description"),
                            rs.getString("type"),
                            rs.getString("script"),
                            rs.getObject("checksum") == null ? null : rs.getInt("checksum"),
                            rs.getString("installed_by"),
                            installedOn == null ? null : installedOn.toLocalDateTime(),
                            rs.getInt("execution_time"),
                            rs.getBoolean("success")
                    );
                });
    }
}
