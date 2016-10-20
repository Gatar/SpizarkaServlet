package com.gatar.security;

import com.gatar.database.AccountDAO;
import com.gatar.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final static String REALM = "SPIZARKA_REALM";

	@Autowired
    AccountDAO accountDAO;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception{
		List<Account> registeredAccounts = accountDAO.findAll();
		for(Account account : registeredAccounts){
			auth.inMemoryAuthentication().withUser(account.getUsername()).password(account.getPassword()).roles("USER");
		}

		//TODO Delete it after or make some beutiful, interface of smthg
		auth.inMemoryAuthentication().withUser("gatar").password("password").roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/*/**").hasRole("USER")
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
