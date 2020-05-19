package it.polito.ai.es2.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

// logout viene fatto su url /logout
@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Bean
  PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Autowired
  private DataSource dataSource;
  
  //configura gli utenti e i ruoli
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth.userDetailsService(parametro_da_autowired_db_related);
//    auth.jdbcAuthentication().dataSource(dataSource)
    auth.inMemoryAuthentication()
        .withUser("Tizio")
        .password(encoder().encode("Alfa"))
        .roles("user")
        .and()
        .withUser("Caio")
        .password(encoder().encode("Beta"))
        .roles("user", "admin")
        .and()
        .withUser(encoder().encode("Sem"))
        .password("Gamma")
        .roles("admin");
  }
  
  //configura le URL da proteggere
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().disable() // inpiccio in fase di development, da abilitare in produzione. Cors si applica solo se c'è un browser client side
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/**").hasRole("user")
//        .antMatchers("/", "index.html").permitAll()
        //.authenticated()
        .and()
        //chiedo a spring security di creare pagina di login (tutta la struttura)
        .formLogin() // crea un controllore per l'accesso
        // default is username (nome elemento input html)
        .usernameParameter("custom_user")
        // default is password
        .passwordParameter("custom_pass")
        .and().sessionManagement().disable();
/*
        // ---> part below NOT WORKING !?
        // default is /login with an HTTP get
        .loginPage("/auth/login")
        // default is /login?error
        .failureUrl("/auth/login?failed");
        // default is /login with an HTTP post;
        .loginProcessingUrl("/auth/login/process");
*/
  }
  
  //configura la catena dei filtri di sicurezza
  @Override
  public void configure(WebSecurity web) throws Exception {
    super.configure(web);
  }
}
