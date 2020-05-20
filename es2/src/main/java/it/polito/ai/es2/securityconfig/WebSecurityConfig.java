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
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  @Autowired
  private UserDetailsService userDetailsServiceImpl;
  @Autowired
  private JwtRequestFilter jwtRequestFilter;
  
  //  @Autowired  --> with autowire i can change method name from configure to anything else
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
  
  @Autowired // non override perchè ho dichiarato prima il bean per AuthenticationManagerBuilder. Nota: nome metodo irrelevante con autowired
  public void configure(AuthenticationManagerBuilder auth_builder, DataSource dataSource) throws Exception {
    auth_builder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    
    auth_builder.jdbcAuthentication().dataSource(dataSource)
//        .withUser("u1").password(passwordEncoder().encode("pass")).roles("user","Guest")
//        .and().withUser("U2").password(passwordEncoder().encode("PASS")).roles("User","Guest")
//        .and().withUser("User_jdbc").password(passwordEncoder().encode("Pass_Jdbc")).roles("User","guest","admin")
/*        .usersByUsernameQuery("select email,password,enabled "
                                  + "from bael_users "
                                  + "where email = ?")
        .authoritiesByUsernameQuery("select email,authority "
                                        + "from authorities "
                                        + "where email = ?");
   */;
    auth_builder.inMemoryAuthentication()
        .withUser("Tizio")
        .password(passwordEncoder().encode("Alfa"))
        .roles("user")
        .and()
        .withUser("u")
        .password(passwordEncoder().encode("p"))
        .roles("guest");
  }
  
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
//        .httpBasic().disable()
        .csrf().disable()
        .cors().disable()
        .formLogin()
        // default is username (nome elemento input html)
        .usernameParameter("custom_user")
        // default is password
        .passwordParameter("custom_pass")
        .and() //.antMatcher("/**") --> applies to all?
        .authorizeRequests()
        .antMatchers("/jwt/authenticate", "/jwt/register").permitAll()
        .antMatchers("/notification/**").permitAll()
        .antMatchers("/testing/**").permitAll()
        .antMatchers("/*").authenticated()
        .antMatchers("/users/**").authenticated()
//        .antMatchers("/API/**").hasRole("professor")
        // all other requests need to be authenticated
        .anyRequest().authenticated()
//        .and()
//        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
//        .and()
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    ;
    
    // Add a filter to validate the tokens with every request
//    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }
  
  //configura la catena dei filtri di sicurezza
  @Override
  public void configure(WebSecurity web) throws Exception {
    super.configure(web);
  }
}