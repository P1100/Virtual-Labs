package it.polito.ai.es2._provecodicelearning;

import it.polito.ai.es2.entities.JwtRole;

public interface MyTestingService {
  void entity_manager_test();
  
  boolean addUser(String user, String pass, JwtRole role);
  
  boolean checkUser(String user, String pass);
}
