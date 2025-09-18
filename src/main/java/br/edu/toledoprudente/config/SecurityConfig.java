package br.edu.toledoprudente.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        DataSource dataSource;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests((requests) -> requests
                                                .requestMatchers("/homeindex/login").permitAll()
                                                .requestMatchers("/homeindex/index").permitAll()
                                                .requestMatchers("/image/**").permitAll()
                                                .requestMatchers("/js/**").permitAll()
                                                .requestMatchers("/fonts/**").permitAll()
                                                .requestMatchers("/css/**").permitAll()
                                                .requestMatchers("/vendor/**").permitAll()
                                                .requestMatchers("/icons/**").permitAll()
                                                .requestMatchers("/usuario/cadastro").permitAll()
                                                .requestMatchers("/usuario/salvar").permitAll()
                                                .requestMatchers("/usuario/listar").hasAuthority("ADM")
                                                .requestMatchers("/sites/listarF").hasAuthority("ADM")
                                                .requestMatchers("/usuario/cadastroF").hasAuthority("ADM")
                                                .requestMatchers("/paginas/listarF").hasAuthority("ADM")
                                                .requestMatchers("/paginas/cadastroF").hasAuthority("ADM")
                                                .anyRequest().authenticated()

                                )
                                .formLogin((form) -> form
                                                .loginPage("/homeindex/login")
                                                .defaultSuccessUrl("/usuario/perfil", true)
                                                .permitAll()
                                                .failureUrl("/homeindex/login-error"))
                                .logout((logout) -> logout.invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/homeindex/login").permitAll());
                return http.build();
        }
}
