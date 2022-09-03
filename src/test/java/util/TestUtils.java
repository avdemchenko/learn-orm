package util;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.testcontainers.containers.JdbcDatabaseContainer;

import javax.sql.DataSource;

public class TestUtils {

    public static DataSource initializeDatasource(JdbcDatabaseContainer container) {
        var dataSource = new MysqlDataSource();
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUser(container.getUsername());
        dataSource.setPassword(container.getPassword());
        return dataSource;
    }
}
