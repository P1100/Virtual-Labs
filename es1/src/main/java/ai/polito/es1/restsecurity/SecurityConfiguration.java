package ai.polito.es1.restsecurity;

import ai.polito.es1.restsecurity.security.jwt.JwtSecurityConfigurer;
import ai.polito.es1.restsecurity.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

// logout viene fatto su url /logout
@Configuration
@EnableConfigurationProperties
@EnableWebSecurity
// @EnableGlobalMethodSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
/*  @Autowired
  MongoUserDetailsService userDetailsService;*/
  
  @Autowired
  JwtTokenProvider jwtTokenProvider;
  
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
/*
  @Bean
  PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }*/
/*
  //configura gli utenti e i ruoli
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
    
*//*
    auth.inMemoryAuthentication()
        .withUser("Tizio")
        .password(encoder().encode("Alfa"))
        .roles("user")
        .and()
        .withUser("Caio")
        .password(encoder().encode("Beta"))
        .roles("user", "ADMIN")
        .and()
        .withUser(encoder().encode("Sempronio"))
        .password("Gamma")
        .roles("admin");
    *//*
  }
  */
  
  //configura le URL da proteggere
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/signin").permitAll()
        .antMatchers(HttpMethod.GET, "/esempio").permitAll()
        .antMatchers(HttpMethod.GET, "/me").permitAll()
        .antMatchers(HttpMethod.DELETE, "/r").hasRole("user")
        .anyRequest().authenticated()
        .and()
        .apply(new JwtSecurityConfigurer(jwtTokenProvider));
    
/*    http
        .csrf().disable()
        .authorizeRequests().anyRequest().authenticated()
        .and().httpBasic()
        .and().sessionManagement().disable();
    */
/*
    http.authorizeRequests().anyRequest().permitAll(); //        .antMatchers("/", "index.html").permitAll()
    if(true) return;

http.cors().disable() // inpiccio in fase di development, da abilitare in produzione. Cors si applica solo se c'è un browser client side
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/**").hasRole("user")
        .and()
        //chiedo a spring security di creare pagina di login (tutta la struttura)
        .formLogin() // crea un controllore per l'accesso
        // default is username (nome elemento input html)
        .usernameParameter("custom_user")
        // default is password
        .passwordParameter("custom_pass")
        .and().sessionManagement().disable();
    */
  }
/*
  //configura la catena dei filtri di sicurezza
  @Override
  public void configure(WebSecurity web) throws Exception {
    super.configure(web);
  }*/
}
