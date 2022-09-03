package learnorm.session;

import learnorm.session.cache.EntityKey;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static learnorm.util.EntityUtil.*;

@Log4j2
@RequiredArgsConstructor
public class JdbcEntityDao {

    private final DataSource dataSource;
    private Map<EntityKey<?>, Object> entityCache = new HashMap<>();
    private boolean openStatus = true;

    private final String SELECT_FROM_TABLE_BY_COLUMN = "SELECT * FROM %s where %s = ?;";

    @SneakyThrows
    public <T> T findById(Class<T> entityType, Object id) {
        verifySessionIsOpened();
        var cachedEntity = entityCache.get(EntityKey.of(entityType, id));
        if (cachedEntity != null) return entityType.cast(cachedEntity);
        var idField = getIdField(entityType);
        return findOneBy(entityType, idField, id);
    }

    @SneakyThrows
    public <T> List<T> findAllBy(Class<T> entityType, Field field, Object columnValue) {
        verifySessionIsOpened();
        var list = new ArrayList<T>();
        try (var connection = dataSource.getConnection()) {
            var tableName = resolveTableName(entityType);
            var columnName = resolveColumnName(field);
            var selectSql = String.format(SELECT_FROM_TABLE_BY_COLUMN, tableName, columnName);
            try (var statement = connection.prepareStatement(selectSql)) {
                statement.setObject(1, columnValue);
                log.trace("SQL: {}", statement);
                var resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    var entity = createEntityFromResultSet(entityType, resultSet);
                    list.add(entity);
                }
            }
        }
        return list;
    }

    @SneakyThrows
    public <T> T findOneBy(Class<T> entityType, Field field, Object columnValue) {
        var result = findAllBy(entityType, field, columnValue);
        if (result.size() > 1) throw new IllegalArgumentException("The result contains more than one record");
        return result.get(0);
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

        return cache(entity);
    }

    private <T> T cache(T entity) {
        var entityKey = EntityKey.valueOf(entity);
        var cachedEntity = entityCache.get(entityKey);
        if (cachedEntity != null) {
            return (T) cachedEntity;
        } else {
            entityCache.put(entityKey, entity);
            return entity;
        }
    }

    public void verifySessionIsOpened() {
        if (!isOpened()) throw new RuntimeException("Session is closed");
    }

    public boolean isOpened() {
        return openStatus;
    }

    public void close() {
        entityCache.clear();
        openStatus = false;
    }
}
