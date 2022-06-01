package br.com.cjt.easybim.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	// Configurações de autenticação
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
	}

	// Configurações de autorização
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET ,"/person").permitAll() // públicos
			.antMatchers(HttpMethod.GET, "/person/*").permitAll()
			.antMatchers(HttpMethod.GET, "/tabelacustosindices").permitAll()
			.antMatchers(HttpMethod.GET, "/tabelacustosindices/*").permitAll()
			.anyRequest().authenticated()
			.and().csrf().disable();
			//.and().formLogin();
	}

	// Configurações de recursos estáticos (js, css, imagens, etc)
	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

}
