package me.grgamer2626.configuration;

import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final String[] permitAll = {"/css/**", "/js/**", "/img/**", "/", "/login", "/registration", "/forget-password", "/about-us", "how-to-play", "/rules", "/contact" };
	private final UserRepository userRepository;
	
	@Autowired
	public SecurityConfig(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Bean
	public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests -> (requests
						.requestMatchers(permitAll).permitAll()
						.requestMatchers("/admin").hasAnyRole("ADMIN")
						.anyRequest())
				.authenticated())
				
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/lobby")
						.usernameParameter("nickName")
						.passwordParameter("password"))
				
				.httpBasic(Customizer.withDefaults());
		
		return http.build();
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider =  new DaoAuthenticationProvider(getPasswordEncoder());
		authProvider.setUserDetailsService(userDetailsService());
		return authProvider;
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			User user = userRepository.findByName(username);
			
			if(user == null) throw new UsernameNotFoundException(username);
			
			List<String> roles = user.getRoles().stream()
					.map(role -> role.getName().name())
					.collect(Collectors.toList());
			
			List<GrantedAuthority> authorityUtils = AuthorityUtils.createAuthorityList(roles);
			
			return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorityUtils );
		};
	}
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		int strength = 10; //the higher, the safer but longer time
		return new BCryptPasswordEncoder(strength);
	}

}
