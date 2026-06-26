package com.github.completablefuture.repository;

import com.github.completablefuture.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public Optional<Product> findById(Long id) {
        String sql = """
                select %s from tb_product where id = ?
                """.formatted(RowMappers.productSelectColumns());
        List<Product> products = jdbcTemplate.query(sql, RowMappers.PRODUCT, id);
        return products.stream().findFirst();
    }

    public int deductStock(Long productId, int quantity) {
        String sql = """
                update tb_product
                set stock = stock - ?
                where id = ? and stock >= ?
                """;
        return jdbcTemplate.update(sql, quantity, productId, quantity);
    }
}
