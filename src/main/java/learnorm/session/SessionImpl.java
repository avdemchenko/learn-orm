package learnorm.session;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class SessionImpl implements Session {

    private JdbcEntityDao jdbcEntityDao;

    public SessionImpl(DataSource dataSource) {
        jdbcEntityDao = new JdbcEntityDao(dataSource);
    }

    @Override
    @SneakyThrows
    public <T> T find(Class<T> entityType, Object id) {
        return jdbcEntityDao.findById(entityType, id);
    }

    @Override
    public void close() {
        jdbcEntityDao.close();
    }
}
