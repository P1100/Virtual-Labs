package it.polito.ai.es2.securityconfig;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "roles")
//@Table(name = "authorities", uniqueConstraints = @UniqueConstraint(columnNames = {"username","authority"}))
public class Role {
    //    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//    @Column(nullable = false, unique = true)
    @Id
    private String name; //name
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();
}

