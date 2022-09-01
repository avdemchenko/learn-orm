package learnorm.session;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.ResultSet;

@RequiredArgsConstructor
public class SessionImpl implements Session {

    private final DataSource dataSource;

    @Override
    @SneakyThrows
    public <T> T find(Class<T> entityType, Object id) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.createStatement()) {
                var resultSet = statement.executeQuery("SELECT * FROM persons where id = " + id);
                resultSet.next();
                return createEntityFromResultSet(entityType, resultSet);
            }
        }
    }

    private <T> T createEntityFromResultSet(Class<T> entityType, ResultSet resultSet) {
        return null;
    }
}
