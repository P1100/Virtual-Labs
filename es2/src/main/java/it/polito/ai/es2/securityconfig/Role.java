package it.polito.ai.es2.securityconfig;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

// TODO: cleanup, check uppercase equals
@Data
@NoArgsConstructor
@Entity(name = "role")
//@Table(name = "authorities", uniqueConstraints = @UniqueConstraint(columnNames = {"username","authority"}))
public class Role {
  //    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//    @Column(nullable = false, unique = true)
  @Id
  private String name; //name
  @ManyToMany(mappedBy = "roles")
  @ToString.Exclude
  private List<User> users = new ArrayList<>();
  
  public Role(String role) {
    this.name = role;
  }
}

