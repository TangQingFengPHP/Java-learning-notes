package com.github.rabbitmq.repository;

import com.github.rabbitmq.model.MessageLog;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageLogRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean exists(String messageId, String consumerName) {
        String sql = "select count(*) from tb_message_log where message_id = ? and consumer_name = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, messageId, consumerName);
        return count != null && count > 0;
    }

    public void save(String messageId, String consumerName, String routingKey, String payloadSummary) {
        String sql = """
                insert into tb_message_log (message_id, consumer_name, routing_key, payload_summary, created_at)
                values (?, ?, ?, ?, ?)
                """;
        try {
            jdbcTemplate.update(sql, messageId, consumerName, routingKey, payloadSummary, LocalDateTime.now());
        } catch (DuplicateKeyException ex) {
            // 幂等：并发插入同一条消费记录时忽略
        }
    }

    public List<MessageLog> findRecent(int limit) {
        String sql = """
                select id, message_id, consumer_name, routing_key, payload_summary, created_at
                from tb_message_log order by id desc limit ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MessageLog log = new MessageLog();
            log.setId(rs.getLong("id"));
            log.setMessageId(rs.getString("message_id"));
            log.setConsumerName(rs.getString("consumer_name"));
            log.setRoutingKey(rs.getString("routing_key"));
            log.setPayloadSummary(rs.getString("payload_summary"));
            log.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return log;
        }, limit);
    }
}
