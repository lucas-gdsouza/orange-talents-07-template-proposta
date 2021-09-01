package br.com.zupacademy.propostas.customizations.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests
                        .antMatchers(HttpMethod.GET, "/actuator/**").hasAuthority("SCOPE_escopo-proposta")
                        .antMatchers(HttpMethod.GET, "/api/v1/propostas/**").hasAuthority("SCOPE_escopo-proposta")
                        .antMatchers(HttpMethod.POST, "/api/v1/avisos/**").hasAuthority("SCOPE_escopo-proposta")
                        .antMatchers(HttpMethod.POST, "/api/v1/biometrias/**").hasAuthority("SCOPE_escopo-proposta")
                        .antMatchers(HttpMethod.POST, "/api/v1/bloqueios/**").hasAuthority("SCOPE_escopo-proposta")
                        .antMatchers(HttpMethod.POST, "/api/v1/propostas/**").hasAuthority("SCOPE_escopo-proposta")
                        .anyRequest().authenticated()
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}