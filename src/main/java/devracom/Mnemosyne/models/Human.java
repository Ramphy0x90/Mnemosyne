package devracom.Mnemosyne.models;

import jakarta.persistence.*;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  Human model class.
 *
 *  @author  Ramphy Aquino Nova
 *  @version 2023.03.26
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private Date birthDate;
    @Column(columnDefinition = "boolean default true")
    private Boolean searchable;
    @OneToOne(mappedBy = "human")
    private Account account;
}




