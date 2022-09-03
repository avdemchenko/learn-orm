package learnorm.session;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class SessionFactoryImpl implements SessionFactory {

    private final DataSource dataSource;

    @Override
    public Session openSession() {
        return new SessionImpl(dataSource);
    }
}
