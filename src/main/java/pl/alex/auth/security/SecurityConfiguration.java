package pl.alex.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private final Environment environment;
  private final UserDetailsService userDetailsService;

  public SecurityConfiguration(Environment environment, UserDetailsService userDetailsService) {
    this.environment = environment;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
        AuthenticationManagerBuilder.class);

    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests.requestMatchers("/**")
                .access(new WebExpressionAuthorizationManager(
                    "hasIpAddress('" + environment.getProperty("gateway.ip") + "')")))
        .sessionManagement(sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
    // Custom UsernamePasswordAuthFilter
    http.addFilter(
            new AuthenticationFilter(authenticationManager,environment, userDetailsService))
        .authenticationManager(authenticationManager);

    return http.build();
  }

  @Bean
  BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
