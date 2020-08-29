package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Si è scelto di non aggiungere ID (generated), per cercare di uniformarsi a schema di default di jdbcauthentication, rendendo la join table users_roles chiara.
 */
// TODO: rivedere noargs, e rimuovere costruttori non utilizzati
@Data
@NoArgsConstructor
@Entity(name = "role")
//@Table(name = "authorities")//, uniqueConstraints = @UniqueConstraint(columnNames = {"username","authority"}))
public class Role {
  @Id
  private String name;
  private String description;
  @ManyToMany(mappedBy = "roles")
  @ToString.Exclude
  private List<User> users = new ArrayList<>();
  
  public Role(String role) {
    this.name = role;
    this.description = "";
  }
}

