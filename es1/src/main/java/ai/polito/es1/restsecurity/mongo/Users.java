package ai.polito.es1.restsecurity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Users {
  @Id
  public ObjectId _id;
  public String username;
  public String password;
}
