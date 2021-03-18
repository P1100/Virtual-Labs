package it.polito.ai.es2.securityconfig.jwt;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class JwtResponse implements Serializable {
  private static final long serialVersionUID = -8091879091924046844L;
  private final String token;
  private String role;

  public JwtResponse(String jwttoken) {
    token = jwttoken;
  }

  public String getToken() {
    return token;
  }
}
