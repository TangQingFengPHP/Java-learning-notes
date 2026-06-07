package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

/**
 * Java Migration 示例：将已有用户邮箱统一为小写。
 */
public class V9__normalize_user_email extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.executeUpdate("update users set email = lower(email) where email <> lower(email)");
        }
    }
}
