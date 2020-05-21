package it.polito.ai.es2.repositories;

import it.polito.ai.es2.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
	User findTopByUsername(String username);
}

/*
//db.users.insert({  "username" : "admin",  "password" : "$2a$10$AjHGc4x3Nez/p4ZpvFDWeO6FGxee/cVqj5KHHnHfuLnIOzC5ag4fm"})
//db.users.insert({  "username" : "user",  "password" : "pass"})
//@Repository
public interface MongoUsersRepository extends MongoRepository<JwtUser, String> {
	JwtUser findByUsername(String username);
	}
*/
