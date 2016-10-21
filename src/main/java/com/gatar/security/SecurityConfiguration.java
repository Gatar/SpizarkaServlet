package com.gatar.security;

import com.gatar.database.AccountDAO;
import com.gatar.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Properties;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements SecurityConfigurationInterface{

	private final static String REALM = "SPIZARKA_REALM";

	@Autowired
    AccountDAO accountDAO;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception{

		auth.inMemoryAuthentication().withUser(adminAccount.getUsername()).password(adminAccount.getPassword()).roles("ADMIN");

		List<Account> registeredAccounts = accountDAO.findAll();
		for(Account account : registeredAccounts){
            final String role = account.getAuthority();
			auth.inMemoryAuthentication().withUser(account.getUsername()).password(account.getPassword()).roles(role);
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/*/**").hasAnyRole("USER","ADMIN")
				.and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/addNewAccount");
	}

	@Bean
	public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
		return new CustomBasicAuthenticationEntryPoint();
	}
}
