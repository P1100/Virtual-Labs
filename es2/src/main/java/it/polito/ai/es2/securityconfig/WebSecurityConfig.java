package it.polito.ai.es2.securityconfig;

import it.polito.ai.es2.securityconfig.jwt.JwtAuthenticationEntryPoint;
import it.polito.ai.es2.securityconfig.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  @Autowired
  private UserDetailsService userDetailsServiceImpl;
  @Autowired
  private JwtRequestFilter jwtRequestFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Autowired // using bean, method name is irrelevant
  public void configure(AuthenticationManagerBuilder auth_builder, DataSource dataSource) throws Exception {
    auth_builder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
//    auth_builder.inMemoryAuthentication()
//        .withUser("mem")
//        .password(passwordEncoder().encode("mem"))
//        .roles("USER", "GUEST", "ADMIN");
  }

  /**
   * User: 'admin'. Password: 'a'
   * Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU5MzAzODgwMiwiaWF0IjoxNTkzMDIwODAyfQ.eNvYEI3XidkaWBl9bt3wUPSEOlV4Yg3TA5C17eB6L0TRRW07U-lC8tv4nVBjBEIU5c4-USIIX4eZc4mDMexxeg
   */
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .httpBasic().disable()
        .csrf().disable() // MUST DISABLE!! Otherwise all POST fail
        .cors().and()
//       .formLogin()
        .authorizeRequests()
//        .antMatchers("/jwt/authenticate", "/jwt/register").permitAll()
//        .antMatchers("/notification/**").permitAll()
//        .antMatchers("/testing/**").permitAll()
//        .antMatchers("/api").hasRole("ADMIN")
//        .antMatchers("/*").authenticated()
//        .antMatchers("/api").authenticated()
        .anyRequest().permitAll()
        .and()
        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint) // TODO: Move to Global handler
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    ;

    // Add a filter to validate jwt tokens with every request
    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").maxAge(9999999).allowedMethods("*").allowedOrigins("*").allowedHeaders("*");
  }
}