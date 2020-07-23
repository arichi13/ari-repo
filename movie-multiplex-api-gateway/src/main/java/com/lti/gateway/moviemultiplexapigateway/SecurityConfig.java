package com.lti.gateway.moviemultiplexapigateway;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		/*UserBuilder builder = User.withDefaultPasswordEncoder(); // noop(plain text) | bcrypt
		auth.inMemoryAuthentication()
		.withUser(builder.username("admin").password("root"));*/
		System.out.println("configure1 called");
		auth
        .inMemoryAuthentication()
            .withUser("arichi232")
            .password("{noop}arichi232")
            .roles("ADMIN")
        .and()
            .withUser("joe")
            .password("{noop}password")
            .roles("ADMIN");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("configure2 called");
		http
			.cors().and()
			.csrf().disable()
			.authorizeRequests()
			//.antMatchers("/micro-post/api/post/add/**").hasRole("ADMIN")
			.antMatchers("/micro-movie/movie-mgmt/adminLogin**").hasRole("ADMIN")
			.antMatchers("/micro-multiplex/multiplex-mgmt/admin/**").hasRole("ADMIN")
			.antMatchers("/micro-movie/movie-mgmt/admin/**").hasRole("ADMIN").and()
			.httpBasic();
			
	}

}
