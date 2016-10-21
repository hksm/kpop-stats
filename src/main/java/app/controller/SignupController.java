package app.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.Role;
import app.model.User;
import app.repository.RoleRepository;
import app.repository.UserRepository;

@RestController
public class SignupController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@RequestMapping(value="/signup", method=RequestMethod.POST, produces=MediaType.TEXT_PLAIN_VALUE)
	public String signup(@RequestBody User user) {
		try {
			Set<Role> roles = new HashSet<>();
			Role role = roleRepository.findByRole("ROLE_USER");
			if (role == null) {
				role = roleRepository.save(new Role("ROLE_USER"));
			}
			roles.add(role);
			user.setRoles(roles);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setEnabled(true);
			userRepository.save(user);
		} catch(Exception e) {
			e.printStackTrace();
			return "Error: " + e.toString();
		}
		return "Registration completed!";
	}
}