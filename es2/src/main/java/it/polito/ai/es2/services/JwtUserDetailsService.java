package it.polito.ai.es2.services;

import java.util.ArrayList;

import it.polito.ai.es2.repositories.JwtUserRepository;
import it.polito.ai.es2.dtos.JwtUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.polito.ai.es2.entities.JwtUser;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private JwtUserRepository jwtUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		JwtUser user = jwtUserRepository.findTopByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}
	
	public JwtUser save(JwtUserDTO user) {
		JwtUser newUser = new JwtUser();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		return jwtUserRepository.save(newUser);
	}
}