package com.github.liquibase.service;

import com.github.liquibase.model.ChangelogRecordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LiquibaseAdminService {

    private final JdbcTemplate jdbcTemplate;

    public long countExecutedChangesets() {
        Long count = jdbcTemplate.queryForObject(
                "select count(*) from DATABASECHANGELOG",
                Long.class
        );
        return count == null ? 0 : count;
    }

    public List<ChangelogRecordDTO> listExecutedChangesets() {
        return jdbcTemplate.query("""
                        select id, author, filename, dateexecuted, orderexecuted, description, contexts, labels
                        from DATABASECHANGELOG
                        order by orderexecuted
                        """,
                (rs, rowNum) -> {
                    Timestamp executed = rs.getTimestamp("dateexecuted");
                    return new ChangelogRecordDTO(
                            rs.getString("id"),
                            rs.getString("author"),
                            rs.getString("filename"),
                            executed == null ? null : executed.toLocalDateTime(),
                            rs.getInt("orderexecuted"),
                            rs.getString("description"),
                            rs.getString("contexts"),
                            rs.getString("labels")
                    );
                });
    }
}
