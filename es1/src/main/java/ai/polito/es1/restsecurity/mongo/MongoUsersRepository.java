package ai.polito.es1.restsecurity.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

//db.users.insert({  "username" : "admin",  "password" : "$2a$10$AjHGc4x3Nez/p4ZpvFDWeO6FGxee/cVqj5KHHnHfuLnIOzC5ag4fm"})
//db.users.insert({  "username" : "user",  "password" : "pass"})

//@Repository
public interface MongoUsersRepository extends MongoRepository<Users, String> {
  Users findByUsername(String username);
}