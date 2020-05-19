package it.polito.ai.es2.repositories;

import it.polito.ai.es2.entities.JwtUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtUserRepository extends CrudRepository<JwtUser, Integer> {
	JwtUser findTopByUsername(String username);
}