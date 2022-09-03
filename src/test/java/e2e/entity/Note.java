package e2e.entity;

import learnorm.annotation.Column;
import learnorm.annotation.Id;
import learnorm.annotation.ManyToOne;
import learnorm.annotation.Table;
import lombok.Data;

@Data
@Table("notes")
public class Note {

    @Id
    private Long id;

    private String body;

    @ManyToOne
    @Column("person_id")
    private Person person;
}
