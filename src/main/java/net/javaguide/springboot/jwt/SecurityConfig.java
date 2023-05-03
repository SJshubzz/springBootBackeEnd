package net.javaguide.springboot.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	CustomrUserDetailService customrUserDetailService;

	@Autowired
	jwtFilter jwfilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).and().csrf()
				.disable().authorizeHttpRequests()
				.requestMatchers("user/login", "user/forgotPassword" , "user/changePassword" , "user/signup","user/update"
						,"user/get","category/add","category/get","category/update","product/add","product/get","product/update"
						,"product/delete/{id}","product/updateStatus","product/getCategory/{id}","product/getByid/{id}"
						,"bill/generateReport")
				.permitAll().anyRequest().authenticated().and().exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		return http.build();

	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.customrUserDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
//
//http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).and().csrf()
//.disable().authorizeHttpRequests().requestMatchers("user/login", "user/forgotpassword", "/user/signup")
//.permitAll().anyRequest().authenticated().and().exceptionHandling().and().sessionManagement()
//.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//http.addFilterBefore(jwfilter, UsernamePasswordAuthenticationFilter.class);
//
