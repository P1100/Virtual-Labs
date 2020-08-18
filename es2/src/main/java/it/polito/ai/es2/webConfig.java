package it.polito.ai.es2;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
//@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class webConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").maxAge(9999999).allowedOrigins("*")
        .allowedMethods("*").allowedHeaders("*");
  }
}
