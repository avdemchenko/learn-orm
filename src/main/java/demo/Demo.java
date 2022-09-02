package demo;

import com.mysql.cj.jdbc.MysqlDataSource;
import demo.entity.Note;
import demo.entity.Person;
import learnorm.session.SessionFactory;
import learnorm.session.SessionFactoryImpl;

import javax.sql.DataSource;

public class Demo {

    public static void main(String[] args) {
        SessionFactory sessionFactory = new SessionFactoryImpl(initializeDataSource());
        var session = sessionFactory.openSession();

        var person = session.find(Person.class, 1L);
        System.out.println(person);

        var note = session.find(Note.class, 2L);
        System.out.println(note);
    }

    private static DataSource initializeDataSource() {
        var dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/person");
        dataSource.setUser("admin");
        dataSource.setPassword("admin");
        return dataSource;
    }
}
