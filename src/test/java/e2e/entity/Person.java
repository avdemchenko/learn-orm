package e2e.entity;

import learnorm.annotation.Column;
import learnorm.annotation.Id;
import learnorm.annotation.OneToMany;
import learnorm.annotation.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Table("persons")
public class Person {

    @Id
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @OneToMany
    private List<Note> notes = new ArrayList<>();
}
