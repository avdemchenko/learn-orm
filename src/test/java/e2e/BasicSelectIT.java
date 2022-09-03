package e2e;

import e2e.entity.Note;
import e2e.entity.Person;
import learnorm.session.SessionFactoryImpl;
import learnorm.session.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static util.TestUtils.initializeDatasource;

@Testcontainers
class BasicSelectIT {

    private DataSource dataSource;

    @Container
    private final MySQLContainer<?> container = new MySQLContainer<>("mysql:latest")
            .withNetwork(Network.newNetwork())
            .withDatabaseName("person")
            .withUsername("admin")
            .withPassword("admin")
            .withInitScript("db/init.sql");

    @BeforeEach
    public void setUp() {
        dataSource = initializeDatasource(container);
    }

    @Test
    @DisplayName("Should find entities by id")
    public void shouldFindEntitiesById() {
        // given
        SessionFactory sessionFactory = new SessionFactoryImpl(dataSource);
        var session = sessionFactory.openSession();

        // when
        var person = session.find(Person.class, 1L);

        // and
        var note = session.find(Note.class, 3L);

        // then
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getFirstName()).isEqualTo("John");
        assertThat(person.getLastName()).isEqualTo("McClane");

        // and
        assertThat(note.getId()).isEqualTo(3);
        assertThat(note.getBody()).isEqualTo("Welcome to the party, pal");
    }
}
