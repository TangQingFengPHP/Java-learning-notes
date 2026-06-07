package com.github.flyway.service;

import com.github.flyway.entity.Product;
import com.github.flyway.model.UserOrderSummaryDTO;
import com.github.flyway.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRepository productRepository;

    public List<UserOrderSummaryDTO> userOrderSummary() {
        return jdbcTemplate.query("""
                        select user_id, username, order_count, total_amount
                        from v_user_order_summary
                        order by user_id
                        """,
                (rs, rowNum) -> new UserOrderSummaryDTO(
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        rs.getLong("order_count"),
                        rs.getBigDecimal("total_amount")
                ));
    }

    public Optional<String> getConfigValue(String key) {
        List<String> values = jdbcTemplate.query("""
                        select config_value from app_config where config_key = ?
                        """,
                (rs, rowNum) -> rs.getString(1),
                key);
        return values.isEmpty() ? Optional.empty() : Optional.of(values.getFirst());
    }

    @Transactional
    public Long createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return productRepository.save(product).getId();
    }

    public List<Product> listProducts() {
        return productRepository.findAll();
    }
}
