package it.polito.ai.es2.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Si è scelto di non aggiungere ID (generated), per cercare di uniformarsi a schema di default di jdbcauthentication, rendendo la join table users_roles chiara.
 */
@Data
@NoArgsConstructor
@Entity
public class Role {
  @Id
  private String name;
  // Only the Role name is used by Spring Security, optional parameter
  private String description;

  @ManyToMany(mappedBy = "roles")
  private List<User> users = new ArrayList<>();

  public Role(String role) {
    name = role;
    description = "";
  }
}

