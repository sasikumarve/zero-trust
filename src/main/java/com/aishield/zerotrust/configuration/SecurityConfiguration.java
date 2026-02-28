/**
 * 
 */
package com.aishield.zerotrust.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.aishield.zerotrust.security.JwtFilter;

/**
 * @author sasikumar
 *
 */
@Configuration
public class SecurityConfiguration {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

    http.csrf(csrf -> csrf.disable()).addFilterBefore(jwtFilter,
        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/login").permitAll().anyRequest().permitAll());

    return http.build();
  }

}
