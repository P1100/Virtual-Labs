package it.polito.ai.es2.services;

import it.polito.ai.es2.securityconfig.User;

public interface SecurityService {
  boolean hasPermissions(String username, String corso);
  
  boolean hasPermissions(String username);
  
  boolean hasAdminPermissions(User user);
}