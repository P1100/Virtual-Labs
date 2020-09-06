package it.polito.ai.es2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

public abstract class CommonURL {
  @Value("${server.port}")
  private String port;
  @Value("${server.address}")
  private String address;
  @Value("${myprop.prefixurl}")
  private String httpPrefix; // http or https
  public String baseUrl = "";
  @Autowired
  Environment environment;

  @PostConstruct
  public void init() {
    baseUrl = httpPrefix + "://" + address + ":" + port;
  }
}
