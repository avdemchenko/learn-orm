package learnorm.session;

import learnorm.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;

import static learnorm.util.EntityUtil.*;

@RequiredArgsConstructor
public class JdbcEntityDao {

    private final String SELECT_FROM_TABLE_BY_COLUMN = "SELECT * FROM %s where %s = ?";
    private final DataSource dataSource;

    @SneakyThrows
    public <T> T findById(Class<T> entityType, Object id) {
        var idField = getIdField(entityType);
        return findOneBy(entityType, idField, id);
    }

    @SneakyThrows
    public <T> T findOneBy(Class<T> entityType, Field field, Object columnValue) {
        try (var connection = dataSource.getConnection()) {
            var tableName = resolveTableName(entityType);
            var columnName = resolveColumnName(field);
            var selectSql = String.format(SELECT_FROM_TABLE_BY_COLUMN, tableName, columnName);
            try (var statement = connection.prepareStatement(selectSql)) {
                statement.setObject(1, columnValue);
                var resultSet = statement.executeQuery();
                resultSet.next();
                return createEntityFromResultSet(entityType, resultSet);
            }
        }
    }

    @SneakyThrows
    private <T> T createEntityFromResultSet(Class<T> entityType, ResultSet resultSet) {
        var constructor = entityType.getConstructor();
        var entity = constructor.newInstance();
        for (Field field : entityType.getDeclaredFields()) {
            var columnName = resolveColumnName(field);
            var columnValue = resultSet.getObject(columnName);
            field.setAccessible(true);
            field.set(entity, columnValue);
        }
        return entity;
    }
}
