package demo.entity;

import learnorm.annotation.Id;
import learnorm.annotation.Table;
import lombok.Data;

@Data
@Table("notes")
public class Note {

    @Id
    private Long id;

    private String body;
}
